package ingbank.com.tr.happybanking.map.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.androidmapsextensions.ClusterOptions;
import com.androidmapsextensions.ClusteringSettings;
import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.Marker;
import com.androidmapsextensions.MarkerOptions;
import com.androidmapsextensions.Polyline;
import com.androidmapsextensions.PolylineOptions;
import com.androidmapsextensions.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import ingbank.com.tr.happybanking.R;
import ingbank.com.tr.happybanking.common.request.map.DirectionRequest;
import ingbank.com.tr.happybanking.common.util.PolyUtil;
import ingbank.com.tr.happybanking.map.MapActivity;
import ingbank.com.tr.happybanking.map.controller.MapController;
import ingbank.com.tr.happybanking.map.controller.MapLocation;
import ingbank.com.tr.happybanking.map.model.map.Channel;
import ingbank.com.tr.happybanking.map.model.map.Route;
import ingbank.com.tr.happybanking.ui.BaseFragment;

public class AtmBranchMapFragment extends BaseFragment {

    private static final double CLUSTER_SIZE = 120;
    private static final float MAX_ZOOM_LEVEL = 21f;
    private static final float ZOOM_LEVEL = 18f;
    protected GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private Marker mMyLocationMarker;
    private MapItemContainerFragment mMapItemContainerFragment;
    private float currentZoomLevel;
    private Marker selectedMarker, selectedCluster;
    private MapActivity mContext;
    private RelativeLayout rlMapItemContainerFragment;
    private BitmapDescriptor bitmapDescriptorAtm, bitmapDescriptorBranch;
    private ImageView ivMyLocationButton;
    private HashMap<Channel, Marker> mapItemHashMap;
    private Polyline mRouteLine;
    private boolean mRouteDrawn = false;
    private Gson mJsonSerializer;
    private MapLocation mLocation;
    private boolean mIsCameraZoomedByList = false;
    private boolean mIsCameraFocusedDefaultLocation = false;
    private boolean mIsCameraFocusedGPSLocation = false;
    private AtmAndBranchClusterOptionsProvider mClusterOptionsProvider;
    private AtmAndBranchClusterOptionsProviderPressed mClusterOptionsProviderClicked;

    public static boolean isAllItemsInSameLocation(List<? extends Marker> l) {
        if (l != null && l.size() > 1) {
            Marker refMarker = l.get(0);
            LatLng refPosition = refMarker.getPosition();
            double refLat = refPosition.latitude;
            double refLong = refPosition.longitude;
            for (int i = 1; i < l.size(); i++) {
                Marker marker = l.get(i);
                LatLng position = marker.getPosition();
                if (refLat != position.latitude && refLong != position.longitude)
                    return false;
            }
            return true;
        }

        return false;
    }




    private void createMapFragmentIfNeeded() {
        FragmentManager fm = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
        if (mapFragment == null) {
            mapFragment = createMapFragment();
            FragmentTransaction tx = fm.beginTransaction();
            tx.add(R.id.map_container, mapFragment);
            tx.commit();
        }
    }

    protected SupportMapFragment createMapFragment() {
        return SupportMapFragment.newInstance();
    }

