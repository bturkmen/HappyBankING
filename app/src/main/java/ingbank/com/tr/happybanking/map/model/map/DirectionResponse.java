package ingbank.com.tr.happybanking.map.model.map;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DirectionResponse{
    @SerializedName("routes")
    private ArrayList<Route> mRoutes;
    @SerializedName("status")
    private String mStatus;

    public ArrayList<Route> getRoutes() {
        return mRoutes;
    }

    public void setRoutes(ArrayList<Route> mRoutes) {
        this.mRoutes = mRoutes;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }
}
