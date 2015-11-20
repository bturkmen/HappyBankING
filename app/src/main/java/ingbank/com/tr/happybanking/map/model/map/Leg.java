package ingbank.com.tr.happybanking.map.model.map;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created on 7/1/14.
 * Copyright - Valensas 2014
 *
 * @author Furkan BAYRAKTAR
 */
public class Leg {

    @SerializedName("distance")
    private KeyValue mDistance;
    @SerializedName("duration")
    private KeyValue mDuration;
    @SerializedName("end_address")
    private String mEnd_address;
    @SerializedName("end_location")
    private LatLng mEnd_location;
    @SerializedName("start_address")
    private String mStart_address;
    @SerializedName("start_location")
    private LatLng mStart_location;
    @SerializedName("steps")
    private ArrayList<Step> mSteps;

    public KeyValue getDistance() {
        return mDistance;
    }

    public void setDistance(KeyValue distance) {
        this.mDistance = distance;
    }

    public KeyValue getDuration() {
        return mDuration;
    }

    public void setDuration(KeyValue duration) {
        this.mDuration = duration;
    }

    public String getEnd_address() {
        return mEnd_address;
    }

    public void setEnd_address(String end_address) {
        this.mEnd_address = end_address;
    }

    public LatLng getEnd_location() {
        return mEnd_location;
    }

    public void setEnd_location(LatLng end_location) {
        this.mEnd_location = end_location;
    }

    public String getStart_address() {
        return mStart_address;
    }

    public void setStart_address(String start_address) {
        this.mStart_address = start_address;
    }

    public LatLng getStart_location() {
        return mStart_location;
    }

    public void setStart_location(LatLng start_location) {
        this.mStart_location = start_location;
    }

    public ArrayList<Step> getSteps() {
        return mSteps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.mSteps = steps;
    }
}