    public void displayLocationWarning() {
        createAlertDialog(getString(R.string.errorHeader83), getString(R.string.error83), getString(R.string.pgMap_open_location_settings), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);
                //get gps
            }
        }, getString(R.string.global_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }, true).show();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {


            mMap = mapFragment.getExtendedMap();
            if (mMap != null) {
                setUpMapContainerFragment();
                setUpMap();
            }
        }
    }

    /**
     * Sets up Map Item Detail Fragment
     */
    private void setUpMapContainerFragment() {
        hideMapItemContainerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
            mMapItemContainerFragment = (MapItemContainerFragment) getChildFragmentManager().findFragmentById(R.id.map_item_container);
            rlMapItemContainerFragment = (RelativeLayout) view.findViewById(R.id.rlMapItemContainerFragment);


            ivMyLocationButton = (ImageView) view.findViewById(R.id.ivMyLocationButton);
            mJsonSerializer = new Gson();
            setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            createMapFragmentIfNeeded();
            setUpClusteringViews(view);
            MapsInitializer.initialize(getActivity().getApplicationContext());

            if (bitmapDescriptorAtm == null || bitmapDescriptorBranch == null) {
                bitmapDescriptorAtm = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),
                        R.drawable.map_placemarker_atm));
                bitmapDescriptorBranch = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),
                        R.drawable.map_placemarker_branch));
            }
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_map_v2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mContext = ((MapActivity) getActivity());
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onViewSelected");
        }
    }

    protected void setUpMap() {

        mMap.setMyLocationEnabled(false); // we don't want default blue dot of Google Maps
        UiSettings settings = mMap.getUiSettings();
        settings.setAllGesturesEnabled(true);
        settings.setMyLocationButtonEnabled(true);

        updateClustering();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (!mRouteDrawn)
                    hideMapItemContainerFragment();
            }
        });

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                /**
                 * Hide Map Item Container Fragment if Zoom Out
                 */
                if (currentZoomLevel > cameraPosition.zoom) {
                    if (!mRouteDrawn)
                        hideMapItemContainerFragment();
                }
                if (mIsCameraZoomedByList) {
                    mIsCameraZoomedByList = false;
                    if (selectedCluster != null) {
                        if (isAllItemsInSameLocation(selectedCluster.getMarkers())) {
                            focusToSelectedCluster(selectedCluster);
                        } else {
                            focusToSelectedItem(selectedMarker);
                        }

                    }
                }

                currentZoomLevel = cameraPosition.zoom;
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                if (mRouteDrawn)
                    return true;

                hideMapItemContainerFragment();
                selectedMarker = null;
                selectedCluster = null;
                currentZoomLevel = mMap.getCameraPosition().zoom;
                rlMapItemContainerFragment.setVisibility(View.VISIBLE);

                mMapItemContainerFragment = (MapItemContainerFragment) getChildFragmentManager().findFragmentById(R.id.map_item_container);

                if (mContext != null && mContext.getIsFilterDialogVisible() != null) {
                    //Do not run Marker Specific codes if Filter Dialog is visible
                    if (mContext.getIsFilterDialogVisible()) {
                        return true;
                    }


                }

                if (marker.isCluster()) {
                    deClusterMarker(marker);
                    return true;

                    //marker.getClusterGroup() != -1 prevents MyLocationMarker being selected.
                } else if (marker.getClusterGroup() != -1 && mMapItemContainerFragment != null) {
                    focusToSelectedMarkerObs(marker);
                }
                return true;
            }
        });

        ivMyLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusToMyLocation(true);
            }
        });
    }

    /**
     * @param position
     */
    public void focusToSelectedPosition(LatLngBounds position) {
        if (rlMapItemContainerFragment.getVisibility() == View.INVISIBLE)
            rlMapItemContainerFragment.setVisibility(View.VISIBLE);

        if (mContext.getActiveFragment() == MapActivity.ActiveFragment.MAP) {
            currentZoomLevel = mMap.getCameraPosition().zoom;
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(position.getCenter().latitude, position.getCenter().longitude), currentZoomLevel));

        } else if (mContext.getActiveFragment() == MapActivity.ActiveFragment.LIST) {
            currentZoomLevel = 10.5f;
            changeCameraBounds(position);
        }

        //Animate to the selected item's position

        //Show Map Item Detail Fragment
