package ingbank.com.tr.happybanking.common.request.map;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import ingbank.com.tr.happybanking.common.request.map.response.CompositionResponse;

/**
 * Created by mustafa on 1.04.2015.
 */
public class SaveLocationRequest extends CompositionRequest {
    @SerializedName("Latitude")
    private String mLatitude;
    @SerializedName("Longitude")
    private String mLongitude;


    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }

    @Override
    public Type getResponseType() {
        return new TypeToken<CompositionResponse>() {
        }.getType();
    }
}