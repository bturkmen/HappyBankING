package ingbank.com.tr.happybanking.map;



import java.util.ArrayList;

import ingbank.com.tr.happybanking.map.model.map.Channel;

public interface MapView extends BaseView {
    void onListAtmBranch(ArrayList<Channel> atmBranchList);

    void showError(String errorMessage);
}