//        showMapItemContainerFragment();
    }


    /**
     * Focus to the selected marker.
     * This method is being used both in Map Fragment and List Fragment
     *
     * @param marker Selected Marker
     */
    public void focusToSelectedMarker(final Marker marker) {
        selectedCluster = null;
        selectedMarker = marker;
        if (rlMapItemContainerFragment.getVisibility() == View.INVISIBLE)
            rlMapItemContainerFragment.setVisibility(View.VISIBLE);

        if (marker.isCluster()) {
            //selectedCluster = marker;
            //showMapItemContainerFragment();
            focusToSelectedCluster(marker);
            return;
        }

        if (mContext.getActiveFragment() == MapActivity.ActiveFragment.MAP) {
            currentZoomLevel = mMap.getCameraPosition().zoom;
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude), currentZoomLevel));
        } else if (mContext.getActiveFragment() == MapActivity.ActiveFragment.LIST) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude), 40));
            if (focusToSelectedItem(marker)) return;
        }

        showMapItemContainerFragment();
    }

    /**
     * Focus to the selected marker.
     * This method is being used both in Map Fragment and List Fragment
     *
     * @param marker Selected Marker
     */
    public void focusToSelectedMarkerObs(Marker marker) {
        if (rlMapItemContainerFragment.getVisibility() == View.INVISIBLE)
            rlMapItemContainerFragment.setVisibility(View.VISIBLE);

        if (mContext.getActiveFragment() == MapActivity.ActiveFragment.MAP) {
            currentZoomLevel = mMap.getCameraPosition().zoom;
        } else if (mContext.getActiveFragment() == MapActivity.ActiveFragment.LIST) {
            currentZoomLevel = ZOOM_LEVEL;
        }

        selectedMarker = marker;

        //Animate to the selected item's position
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude), currentZoomLevel));

        //Show Map Item Detail Fragment
        showMapItemContainerFragmentObs();
    }

    private boolean focusToSelectedItem(Marker marker) {
        List<Marker> displayedMarkers = mMap.getDisplayedMarkers();
        for (Marker displayedMarker : displayedMarkers) {
            if (displayedMarker.isCluster()) {
                List<Marker> markers = displayedMarker.getMarkers();
                Marker containerMarker = null;
                for (Marker clusterMarker : markers) {
                    if (marker.getPosition().latitude == clusterMarker.getPosition().latitude && marker.getPosition().longitude == clusterMarker.getPosition().longitude) {
                        containerMarker = clusterMarker;
                        break;
                    }
                }

                if (containerMarker != null) {
                    selectedCluster = displayedMarker;
                    mIsCameraZoomedByList = true;
                    CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(containerMarker.getPosition().latitude, containerMarker.getPosition().longitude), MAX_ZOOM_LEVEL);
                    mMap.moveCamera(cu);
//                    focusToSelectedCluster(displayedMarker);
                    return true;
                }

            }

        }

        return false;
    }


    public void focusToSelectedCluster(Marker cluster) {
        if (rlMapItemContainerFragment.getVisibility() == View.INVISIBLE)
            rlMapItemContainerFragment.setVisibility(View.VISIBLE);

        selectedCluster = cluster;
        currentZoomLevel = mMap.getCameraPosition().zoom;
        //Animate to the selected item's position
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(cluster.getPosition().latitude, cluster.getPosition().longitude), currentZoomLevel));

        //Show Map Item Detail Fragment
        showMapItemContainerFragment();
    }

    public void focusToSelectedClusterObs(Marker cluster) {
        if (rlMapItemContainerFragment.getVisibility() == View.INVISIBLE)
            rlMapItemContainerFragment.setVisibility(View.VISIBLE);

        selectedCluster = cluster;
        currentZoomLevel = mMap.getCameraPosition().zoom;
        //Animate to the selected item's position
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(cluster.getPosition().latitude, cluster.getPosition().longitude), currentZoomLevel));

        //Show Map Item Detail Fragment
        showMapItemContainerFragmentObs();
    }

    /**
     * Shows the MapItemDetail Fragment and changes the icon of the selected marker to pressed.
     */
    public void showMapItemContainerFragment() {
        if (mMapItemContainerFragment != null) {
            if (selectedCluster != null && selectedCluster.getMarkers() != null) {
                if (selectedCluster != null && !selectedMarker.isCluster())
                    mMapItemContainerFragment.setUpMapItemContainer(selectedCluster.getMarkers(), selectedMarker);
                else
                    mMapItemContainerFragment.setUpMapItemContainer(selectedCluster.getMarkers());

                ClusterOptions clusterOptions = mClusterOptionsProviderClicked.getClusterOptions(selectedCluster.getMarkers());
                selectedCluster.setIcon(clusterOptions.getIcon());
            } else if (selectedMarker != null && selectedMarker.getData() != null) {
                mMapItemContainerFragment.setUpMapItemContainer(new Vector<Marker>() {{
                    add(selectedMarker);
                }});

                if (((Channel) selectedMarker.getData()).getBankChannelType().equals(MapActivity.CHANNELTYPE_ATM)) {
                    selectedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.map_placemarker_atm_pressed)));
                } else if (((Channel) selectedMarker.getData()).getBankChannelType().equals(MapActivity.CHANNELTYPE_BRANCH)) {
                    selectedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.map_placemarker_branch_pressed)));
                }
            }

