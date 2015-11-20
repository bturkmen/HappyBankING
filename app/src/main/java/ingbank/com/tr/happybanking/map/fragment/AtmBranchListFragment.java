package ingbank.com.tr.happybanking.map.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ListView;

import com.androidmapsextensions.Marker;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;


import java.util.ArrayList;

import ingbank.com.tr.happybanking.R;
import ingbank.com.tr.happybanking.common.ListItem;
import ingbank.com.tr.happybanking.map.MapActivity;
import ingbank.com.tr.happybanking.map.adapter.AtmBranchListAdapter;
import ingbank.com.tr.happybanking.map.controller.MapLocation;
import ingbank.com.tr.happybanking.map.model.map.Channel;
import ingbank.com.tr.happybanking.ui.BaseFragment;

public class AtmBranchListFragment extends BaseFragment {

    public final static String INTENT_FILTERED_CHANNELS_CHANGED = "INTENT_FILTERED_CHANNELS_CHANGED";
    public final static String INTENT_FILTERED_CHANNELS_CHANGED_CHANNELS = "INTENT_FILTERED_CHANNELS_CHANGED";
    private ListView mListView;
    private AtmBranchListAdapter mAdapter;
    private View mRLLoading;
    private Gson mJsonSerializer;
    private AdapterView.OnItemClickListener onItemClickListener;

    @Override
    public int getContentView() {
        return R.layout.fragment_map_list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mJsonSerializer = new Gson();
        mAdapter = new AtmBranchListAdapter(getActivity(), null);
        onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MapActivity activity = (MapActivity) getActivity();
                if (activity == null)
                    return;
                if (activity.getmMapFragment() != null && activity.getmMapFragment().getMapItemHashMap() != null) {
                    ListItem listItem = (ListItem) mListView.getAdapter().getItem(position);
                    Channel channel = listItem.getChannel();
                    if (channel == null)
                        return;

                    String name = channel.getChannelId();
                    if (name.contains("region")) {
                        LatLngBounds bounds = listItem.getBounds();
                        activity.getmMapFragment().focusToSelectedPosition(bounds);
                    } else {
                        Marker marker = activity.getmMapFragment().getMapItemHashMap().get(channel);
                        if (marker == null)
                            return;
                        activity.getmMapFragment().focusToSelectedMarker(marker);
                    }


                    activity.switchtoMapFragment();
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.dispose();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
            mListView = (ListView) view.findViewById(R.id.lvChannel);
            mListView.setAdapter(mAdapter);
            mRLLoading = view.findViewById(R.id.rlLoading);
            mListView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if (visibility == View.VISIBLE) {
                        mRLLoading.setVisibility(View.GONE);
                    } else {
                        mRLLoading.setVisibility(View.VISIBLE);
                    }
                }
            });

        return view;
    }

    public void setChannels(ArrayList<Channel> channels) {
        mAdapter.setChannels(channels);
        mListView.setOnItemClickListener(onItemClickListener);
    }

    public AtmBranchListAdapter getAdapter() {
        return mAdapter;
    }

    public void filter(String text) {
        mListView.setVisibility(View.GONE);
        this.mAdapter.getFilter().filter(text, new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int count) {
                mListView.setVisibility(View.VISIBLE);
            }
        });
    }

    public void filterByCriteria(String text) {
        mListView.setVisibility(View.GONE);
        this.mAdapter.getFilter().filter(text, new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int count) {
                if (count == 0)
                    createAlertDialog(getString(R.string.global_warning), getString(R.string.error24), "Tamam", null, null, null, true).show();

                mListView.setVisibility(View.VISIBLE);
                Intent intent = new Intent(INTENT_FILTERED_CHANNELS_CHANGED);
                intent.setAction(INTENT_FILTERED_CHANNELS_CHANGED);
                intent.putExtra(INTENT_FILTERED_CHANNELS_CHANGED_CHANNELS, mJsonSerializer.toJson(mAdapter.getChannels()));
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
            }
        });
    }

    public void setLocation(MapLocation location) {
        this.mAdapter.setLocation(location);
        mListView.setOnItemClickListener(onItemClickListener);
    }
}
