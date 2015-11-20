package ingbank.com.tr.happybanking.map.fragment;


import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidmapsextensions.Marker;


import java.util.List;
import java.util.Vector;

import ingbank.com.tr.happybanking.MapItemDetailFragment;
import ingbank.com.tr.happybanking.R;
import ingbank.com.tr.happybanking.common.ui.controls.SquarePageIndicator;
import ingbank.com.tr.happybanking.common.util.FontHelper;
import ingbank.com.tr.happybanking.map.MapActivity;
import ingbank.com.tr.happybanking.map.controller.MapController;
import ingbank.com.tr.happybanking.map.model.map.Channel;
import ingbank.com.tr.happybanking.ui.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapItemContainerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class MapItemContainerFragment extends BaseFragment {
    List<Fragment> fragments;
    List<Marker> markers;
    private View myView;
    private ViewPagerStateAdapter<Fragment> mViewPagerAdapter;
    private ViewPager mViewPager;
    private TextView mTxtDirection;
    private LinearLayout llIndicator, llDirection;
    private float scale;

    public MapItemContainerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public void setUpMapItemContainer(List<Marker> markerArrayList) {
        fragments = new Vector<>();
        mViewPagerAdapter.setItems(fragments);
        mViewPagerAdapter.notifyDataSetChanged();
        this.markers = markerArrayList;
        if (markers != null && markers.size() > 0)
            ((AtmBranchMapFragment) getParentFragment()).setSelectedMarker(markers.get(0));

        for (Marker marker : markerArrayList) {
            Channel branch = marker.getData();
            fragments.add(MapItemDetailFragment.newInstance(branch));
        }


        llIndicator.setVisibility(View.VISIBLE);
        llDirection.setVisibility(View.VISIBLE);
        mViewPagerAdapter.setItems(fragments);
        mViewPager.setCurrentItem(0);
        mViewPager.invalidate();
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public void setUpMapItemContainer(List<Marker> markerArrayList, Marker selected) {
        fragments = new Vector<>();
        mViewPagerAdapter.setItems(fragments);
        mViewPagerAdapter.notifyDataSetChanged();
        this.markers = markerArrayList;
        if (markers != null && markers.size() > 0)
            ((AtmBranchMapFragment) getParentFragment()).setSelectedMarker(markers.get(0));

        Channel selectedChannel = selected.getData();
        int foundIndex = 0;
        for (Marker marker : markerArrayList) {
            Channel branch = marker.getData();
            if (selectedChannel.equals(branch)) {
                foundIndex = markerArrayList.indexOf(marker);
            }
            fragments.add(MapItemDetailFragment.newInstance(branch));
        }


        llIndicator.setVisibility(View.VISIBLE);
        llDirection.setVisibility(View.VISIBLE);
        mViewPagerAdapter.setItems(fragments);
        mViewPager.setCurrentItem(foundIndex);
        mViewPager.invalidate();
        SquarePageIndicator pageIndicator = (SquarePageIndicator) myView.findViewById(R.id.pageIndicator);
        pageIndicator.setVisibility(fragments.size() > 1 ? View.VISIBLE : View.INVISIBLE);
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public void setUpMapItemDirectionContainer(Marker marker) {
        fragments = new Vector<>();


        Channel data = (Channel) marker.getData();
        if (data.getDistanceToPoint() == Double.MIN_VALUE) {

            Location lastBestStaleLocation = MapController.GetInstance().getLastBestStaleLocation();

            if (lastBestStaleLocation != null) {
                android.location.Location destination = new android.location.Location("");

                try {
                    destination.setLatitude(Double.parseDouble(data.getLatitude().trim()));
                    destination.setLongitude(Double.parseDouble(data.getLongitude().trim()));
                    double distance = ((double) destination.distanceTo(lastBestStaleLocation));

                    data.setDistanceToPoint(distance);
                } catch (Exception ex) {
                }
            }
        }
        if (((Channel) marker.getData()).getBankChannelType().equals(MapActivity.CHANNELTYPE_ATM)) {
            fragments.add(MapItemDirectionFragment.newInstance(R.drawable.map_atm_address, data.getName(), data.getDistanceToPointAsString(), data.getCounty() + ", " + data.getCity()));
        } else if (((Channel) marker.getData()).getBankChannelType().equals(MapActivity.CHANNELTYPE_BRANCH)) {
            fragments.add(MapItemDirectionFragment.newInstance(R.drawable.map_branch_address, data.getName(), data.getDistanceToPointAsString(), data.getCounty() + ", " + data.getCity()));
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.height = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 105f, getResources().getDisplayMetrics()));

        mViewPager.setLayoutParams(layoutParams);

        llIndicator.setVisibility(View.GONE);
        llDirection.setVisibility(View.GONE);
        mViewPagerAdapter.setItems(fragments);
        mViewPager.setCurrentItem(0);
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_map_item_container;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        scale = getResources().getDisplayMetrics().density;

        myView = super.onCreateView(inflater, container, savedInstanceState);
            mTxtDirection = (TextView) myView.findViewById(R.id.txtDirection);
            mTxtDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((AtmBranchMapFragment) getParentFragment()).getDirection();
                }
            });


            mViewPagerAdapter = new ViewPagerStateAdapter<>(getChildFragmentManager());
            mViewPager = (ViewPager) myView.findViewById(R.id.viewPager);
            llDirection = (LinearLayout) myView.findViewById(R.id.llDirection);
            llIndicator = (LinearLayout) myView.findViewById(R.id.llIndicator);
            mViewPager.setAdapter(mViewPagerAdapter);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    ((AtmBranchMapFragment) getParentFragment()).setSelectedMarker(markers.get(position));
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            SquarePageIndicator pageIndicator = (SquarePageIndicator) myView.findViewById(R.id.pageIndicator);
            pageIndicator.setVisibility(View.VISIBLE);
            pageIndicator.setStrokeColor(getResources().getColor(R.color.passive_indicator));
            pageIndicator.setFillColor(getResources().getColor(R.color.ing_orange));
            pageIndicator.setPageColor(getResources().getColor(R.color.passive_indicator));
            pageIndicator.setSnap(true);
            pageIndicator.setRadius(getResources().getDimension(R.dimen.space_between_indicator));
            pageIndicator.setViewPager(mViewPager);
            pageIndicator.refreshDrawableState();
            FontHelper.setAppFont((ViewGroup) myView, true);
            myView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });

        return myView;
    }

    public void setSelectedMarker(Marker selectedMarker) {
        if (selectedMarker == null)
            return;
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPagerAdapter.notifyDataSetChanged();
        mViewPager.invalidate();
        Channel selectedChannel = selectedMarker.getData();
        ((AtmBranchMapFragment) getParentFragment()).setSelectedMarker(selectedMarker);
        int count = mViewPagerAdapter.getCount();
        for (int i = 0; i < count; i++) {
            MapItemDetailFragment item = (MapItemDetailFragment) mViewPagerAdapter.getItem(i);
            if (item.getChannel().equals(selectedChannel)) {
                mViewPager.setCurrentItem(i, true);
                return;
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }
}