//            ivMyLocationButton.setEnabled(false);
            mMapItemContainerFragment.setVisibility(View.VISIBLE);
            ivMyLocationButton.setVisibility(View.GONE);

        }
    }


    /**
     * Shows the MapItemDetail Fragment and changes the icon of the selected marker to pressed.
     */
    public void showMapItemContainerFragmentObs() {
        if (mMapItemContainerFragment != null) {
            if (selectedMarker != null && selectedMarker.getData() != null) {
                mMapItemContainerFragment.setUpMapItemContainer(new Vector<Marker>() {{
                    add(selectedMarker);
                }});

                if (((Channel) selectedMarker.getData()).getBankChannelType().equals(MapActivity.CHANNELTYPE_ATM)) {
                    selectedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.map_placemarker_atm_pressed)));
                } else if (((Channel) selectedMarker.getData()).getBankChannelType().equals(MapActivity.CHANNELTYPE_BRANCH)) {
                    selectedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.map_placemarker_branch_pressed)));
                }
            } else if (selectedCluster != null && selectedCluster.getMarkers() != null) {
                mMapItemContainerFragment.setUpMapItemContainer(selectedCluster.getMarkers());
                ClusterOptions clusterOptions = mClusterOptionsProviderClicked.getClusterOptions(selectedCluster.getMarkers());
                selectedCluster.setIcon(clusterOptions.getIcon());
                //TODO Change Cluster Icon
//                if (((Channel) selectedCluster.getMarkers().get(0).getData()).getBankChannelType().equals(MapActivity.CHANNELTYPE_ATM)) {
//                    selectedCluster.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.map_placemarker_atm_pressed)));
//                } else if (((Channel) selectedCluster.getMarkers().get(0).getData()).getBankChannelType().equals(MapActivity.CHANNELTYPE_BRANCH)) {
//                    selectedCluster.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.map_placemarker_branch_pressed)));
//                }
            }

            //TODO Animate does not function properly on Turkcell Maxi Pro
//            ViewCompat.animate(mMapItemContainerFragment.getView())
//                    .alpha(1f)
//                    .setDuration(300)
//                    .translationY(0f)
//                    .setInterpolator(new DecelerateInterpolator())
//                    .start();
            mMapItemContainerFragment.setVisibility(View.VISIBLE);
            ivMyLocationButton.setVisibility(View.GONE);
//            if (mMyLocationMarker != null)
//                mMyLocationMarker.setVisible(false);
        }
    }

    /**
     * Hides the MapItemDetail Fragment and changes the icon of the selected marker to default.
     */
    public void hideMapItemContainerFragment() {
        if (mMapItemContainerFragment != null) {
            if (selectedMarker != null) {
                if (((Channel) selectedMarker.getData()).getBankChannelType().equals(MapActivity.CHANNELTYPE_ATM)) {
                    selectedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.map_placemarker_atm)));
                } else if (((Channel) selectedMarker.getData()).getBankChannelType().equals(MapActivity.CHANNELTYPE_BRANCH)) {
                    selectedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.map_placemarker_branch)));
                }

                selectedMarker = null;

            }

            if (selectedCluster != null) {
                ClusterOptions clusterOptions = mClusterOptionsProvider.getClusterOptions(selectedCluster.getMarkers());
                selectedCluster.setIcon(clusterOptions.getIcon());
                //TODO Change Cluster Icon
//                if (((Channel) selectedCluster.getMarkers().get(0).getData()).getBankChannelType().equals(MapActivity.CHANNELTYPE_ATM)) {
//                    selectedCluster.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.map_placemarker_atm)));
//                } else if (((Channel) selectedCluster.getMarkers().get(0).getData()).getBankChannelType().equals(MapActivity.CHANNELTYPE_BRANCH)) {
//                    selectedCluster.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.map_placemarker_branch)));
//                }

                selectedCluster = null;
            }

            //TODO Animate does not function properly on Turkcell Maxi Pro
