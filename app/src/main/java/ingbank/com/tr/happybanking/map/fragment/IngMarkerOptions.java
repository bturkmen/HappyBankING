package ingbank.com.tr.happybanking.map.fragment;

import com.androidmapsextensions.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

import ingbank.com.tr.happybanking.map.model.map.Channel;

public class IngMarkerOptions extends MarkerOptions {
    public IngMarkerOptions(Channel channel, BitmapDescriptor icon) {
        this.data(channel);
        Channel data = (Channel) this.getData();
        this.title(data.getName());
        this.position(new LatLng(Double.parseDouble(data.getLatitude()), Double.parseDouble(data.getLongitude())));
        this.icon(icon);
    }
}
