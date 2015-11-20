package ingbank.com.tr.happybanking.common;



import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import ingbank.com.tr.happybanking.map.model.map.Channel;


public class MapItem implements ClusterItem
 {
    private String mItemId;
    private Channel mChannel;
    private int mMarkerImage;
    private final LatLng mPosition;

    public MapItem(Channel channel, int image, LatLng pos) {
        if(null != channel) {
            setItemId(channel.getChannelId());
        }
        setChannel(channel);
        setMarkerImage(image);
        mPosition = pos;
    }

    public Channel getChannel() {
        return mChannel;
    }

    public void setChannel(Channel channel) {
        mChannel = channel;
    }

    public int getMarkerImage() {
        return mMarkerImage;
    }

    public void setMarkerImage(int markerImage) {
        mMarkerImage = markerImage;
    }

    public String getItemId() {
        return mItemId;
    }

    public void setItemId(String itemId) {
        mItemId = itemId;
    }

    @Override
    public com.google.android.gms.maps.model.LatLng getPosition() {
        return mPosition;
    }
}