//            ViewCompat.animate(mMapItemContainerFragment.getView())
//                    .alpha(0f)
//                    .setDuration(300)
//                    .translationY(mMapItemContainerFragment.getView().getHeight())
//                    .setInterpolator(new DecelerateInterpolator())
//                    .start();

            mMapItemContainerFragment.setVisibility(View.GONE);
            if (mMyLocationMarker != null) {
                mMyLocationMarker.setVisible(MapController.GetInstance().isLocationServicesEnabled() && !mLocation.isDefault());
            }

            if (MapController.GetInstance().isLocationServicesEnabled())
                ivMyLocationButton.setVisibility(View.VISIBLE);
            else
                ivMyLocationButton.setVisibility(View.GONE);


//            ivMyLocationButton.setEnabled(true);
        }
    }

    private void deClusterMarker(Marker marker) {
        if (!marker.isCluster())
            return;

        if (isAllItemsInSameLocation(marker.getMarkers())) {
            //selectedCluster = marker;
            //showMapItemContainerFragment();
            selectedMarker = null;
            focusToSelectedClusterObs(marker);
            return;
        }

        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (Marker item : marker.getMarkers()) {
            builder.include(item.getPosition());
        }
        final LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String stringExtra = intent.getStringExtra(AtmBranchListFragment.INTENT_FILTERED_CHANNELS_CHANGED);
            final ArrayList<Channel> filteredChannel = mJsonSerializer.fromJson(stringExtra, new TypeToken<ArrayList<Channel>>() {
            }.getType());
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setChannels(filteredChannel);
                }
            });
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(AtmBranchListFragment.INTENT_FILTERED_CHANNELS_CHANGED));
        getActivity().openOptionsMenu();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    private void setUpClusteringViews(View view) {
        updateClustering();
    }

    void updateClustering() {
        if (mMap == null) {
            return;
        }

        ClusteringSettings clusteringSettings = new ClusteringSettings();
        clusteringSettings.addMarkersDynamically(true);

        mClusterOptionsProvider = new AtmAndBranchClusterOptionsProvider(getResources());
        mClusterOptionsProviderClicked = new AtmAndBranchClusterOptionsProviderPressed(getResources());
        clusteringSettings.clusterOptionsProvider(mClusterOptionsProvider);

        clusteringSettings.clusterSize(CLUSTER_SIZE);
        mMap.setClustering(clusteringSettings);
    }

    /**
     * Focus to my current location with 15f zoom value in 1000ms
     */
    public void focusToMyLocation(Boolean moveCamera) {

        if (moveCamera) {
            CameraPosition newCamPos = new CameraPosition(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()),
                    15f,
                    mMap.getCameraPosition().tilt,
                    mMap.getCameraPosition().bearing);
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCamPos), 1000, null);
        }

        if (mMyLocationMarker != null)
            mMyLocationMarker.animatePosition(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
    }

    /**
     * Set all markers visibility to false except the selected one.
     */
    public void hideOtherLocationMarkers() {
        for (Marker marker : getMapItemHashMap().values()) {
            if (marker != selectedMarker) {
                marker.setVisible(false);
            }
        }
    }

    /**
     * Hides the List Fragment Button, Search Area and Filter Button, when "get direction" is requested for a marker.
     */
    public void hideSearchAndListComponents() {
        if (mContext != null) {
            mContext.getMenuItemListView().setVisible(false);
            mContext.getMenuItemMapView().setIcon(R.drawable.transparent);
            mContext.getMenuItemMapView().setEnabled(false);
        }
    }

    /**
     * Shows the List Fragment Button, Search Area and Filter Button after the direction dialog has been closed.
     * Remove Route Line if possible.
     * Hide Map Item Container
     */
    public void showSearchAndListComponents() {
        if (mRouteLine != null) {
            mRouteLine.remove();
            mRouteDrawn = false;
        }

        hideMapItemContainerFragment();

        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mContext.getMenuItemMapView().setIcon(R.drawable.map_listing_view);
                mContext.getMenuItemMapView().setEnabled(true);
                mContext.switchtoMapFragment();
            }
        });

        setChannels(mapItemHashMap.keySet());
    }


    public void setLocationEnabled(final boolean enabled) {
        if (getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (enabled) {
                        if (rlMapItemContainerFragment.getVisibility() == View.VISIBLE)
                            ivMyLocationButton.setVisibility(View.INVISIBLE);
                        else
                            ivMyLocationButton.setVisibility(View.VISIBLE);
                    } else {
                        ivMyLocationButton.setVisibility(View.INVISIBLE);
                    }
                }
            });

    }


    public void setChannels(Collection<Channel> channels) {
        hideMapItemContainerFragment();
        setMapItems(channels);
    }

    private void setMapItems(Collection<Channel> channels) {
        if (channels == null)
            return;

        mMap.clear();
        createMyLocationMarker(true);
        if (mMyLocationMarker != null) {
            mMyLocationMarker.setVisible(MapController.GetInstance().isLocationServicesEnabled() && !mLocation.isDefault());
        }

        mapItemHashMap = new HashMap<>();
        for (Channel channel : channels) {
            if (channel.getBankChannelType().equals(MapActivity.CHANNELTYPE_ATM)) {
                Marker marker = mMap.addMarker(new IngMarkerOptions(channel, bitmapDescriptorAtm));
                mapItemHashMap.put((Channel) marker.getData(), marker);

            } else if (channel.getBankChannelType().equals(MapActivity.CHANNELTYPE_BRANCH)) {
                Marker marker = mMap.addMarker(new IngMarkerOptions(channel, bitmapDescriptorBranch));
                mapItemHashMap.put((Channel) marker.getData(), marker);
            }
        }
    }

    /**
     * Call Google Api's Get Direction service with origin and destination lat, long parameters.
     */

    public void getDirection() {

        final Marker marker = getSelectedMarker();
        if (!MapController.GetInstance().isLocationServicesEnabled()) {
            mContext.displayLocationGetDirectionWarning();
        } else if (marker != null) {
            final DirectionRequest directionRequest = new DirectionRequest();
            while (mLocation.isDefault()) {
                Location lastBestStaleLocation =
                        MapController.GetInstance().getLastBestStaleLocation();
                if (lastBestStaleLocation != null) {
                    mLocation = MapLocation.newInstance(MapController.GetInstance().getLastBestStaleLocation());
                }
            }

            directionRequest.setOriginLatitude(mLocation.getLatitude());
            directionRequest.setOriginLongitude(mLocation.getLongitude());
            directionRequest.setDestinationLatitude(Double.parseDouble(((Channel) marker.getData()).getLatitude()));
            directionRequest.setDestinationLongitude(Double.parseDouble(((Channel) marker.getData()).getLongitude()));

            try {
                showWaitingDialog();

            } catch (Exception e) {
                dismissWaitingDialog();
            }
        }
    }

    public void drawRoute(ArrayList<Route> routes) {
        if (selectedMarker != null && routes.size() > 0) {

            hideOtherLocationMarkers();
            hideSearchAndListComponents();
            if (mMyLocationMarker == null)
                createMyLocationMarker(true);
            mMyLocationMarker.setVisible(true);
            mMyLocationMarker.setPosition(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
            ArrayList<String> polyMarkerNodes = new ArrayList<>();
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            // fit the user location and target the marker into the mMap
            builder.include(new LatLng(Double.valueOf(((Channel) selectedMarker.getData()).getLatitude()), Double.valueOf(((Channel) selectedMarker.getData()).getLongitude())));
            builder.include(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));

            for (int i = 0; i < routes.get(0).getLegs().get(0).getSteps().size(); i++) {
                ingbank.com.tr.happybanking.map.model.map.LatLng start_location = routes.get(0).getLegs().get(0).getSteps().get(i).getStart_location();
                ingbank.com.tr.happybanking.map.model.map.LatLng end_location = routes.get(0).getLegs().get(0).getSteps().get(i).getEnd_location();
                builder.include(new LatLng(start_location.getLatitude(), start_location.getLongitude()));
                builder.include(new LatLng(end_location.getLatitude(), end_location.getLongitude()));
                polyMarkerNodes.add(routes.get(0).getLegs().get(0).getSteps().get(i).getPolyline().getPoints());

            }

            drawPolyLine(polyMarkerNodes);

            LatLngBounds bounds = builder.build();
            changeCameraBounds(bounds);


            mMapItemContainerFragment.setUpMapItemDirectionContainer(selectedMarker);
            mRouteDrawn = true;
            selectedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.map_placemarker_branch)));
