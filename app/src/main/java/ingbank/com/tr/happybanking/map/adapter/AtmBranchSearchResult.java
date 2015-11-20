package ingbank.com.tr.happybanking.map.adapter;


import java.util.ArrayList;
import java.util.List;

import ingbank.com.tr.happybanking.common.ListItem;

public class AtmBranchSearchResult {

    public AtmBranchSearchResult() {
        this.Regions = new ArrayList<>();
        this.Atms = new ArrayList<>();
    }

    public List<ListItem> Regions;

    public List<ListItem> Atms;
}
