package ingbank.com.tr.happybanking.map.adapter;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Pair;
import android.widget.Filter;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;

import ingbank.com.tr.happybanking.map.model.map.Channel;


public class AtmBranchListFilter extends Filter {

    public static final int BRANCH_SELECTED = 0x01;
    public static final int ATM_SELECTED = 0x02;
    public static final int SPECIAL_SELECTED = 0x04;
    public static final int CONCEPT_SELECTED = 0x08;
    public static final int DISABLED_SELECTED = 0x10;


    private AtmBranchListAdapter mAdapter;
    private AtmBranchDbHelper mDbAdapter;
    private ArrayList<Channel> mOriginalListItems;

    public AtmBranchListFilter(AtmBranchListAdapter adapter) {
        this.mAdapter = adapter;
        this.mOriginalListItems = this.mAdapter.getChannels();
        this.mDbAdapter = new AtmBranchDbHelper(adapter.getContext());
        this.mDbAdapter.open();
        this.mDbAdapter.deleteAllChannelAtms();
        if (this.mOriginalListItems != null)
            this.mDbAdapter.createAtmBranch(mOriginalListItems);
    }

    public void setItems(ArrayList<Channel> items) {
        this.mOriginalListItems = items;

        this.mDbAdapter.deleteAllChannelAtms();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mOriginalListItems != null)
                    mDbAdapter.createAtmBranch(mOriginalListItems);
            }
        }).start();
    }


    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        try {
            Pair<HashSet<Channel>, HashSet<Channel>> filteredResults = this.getFilteredResults(constraint);
            FilterResults results = new FilterResults();
            results.values = filteredResults;
            results.count = filteredResults.first.size() + filteredResults.second.size();
            return results;
        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }
    }

    private Pair<HashSet<Channel>, HashSet<Channel>> getFilteredResults(CharSequence constraint) {
        HashSet<Channel> resultRegion = new HashSet<>();
        HashSet<Channel> result = new HashSet<>();
        Gson gson = new Gson();
        String[] split = constraint.toString().split(",");
        String searchText = split[0].replace(" ", "");
        if (!TextUtils.isEmpty(searchText)) {
            Cursor cursorRegion = this.mDbAdapter.searchRegionByCity(split[0], Integer.parseInt(split[1]));
            if (cursorRegion.getCount() != 0) {

                do {
                    String jsonObj = cursorRegion.getString(cursorRegion.getColumnIndexOrThrow(AtmBranchDbHelper.KEY_OBJECT));
                    Channel channel = gson.fromJson(jsonObj, Channel.class);
                    result.add(new Channel(channel));
                    channel.setName(channel.getCity());
                    resultRegion.add(channel);
                } while (cursorRegion.moveToNext());
            }

            Cursor cursorRegionCounty = this.mDbAdapter.searchRegionByCounty(split[0], Integer.parseInt(split[1]));
            if (cursorRegionCounty.getCount() != 0) {

                do {
                    String jsonObj = cursorRegionCounty.getString(cursorRegion.getColumnIndexOrThrow(AtmBranchDbHelper.KEY_OBJECT));
                    Channel channel = gson.fromJson(jsonObj, Channel.class);
                    result.add(new Channel(channel));
                    channel.setName(channel.getCounty() + " / " + channel.getCity());
                    resultRegion.add(channel);

                } while (cursorRegionCounty.moveToNext());
            }

        }

        Cursor cursor = this.mDbAdapter.searchChannelAtm(split[0], Integer.parseInt(split[1]));
        if (cursor.getCount() != 0) {
            do {
                String jsonObj = cursor.getString(cursor.getColumnIndexOrThrow(AtmBranchDbHelper.KEY_OBJECT));
                result.add(gson.fromJson(jsonObj, Channel.class));

            } while (cursor.moveToNext());
        }


        return new Pair<>(resultRegion, result);
    }

    private boolean searchChannelByRegion(HashSet<Channel> resultRegion, Channel channel) {
        boolean found = false;

        for (Channel item : resultRegion) {
            if (item.getCityCode() == channel.getCityCode() || item.getCountyCode() == channel.getCountyCode()) {
                found = true;
                break;
            }
        }
        return found;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        if (results == null || results.values == null)
            mAdapter.setItems(new HashSet<>(this.mOriginalListItems));
        else {
            Pair<HashSet<Channel>, HashSet<Channel>> vals = (Pair<HashSet<Channel>, HashSet<Channel>>) results.values;
            if (vals.first.size() == 0)
                mAdapter.setItems(vals.second);
            else
                mAdapter.setItems(vals.first, vals.second);

        }
    }


    public void dispose() {
        this.mDbAdapter.deleteAllChannelAtms();
        this.mDbAdapter.close();
    }

}
