package ingbank.com.tr.happybanking.map.listener.map;

import java.util.ArrayList;

import ingbank.com.tr.happybanking.map.model.map.Channel;

public interface onListAtmBranchListener extends OnInteractorListener {

    void onListAtmBranch(ArrayList<Channel> channelList);
}
