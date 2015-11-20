package ingbank.com.tr.happybanking.map.model.map;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 7/1/14.
 * Copyright - Valensas 2014
 *
 * @author Furkan BAYRAKTAR
 */
public class PolyLine {

    @SerializedName("points")
    private String mPoints;

    public String getPoints() {
        return mPoints;
    }

    public void setPoints(String mPoints) {
        this.mPoints = mPoints;
    }
}
