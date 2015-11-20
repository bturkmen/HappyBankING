package ingbank.com.tr.happybanking.map.model.map;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created on 6/25/14.
 * Copyright - Valensas 2014
 *
 * @author Furkan BAYRAKTAR
 */
public class Location implements Serializable {

    @SerializedName("Address")
    private String mAddress;
    @SerializedName("Latitude")
    private double mLatitude;
    @SerializedName("Longitude")
    private double mLongitude;
    @SerializedName("DistanceToPoint")
    private double mDistanceToPoint;

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

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

    public double getDistanceToPoint() {
        return mDistanceToPoint;
    }

    public void setDistanceToPoint(double mDistanceToPoint) {
        this.mDistanceToPoint = mDistanceToPoint;
    }
}
