package ingbank.com.tr.happybanking.common.request.map;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 7/1/14.
 * Copyright - Valensas 2014
 *
 * @author Furkan BAYRAKTAR
 */
public class DirectionRequest {

    private double mOriginLatitude;
    private double mOriginLongitude;
    private double mDestinationLatitude;
    private double mDestinationLongitude;
    @SerializedName("origin")
    private String mOrigin;

    public double getOriginLatitude() {
        return mOriginLatitude;
    }

    public void setOriginLatitude(double mOriginLatitude) {
        this.mOriginLatitude = mOriginLatitude;
    }

    public double getOriginLongitude() {
        return mOriginLongitude;
    }

    public void setOriginLongitude(double mOriginLongitude) {
        this.mOriginLongitude = mOriginLongitude;
    }

    public double getDestinationLatitude() {
        return mDestinationLatitude;
    }

    public void setDestinationLatitude(double mDestinationLatitude) {
        this.mDestinationLatitude = mDestinationLatitude;
    }

    public double getDestinationLongitude() {
        return mDestinationLongitude;
    }

    public void setDestinationLongitude(double mDestinationLongitude) {
        this.mDestinationLongitude = mDestinationLongitude;
    }
}
