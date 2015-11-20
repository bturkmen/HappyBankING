package ingbank.com.tr.happybanking.map.model.map;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 7/1/14.
 * Copyright - Valensas 2014
 *
 * @author Furkan BAYRAKTAR
 */
public class Step {
    @SerializedName("distance")
    private KeyValue mDistance;
    @SerializedName("duration")
    private KeyValue mDuration;
    @SerializedName("end_location")
    private LatLng mEnd_location;
    @SerializedName("html_instructions")
    private String mHtml_instructions;
    @SerializedName("polyline")
    private PolyLine mPolyline;
    @SerializedName("start_location")
    private LatLng mStart_location;
    @SerializedName("travel_mode")
    private String mTravel_mode;

    public KeyValue getDistance() {
        return mDistance;
    }

    public void setDistance(KeyValue mDistance) {
        this.mDistance = mDistance;
    }

    public KeyValue getDuration() {
        return mDuration;
    }

    public void setDuration(KeyValue mDuration) {
        this.mDuration = mDuration;
    }

    public LatLng getEnd_location() {
        return mEnd_location;
    }

    public void setEnd_location(LatLng mEnd_location) {
        this.mEnd_location = mEnd_location;
    }

    public String getHtml_instructions() {
        return mHtml_instructions;
    }

    public void setHtml_instructions(String mHtml_instructions) {
        this.mHtml_instructions = mHtml_instructions;
    }

    public PolyLine getPolyline() {
        return mPolyline;
    }

    public void setPolyline(PolyLine mPolyline) {
        this.mPolyline = mPolyline;
    }

    public LatLng getStart_location() {
        return mStart_location;
    }

    public void setStart_location(LatLng mStart_location) {
        this.mStart_location = mStart_location;
    }

    public String getTravel_mode() {
        return mTravel_mode;
    }

    public void setTravel_mode(String mTravel_mode) {
        this.mTravel_mode = mTravel_mode;
    }
}
