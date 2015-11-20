package ingbank.com.tr.happybanking.map.controller;

import android.location.Location;

public class MapLocation extends android.location.Location {

    public static final double DEFAULT_CENTAL_LOCATION_LAT = 41.108416;
    public static final double DEFAULT_CENTAL_LOCATION_LONG = 29.016686;
    private static MapLocation mDefaultLocation = null;

    public MapLocation(String provider) {
        super(provider);
    }

    public MapLocation(android.location.Location l) {
        super(l);
    }

    public boolean isDefault() {
        return getLatitude() == DEFAULT_CENTAL_LOCATION_LAT && getLongitude() == DEFAULT_CENTAL_LOCATION_LONG;
    }

    public static MapLocation newInstance(Location location) {
        return new MapLocation(location);
    }

    public static synchronized MapLocation defaultLocation() {
        if (mDefaultLocation == null) {
            mDefaultLocation = new MapLocation("");
            mDefaultLocation.setLongitude(DEFAULT_CENTAL_LOCATION_LONG);
            mDefaultLocation.setLatitude(DEFAULT_CENTAL_LOCATION_LAT);
        }

        return mDefaultLocation;
    }
}