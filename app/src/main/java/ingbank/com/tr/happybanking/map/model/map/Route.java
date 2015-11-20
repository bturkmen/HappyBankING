package ingbank.com.tr.happybanking.map.model.map;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created on 7/1/14.
 * Copyright - Valensas 2014
 *
 * @author Furkan BAYRAKTAR
 */
public class Route {

    @SerializedName("bounds")
    private Bounds mBounds;

    @SerializedName("copyrights")
    private String mCopyrights;

    @SerializedName("legs")
    private ArrayList<Leg> mLegs;

    @SerializedName("overview_polyline")
    private PolyLine mOverview_polyline;

    @SerializedName("summary")
    private String mSummary;

    public Bounds getBounds() {
        return mBounds;
    }

    public void setBounds(Bounds mBounds) {
        this.mBounds = mBounds;
    }

    public String getCopyrights() {
        return mCopyrights;
    }

    public void setCopyrights(String mCopyrights) {
        this.mCopyrights = mCopyrights;
    }

    public ArrayList<Leg> getLegs() {
        return mLegs;
    }

    public void setLegs(ArrayList<Leg> mLegs) {
        this.mLegs = mLegs;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        this.mSummary = summary;
    }

    public PolyLine getOverview_polyline() {
        return mOverview_polyline;
    }

    public void setOverview_polyline(PolyLine overview_polyline) {
        this.mOverview_polyline = overview_polyline;
    }
}
