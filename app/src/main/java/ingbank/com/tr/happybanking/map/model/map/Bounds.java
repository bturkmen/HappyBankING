package ingbank.com.tr.happybanking.map.model.map;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 7/1/14.
 * Copyright - Valensas 2014
 *
 * @author Furkan BAYRAKTAR
 */
public class Bounds {

    @SerializedName("northeast")
    private LatLng mNortheast;

    @SerializedName("southwest")
    private LatLng mSouthwest;

    public LatLng getNortheast() {
        return mNortheast;
    }

    public void setNortheast(LatLng mNortheast) {
        this.mNortheast = mNortheast;
    }

    public LatLng getSouthwest() {
        return mSouthwest;
    }

    public void setSouthwest(LatLng mSouthwest) {
        this.mSouthwest = mSouthwest;
    }
}
