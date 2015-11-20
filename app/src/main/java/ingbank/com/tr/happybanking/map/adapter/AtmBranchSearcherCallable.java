package ingbank.com.tr.happybanking.map.adapter;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;


import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;

import ingbank.com.tr.happybanking.R;
import ingbank.com.tr.happybanking.common.ListItem;
import ingbank.com.tr.happybanking.map.MapActivity;
import ingbank.com.tr.happybanking.map.model.map.Channel;

public class AtmBranchSearcherCallable implements Callable<AtmBranchSearchResult> {
    private Iterable<Channel> mChannels;
    private String charSequence;
    private AtmBranchSearchResult result;

    public AtmBranchSearcherCallable(String query, Iterable<Channel> data) {
        this.charSequence = query;
        this.mChannels = data;
        this.result = new AtmBranchSearchResult();
    }


    private String replaceTurkishCharacters(String word) {
        return Normalizer
                .normalize(word, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }

    @Override
    public AtmBranchSearchResult call() throws Exception {
        String searchQuery = charSequence;
        String normalizedSequence = replaceTurkishCharacters(searchQuery);
        String regex_normalized_up;
        String regex_normalized_low;

        HashMap<String, ArrayList<LatLng>> regions = new HashMap<String, ArrayList<LatLng>>();
        ArrayList<ListItem> tempRegions = new ArrayList<ListItem>();
        ArrayList<ListItem> tempAtmbranches = new ArrayList<ListItem>();

        String reqex_up;
        String reqex_low;
        charSequence = charSequence.toUpperCase();
        reqex_up = "(?i:.*" + charSequence + ".*)";
        regex_normalized_up = "(?i:.*" + normalizedSequence.toUpperCase() + ".*)";
        CharSequence charSequence2 = charSequence.toLowerCase();
        reqex_low = "(?i:.*" + charSequence2 + ".*)";
        regex_normalized_low = "(?i:.*" + normalizedSequence.toLowerCase() + ".*)";

        String city, county, county_name, name;
        Iterator<Channel> iterator = mChannels.iterator();
        while (iterator.hasNext()) {
            Channel channel = iterator.next();
            city = channel.getCity();
            county = channel.getCounty();
            county_name = county + " / " + city;
            name = channel.getName();

            //Search for regions (county)
            if (county.matches(reqex_up) || county.matches(reqex_low) ||
                    county.matches(regex_normalized_up) || county.matches(regex_normalized_low)) {

                LatLng position = new LatLng(Double.parseDouble(channel.getLatitude()), Double.parseDouble(channel.getLongitude()));
                if (regions.containsKey(county_name)) {
                    regions.get(county_name).add(position);
                } else {
                    ArrayList<LatLng> list = new ArrayList<LatLng>();
                    list.add(position);
                    regions.put(county_name, list);
                }
            }
            //Search for regions (city)
            if (city.matches(reqex_up) || city.matches(reqex_low) ||
                    city.matches(regex_normalized_up) || city.matches(regex_normalized_low)) {

                LatLng position = new LatLng(Double.parseDouble(channel.getLatitude()), Double.parseDouble(channel.getLongitude()));
                if (regions.containsKey(city)) {
                    regions.get(city).add(position);
                } else {
                    ArrayList<LatLng> list = new ArrayList<LatLng>();
                    list.add(position);
                    regions.put(city, list);
                }
            }
            //Search for ATM and Branches
            if (name.matches(reqex_up) || name.matches(reqex_low) ||
                    name.matches(regex_normalized_up) || name.matches(regex_normalized_low)) {

                ListItem listItem = new ListItem(channel, ListItem.ITEMTYPE_ATM_BRANCH);
                if (channel.getBankChannelType().equals(MapActivity.CHANNELTYPE_ATM)) {
                    listItem.setImage(R.drawable.map_list_atm);
                } else {
                    listItem.setImage(R.drawable.map_list_branch);
                }
                listItem.setType(ListItem.ITEMTYPE_ATM_BRANCH);

                tempAtmbranches.add(listItem);
            }

        }

        for (Map.Entry<String, ArrayList<LatLng>> entry : regions.entrySet()) {
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

            tempRegions.add(listItem);
        }

        result.Regions.addAll(tempRegions);
        result.Atms.addAll(tempAtmbranches);
        return result;
    }
}
