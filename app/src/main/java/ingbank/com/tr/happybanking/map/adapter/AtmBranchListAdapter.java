package ingbank.com.tr.happybanking.map.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import ingbank.com.tr.happybanking.R;
import ingbank.com.tr.happybanking.common.ListItem;
import ingbank.com.tr.happybanking.map.controller.MapLocation;
import ingbank.com.tr.happybanking.map.model.map.Channel;

public class AtmBranchListAdapter extends BaseAdapter implements Filterable {
    //
    public static final String TAG = "AtmBranchListAdapter";
    private Context mContext;
    private ArrayList<ListItem> mItems;
    private AtmBranchListFilter filter;
    private ArrayList<Channel> mChannels;
    private MapLocation mLocation;


    public AtmBranchListAdapter(Context context, ArrayList<Channel> itemsArrayList) {
        super();
        this.mContext = context;
        this.mItems = new ArrayList<>();
        this.mChannels = itemsArrayList;
        this.filter = new AtmBranchListFilter(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View rowView = convertView;
        final ListItem listItem = mItems.get(position);

        if (convertView == null) {
            // 1. Create inflater
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 2. Get rowView from inflater
            rowView = inflater.inflate(R.layout.map_list_row, parent, false);

            // 3. Get the text views from the rowView
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) rowView.findViewById(R.id.txtListItemName);
            viewHolder.address = (TextView) rowView.findViewById(R.id.txtListItemAddress);
            viewHolder.distance = (TextView) rowView.findViewById(R.id.txtListItemDistance);
            viewHolder.icon = (ImageButton) rowView.findViewById(R.id.imgListItemImage);
            rowView.setTag(viewHolder);
        } else {
            //reuse previously created view
            viewHolder = (ViewHolder) rowView.getTag();
        }

        // 4. Set the text for textView
        viewHolder.name.setText(listItem.getName());
        viewHolder.address.setText(listItem.getAddress());
        viewHolder.distance.setText(listItem.getDistance());
        viewHolder.icon.setImageResource(listItem.getImage());

        switch (listItem.getType()) {
            case ListItem.ITEMTYPE_HEADER:
                viewHolder.name.setVisibility(View.VISIBLE);
                viewHolder.address.setVisibility(View.GONE);
                viewHolder.distance.setVisibility(View.GONE);
                viewHolder.icon.setVisibility(View.GONE);
                viewHolder.name.setTextColor(mContext.getResources().getColor(R.color.ing_orange));
                viewHolder.name.setTag("light");
                rowView.setBackgroundColor(0xfff1eee6);
                break;

            case ListItem.ITEMTYPE_REGION:
                viewHolder.name.setVisibility(View.VISIBLE);
                viewHolder.address.setVisibility(View.GONE);
                viewHolder.distance.setVisibility(View.GONE);
                viewHolder.icon.setVisibility(View.VISIBLE);
                viewHolder.name.setTextColor(mContext.getResources().getColor(R.color.dark_gray));
                viewHolder.name.setTag(null);
                rowView.setBackgroundColor(Color.TRANSPARENT);
                break;

            default:
                viewHolder.name.setVisibility(View.VISIBLE);
                viewHolder.address.setVisibility(View.VISIBLE);
                if (mLocation == null || mLocation.isDefault())
                    viewHolder.distance.setVisibility(View.INVISIBLE);
                else
                    viewHolder.distance.setVisibility(View.VISIBLE);
                viewHolder.icon.setVisibility(View.VISIBLE);
                viewHolder.name.setTextColor(mContext.getResources().getColor(R.color.dark_gray));
                viewHolder.name.setTag(null);
                rowView.setBackgroundColor(Color.TRANSPARENT);
                break;
        }

        // 5. return rowView
        return rowView;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.mItems.get(position).hashCode();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public void setChannels(ArrayList<Channel> channels) {
        this.mChannels = channels;
        this.filter.setItems(channels);
        setItems(new HashSet<>(channels));
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public ArrayList<Channel> getChannels() {
        return mChannels;
    }

    public void setLocation(MapLocation location) {
        this.mLocation = location;
        notifyDataSetChanged();

        if (!mLocation.isDefault()) {
            android.location.Location destination = new android.location.Location("");
            for (ListItem item : mItems) {
                try {
                    destination.setLatitude(Double.parseDouble(item.getChannel().getLatitude().trim()));
                    destination.setLongitude(Double.parseDouble(item.getChannel().getLongitude().trim()));
                    double distance = ((double) destination.distanceTo(mLocation));
                    item.getChannel().setDistanceToPoint(distance);
                } catch (Exception ignored) {
                }
            }
        }

        if (!mLocation.isDefault()) {
            orderByDistance(mItems);
        } else {
            orderByName(mItems);
        }
        notifyDataSetInvalidated();
    }

    public void dispose() {
        filter.dispose();
    }

    private static class ViewHolder {
        TextView name;
        TextView address;
        TextView distance;
        ImageButton icon;
    }


    public void setItems(HashSet<Channel> mItems) {
        notifyDataSetChanged();
        this.mChannels = new ArrayList<>(mItems);
        this.mItems.clear();
        for (Channel channel : mItems) {

            if (mLocation != null && !mLocation.isDefault()) {
                android.location.Location destination = new android.location.Location("");

                try {
                    destination.setLatitude(Double.parseDouble(channel.getLatitude().trim()));
                    destination.setLongitude(Double.parseDouble(channel.getLongitude().trim()));
                    double distance = ((double) destination.distanceTo(mLocation));

                    channel.setDistanceToPoint(distance);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }


            ListItem listItem = new ListItem(channel, ListItem.ITEMTYPE_ATM_BRANCH);
            int iconId = -1;
            if (channel.isDisabledBank()) {
                iconId = R.drawable.map_list_disabled;
            } else if (channel.isConceptBank()) {
                iconId = R.drawable.map_list_concept;
            } else if (channel.isSpecialBank()) {
                iconId = R.drawable.map_list_special;
            } else if (channel.isBranch()) {
                iconId = R.drawable.map_branch_address;
            } else if (channel.isAtm()) {
                iconId = R.drawable.map_atm_address;
            }
            if (iconId != -1)
                listItem.setImage(iconId);
            listItem.setType(ListItem.ITEMTYPE_ATM_BRANCH);
            this.mItems.add(listItem);
        }

        if (mLocation != null && !mLocation.isDefault()) {
            this.orderByDistance(this.mItems);
        } else {
            this.orderByName(this.mItems);
        }

        notifyDataSetInvalidated();
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


    public void setItems(HashSet<Channel> regions, HashSet<Channel> items) {


        //                    String jsonObj = cursorRegion.getString(cursorRegion.getColumnIndexOrThrow(AtmBranchDbHelper.KEY_OBJECT));
//                    Channel channel = gson.fromJson(jsonObj, Channel.class);

        notifyDataSetChanged();
        this.mChannels = new ArrayList<>(items);
        this.mItems.clear();

        HashMap<String, ArrayList<LatLng>> regs = new HashMap<String, ArrayList<LatLng>>();
        for (Channel channel : regions) {
            //Search for regions (city)
            LatLng positionCity = new LatLng(Double.parseDouble(channel.getLatitude()), Double.parseDouble(channel.getLongitude()));
            if (regs.containsKey(channel.getName())) {
                regs.get(channel.getName()).add(positionCity);
            } else {
                ArrayList<LatLng> list = new ArrayList<LatLng>();
                list.add(positionCity);
                regs.put(channel.getName(), list);
            }

        }

        if (regs.size() > 0) {
            Channel tmpChannelRegion = new Channel() {
                @Override
                public String getChannelId() {
                    return "bolge";
                }
            };
            tmpChannelRegion.setName("Bölge");
            tmpChannelRegion.setAdress(null);
            tmpChannelRegion.setDistanceToPoint(0);
            ListItem headerRegion = new ListItem(tmpChannelRegion, ListItem.ITEMTYPE_HEADER);
            this.mItems.add(headerRegion);
            for (Map.Entry<String, ArrayList<LatLng>> entry : regs.entrySet()) {
                final String key = entry.getKey();
                ArrayList<LatLng> positions = entry.getValue();
                LatLngBounds.Builder b = new LatLngBounds.Builder();
                for (int i = 0; i < positions.size(); i++) {
                    b.include(positions.get(i));
                }
                LatLngBounds bounds = b.build();
                Channel tmpChannel = new Channel() {
                    @Override
                    public String getChannelId() {
                        return "region".concat(key);
                    }
                };
                tmpChannel.setName(key);
                tmpChannel.setAdress(null);
                tmpChannel.setDistanceToPoint(0);
                ListItem listItem = new ListItem(tmpChannel, ListItem.ITEMTYPE_REGION);
                listItem.setImage(R.drawable.map_list_region);
                listItem.setBounds(bounds);
                this.mItems.add(listItem);
            }
        }
        if (items.size() > 0) {
            Channel tmpChannelAtm = new Channel() {
                @Override
                public String getChannelId() {
                    return "atm-sube";
                }
            };
            tmpChannelAtm.setName("ATM/Şube");
            tmpChannelAtm.setAdress(null);
            tmpChannelAtm.setDistanceToPoint(0);
            ListItem headerRegionAtm = new ListItem(tmpChannelAtm, ListItem.ITEMTYPE_HEADER);
            this.mItems.add(headerRegionAtm);
            List<ListItem> atmBranchs = new ArrayList<>();
            for (Channel channel : items) {
                if (mLocation != null) {
                    android.location.Location destination = new android.location.Location("");

                    try {
                        destination.setLatitude(Double.parseDouble(channel.getLatitude().trim()));
                        destination.setLongitude(Double.parseDouble(channel.getLongitude().trim()));
                        double distance = ((double) destination.distanceTo(mLocation));

                        channel.setDistanceToPoint(distance);
                    } catch (Exception ex) {
                    }
                }
                ListItem listItem = new ListItem(channel, ListItem.ITEMTYPE_ATM_BRANCH);
                int iconId = -1;
                if (channel.isDisabledBank()) {
                    iconId = R.drawable.map_list_disabled;
                } else if (channel.isConceptBank()) {
                    iconId = R.drawable.map_list_concept;
                } else if (channel.isSpecialBank()) {
                    iconId = R.drawable.map_list_special;
                } else if (channel.isBranch()) {
                    iconId = R.drawable.map_branch_address;
                } else if (channel.isAtm()) {
                    iconId = R.drawable.map_atm_address;
                }
                if (iconId != -1)
                    listItem.setImage(iconId);

                listItem.setType(ListItem.ITEMTYPE_ATM_BRANCH);
                atmBranchs.add(listItem);
            }

            if (mLocation != null)
                orderByDistance(atmBranchs);
            else
                orderByName(atmBranchs);

            this.mItems.addAll(atmBranchs);
        }
        notifyDataSetInvalidated();
    }


    private void orderByName(List<ListItem> list) {

        if (list == null)
            return;

        if (list.size() > 0) {
            Collections.sort(list, new Comparator<ListItem>() {
                @Override
                public int compare(ListItem lhs, ListItem rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            });
        }
    }


    private void orderByDistance(List<ListItem> list) {

        if (list == null) {
            return;
        }

        if (list.size() > 0) {
            Collections.sort(list, new Comparator<ListItem>() {
                @Override
                public int compare(ListItem lhs, ListItem rhs) {
                    return (int) (lhs.getChannel().getDistanceToPoint() - rhs.getChannel().getDistanceToPoint());
                }
            });
        }
    }
}