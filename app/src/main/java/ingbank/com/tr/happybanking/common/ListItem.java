package ingbank.com.tr.happybanking.common;


import com.google.android.gms.maps.model.LatLngBounds;

import ingbank.com.tr.happybanking.map.model.map.Channel;
import ingbank.com.tr.happybanking.map.model.map.LatLng;


public class ListItem {

    public final static int ITEMTYPE_HEADER = 0;
    public final static int ITEMTYPE_ATM_BRANCH = 1;
    public final static int ITEMTYPE_REGION = 2;

    private int mType; //section header, atm/branch, region
    private int mImage;
    private LatLng mPosition; //only atm/branches have positions
    private LatLngBounds mBounds; //only regions have bounds instead of position

    private Channel mChannel;

    public ListItem(Channel channel, int type) {
        this.setChannel(channel);
        this.mType = type;
    }

    public String getItemId() {
        return this.getChannel().getChannelId();
    }


    public String getName() {
        return this.getChannel().getName();
    }


    public String getAddress() {
        return this.getChannel().getAdress();
    }


    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public String getDistance() {
        double distance = this.getChannel().getDistanceToPoint();
        if (distance <= 0) {
            return "";
        } else if (distance < 1000) {
            return String.format("%.0f m", distance);
        } else {
            return String.format("%.2f km", distance / 1000);
        }
    }


    public int getImage() {
        return mImage;
    }

    public void setImage(int image) {
        mImage = image;
    }

    public LatLngBounds getBounds() {
        return mBounds;
    }

    public void setBounds(LatLngBounds bounds) {
        mBounds = bounds;
    }

    @Override
    public int hashCode() {
        return this.getItemId().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (!o.getClass().equals(ListItem.class))
            return false;

        ListItem comparedItem = (ListItem) o;

        return this.getItemId().equals(comparedItem.getItemId());

    }

    public Channel getChannel() {
        return mChannel;
    }

    public void setChannel(Channel mChannel) {
        this.mChannel = mChannel;
    }
}

