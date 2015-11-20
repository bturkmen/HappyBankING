package ingbank.com.tr.happybanking.map.model.map;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 7/1/14.
 * Copyright - Valensas 2014
 *
 * @author Furkan BAYRAKTAR
 */
public class LatLng {

    @SerializedName("lat")
    private double mLatitude;
    @SerializedName("lng")
    private double mLongitude;

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }
}