//            selectedMarker = null;

//            mMap.
        }
    }

    public void drawPolyLine(ArrayList<String> points) {
        PolylineOptions options = new PolylineOptions();
        options.color(getResources().getColor(R.color.indigo_blue));
        options.width(16);

        for (int i = 0; i < points.size(); i++) {
            ArrayList<LatLng> temp_points = (ArrayList<LatLng>) PolyUtil.decode(points.get(i));
            for (int i1 = 0; i1 < temp_points.size(); i1++) {
                options.add(temp_points.get(i1));
            }
        }

        if (mRouteLine != null) {
            mRouteLine.remove();
            mRouteDrawn = false;
        }

        mRouteLine = mMap.addPolyline(options);
        mRouteDrawn = true;
    }

    public void changeCameraBounds(LatLngBounds bounds) {
        //Change the padding as per needed
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
        mMap.animateCamera(cu);
    }

    /**
     * Places my location marker to the current location
     *
     * @param isLocationFound is Location Found
     */
    public void createMyLocationMarker(Boolean isLocationFound) {
        mMap.setMyLocationEnabled(false);

        if (!isLocationFound)
            return;

        if (mMyLocationMarker != null) {
            mMyLocationMarker.remove();
            mMyLocationMarker = null;
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.smiley));
        mMyLocationMarker = mMap.addMarker(markerOptions);

        //Prevent myLocationMarker being clustered.
        mMyLocationMarker.setClusterGroup(-1);
    }

    public HashMap<Channel, Marker> getMapItemHashMap() {
        return mapItemHashMap;
    }

    public Marker getSelectedMarker() {
        return selectedMarker;
    }

    public void setSelectedMarker(Marker selectedMarker) {
        if (selectedMarker == null) {
            if (this.selectedMarker != null) {
                if (!this.selectedMarker.isCluster()) {
                    if (((Channel) this.selectedMarker.getData()).getBankChannelType().equals(MapActivity.CHANNELTYPE_ATM)) {
                        this.selectedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.map_placemarker_atm)));
                    } else if (((Channel) this.selectedMarker.getData()).getBankChannelType().equals(MapActivity.CHANNELTYPE_BRANCH)) {
                        this.selectedMarker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.map_placemarker_branch)));
                    }
                } else {
                    if (this.selectedMarker.getMarkers() != null) {
                        ClusterOptions clusterOptions = mClusterOptionsProviderClicked.getClusterOptions(this.selectedMarker.getMarkers());
                        this.selectedMarker.setIcon(clusterOptions.getIcon());

                    }
                }
            }
        }
        this.selectedMarker = selectedMarker;
    }

    public void onLocationChanged(MapLocation location) {
        if (mMyLocationMarker != null)
            mMyLocationMarker.animatePosition(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));

        setLocation(location);
    }

    public void setLocation(MapLocation location) {
        mLocation = location;
//        setLocationEnabled(!mLocation.isDefault());
        arrangeMyLocationMarker();

        if (mLocation.isDefault()) {
            if (!mIsCameraFocusedDefaultLocation)
                focusToMyLocation(true);
            mIsCameraFocusedDefaultLocation = true;
        } else {
            if (selectedMarker == null && selectedCluster == null) {
                if (!mIsCameraFocusedGPSLocation) {
                    focusToMyLocation(true);
                    mIsCameraFocusedGPSLocation = true;
                }
                mIsCameraFocusedDefaultLocation = true;
            }
        }
    }

    private void arrangeMyLocationMarker() {
        if (mMyLocationMarker == null) {
            if (mLocation != null)
                createMyLocationMarker(true);
        }

        if (mMyLocationMarker != null) {
            mMyLocationMarker.setVisible(MapController.GetInstance().isLocationServicesEnabled() && !mLocation.isDefault());
        }


    }

}