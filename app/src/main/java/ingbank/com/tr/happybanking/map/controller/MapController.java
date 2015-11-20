package ingbank.com.tr.happybanking.map.controller;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;


public class MapController {

    public MapControllerListener getListener() {
        return mListener;
    }

    public void setListener(MapControllerListener listener) {
        this.mListener = listener;
    }

    private GoogleApiClient.ConnectionCallbacks mConnectionCallbacks;

    private GoogleApiClient.OnConnectionFailedListener mOnConnectionFailedListener;

    private LocationListener mLocationListener;

    private LocationRequest mLocationRequest;

    private Context mContext;

    private LocationManager mLocationManager;

    private static MapController MapController;

    private android.location.LocationListener mLocationStatusListener;

    private MapControllerListener mListener;


    private boolean mIsConnectedToGoogleServices, mIsStartedToListenLocationUpdate;

    private MapController() {
        mIsConnectedToGoogleServices = false;
        mIsStartedToListenLocationUpdate = false;

        mConnectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                mIsConnectedToGoogleServices = true;
                startLocationUpdates();
                boolean locationServicesEnabled = isLocationServicesEnabled();
                if (mListener != null) {
                    mListener.onLocationServiceStateChanged(locationServicesEnabled);
                }

                Location lastLocation = getLastBestStaleLocation();
                if (lastLocation != null) {
                    if (mListener != null) {
                        mListener.onLocationFound(MapLocation.newInstance(lastLocation));
                    }
                } else {
                    if (mListener != null) {
                        mListener.onLocationFound(MapLocation.defaultLocation());
                    }
                }

            }

            @Override
            public void onConnectionSuspended(int i) {
                mIsConnectedToGoogleServices = false;
                mGoogleApiClient.connect();
            }
        };

        mOnConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {
                mIsConnectedToGoogleServices = false;
            }
        };

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (mListener != null)
                    mListener.onLocationChanged(MapLocation.newInstance(location));
            }
        };


        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(2);
    }

    public synchronized static MapController GetInstance() {
        if (MapController == null)
            MapController = new MapController();
        return MapController;
    }

    private GoogleApiClient mGoogleApiClient;

    public void onCreate(Context context) {
        if (context == null)
            return;
        mContext = context;
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(mConnectionCallbacks)
                .addOnConnectionFailedListener(mOnConnectionFailedListener)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mLocationStatusListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mListener.onLocationChanged(MapLocation.newInstance(location));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                if (mListener != null)
                    mListener.onLocationServiceStateChanged(isLocationServicesEnabled());

                Location lastLocation = getLastBestStaleLocation();

//                Toast.makeText(mContext, "onProviderEnabled:" + (lastLocation == null), Toast.LENGTH_SHORT).show();
                if (lastLocation != null) {
                    if (mListener != null) {
                        mListener.onLocationFound(MapLocation.newInstance(lastLocation));
                    }
                } else {
                    if (mListener != null) {
                        mListener.onLocationFound(MapLocation.defaultLocation());
                    }
                }
            }


            @Override
            public void onProviderDisabled(String provider) {
                if (mListener != null)
                    mListener.onLocationServiceStateChanged(isLocationServicesEnabled());
//                Toast.makeText(mContext, "onProviderDisabled", Toast.LENGTH_SHORT).show();

                if (mListener != null) {
                    mListener.onLocationFound(MapLocation.defaultLocation());
                }
            }
        };

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 35000, 10, mLocationStatusListener);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 35000, 10, mLocationStatusListener);
        mLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 35000, 10, mLocationStatusListener);

    }

    public void onResume() {
        Log.d("Status: ", "onResume");
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }


    private Location getLastKnownLocation() {
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    public Location getLastBestStaleLocation() {

        Location lastFusedLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Location bestResult = getLastKnownLocation();

        if (bestResult == null) {
            bestResult = lastFusedLocation;
        }
        //take Fused Location in to consideration while checking for last stale location
        if (bestResult != null && lastFusedLocation != null) {
            if (bestResult.getTime() < lastFusedLocation.getTime())
                bestResult = lastFusedLocation;
        }

        return bestResult;
    }

    public void onStart() {
        if (mIsConnectedToGoogleServices && !mIsStartedToListenLocationUpdate) {
            startLocationUpdates();
            mIsStartedToListenLocationUpdate = true;
        }

    }

    public void onPause() {
        Log.d("Status: ", "onPause");
        if (mIsConnectedToGoogleServices && mIsStartedToListenLocationUpdate) {
            stopLocationUpdates();
        }

        mIsStartedToListenLocationUpdate = false;

    }

    public void onDestroy() {
        if (mIsConnectedToGoogleServices) {
            mGoogleApiClient.disconnect();
        }
        mIsConnectedToGoogleServices = false;
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, mLocationListener);
    }

    private void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, mLocationListener);

    }

    public boolean isLocationServicesEnabled() {
        LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled;
        boolean isNetworkProviderEnabled;
        boolean isPassiveProviderEnabled;

        try {
            isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSEnabled)
                return true;
        } catch (Exception ex) {
        }

//        try {
//            isNetworkProviderEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//            if (isNetworkProviderEnabled)
//                return true;
//        } catch (Exception ex) {
//        }
//
//        try {
//            isPassiveProviderEnabled = lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
//            if (isPassiveProviderEnabled)
//                return true;
//        } catch (Exception ex) {
//        }


        return false;
    }
}
