package ingbank.com.tr.happybanking.map.model.map;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListAtmBranchResponse {
    @SerializedName("AtmBranchList")
    private ArrayList<Channel> mChannelList;

    public ArrayList<Channel> getChannelList() {
        return mChannelList;
    }

    public void setChannelList(ArrayList<Channel> mChannelList) {
        this.mChannelList = mChannelList;
    }
}
