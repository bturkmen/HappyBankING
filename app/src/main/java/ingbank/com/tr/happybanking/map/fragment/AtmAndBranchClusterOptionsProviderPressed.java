package ingbank.com.tr.happybanking.map.fragment;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.util.LruCache;
import android.util.TypedValue;

import com.androidmapsextensions.ClusterOptions;
import com.androidmapsextensions.ClusterOptionsProvider;
import com.androidmapsextensions.Marker;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.security.InvalidParameterException;
import java.util.List;

import ingbank.com.tr.happybanking.R;
import ingbank.com.tr.happybanking.map.MapActivity;
import ingbank.com.tr.happybanking.map.model.map.Channel;

public class AtmAndBranchClusterOptionsProviderPressed implements ClusterOptionsProvider {

    private LruCache<Integer, BitmapDescriptor> cache = new LruCache<>(128);
    private Resources mResources;
    private ClusterOptions clusterOptions = new ClusterOptions().anchor(0.5f, 0.5f);
    private Paint clusterSizeText = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Rect bounds = new Rect();
    private float x, y;
    private Canvas canvas;
    private String text;
    private Bitmap bitmap;

    public AtmAndBranchClusterOptionsProviderPressed(Resources resources) {
        this.mResources = resources;
    }

    public Resources getResources() {
        return this.mResources;
    }

    @Override
    public ClusterOptions getClusterOptions(List<Marker> markers) {

        int markersCount = markers.size();
        text = String.valueOf(markersCount);

        MarkerType markerType = getMarkerType(markers);

        Bitmap base = markerTypeToBitMap(markerType);

        bitmap = base.copy(Bitmap.Config.ARGB_8888, true);

        adjustClusterSizeText(markerType);

        canvas.drawText(text, x, y, clusterSizeText);

        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);

        cache.put(markersCount, icon);

        return clusterOptions.icon(icon);
    }

    /**
     * Returns marker type of the Cluster according to it's children's channelType value
     *
     * @param markers list of markers
     * @return Marker Type
     */
    private MarkerType getMarkerType(List<Marker> markers) {
        boolean containsATM = false;
        boolean containsBranch = false;
        for (Marker marker : markers) {
            Channel channel = marker.getData();
            if (channel != null) {
                if (channel.getBankChannelType().equals(MapActivity.CHANNELTYPE_ATM)) {
                    containsATM = true;
                    if (containsBranch)
                        break;
                } else if (channel.getBankChannelType().equals(MapActivity.CHANNELTYPE_BRANCH)) {
                    containsBranch = true;
                    if (containsATM)
                        break;
                }
            }
        }

        MarkerType markerType = MarkerType.NONE;
        if (containsATM && containsBranch)
            markerType = MarkerType.ATM_AND_BRANCH;
        else if (containsATM)
            markerType = MarkerType.ATM;
        else if (containsBranch)
            markerType = MarkerType.BRANCH;

        return markerType;
    }

    private Bitmap markerTypeToBitMap(MarkerType type) {
        switch (type) {
            case NONE:
            case ATM:
                return BitmapFactory.decodeResource(getResources(),
                        R.drawable.map_placemarker_cluster_blue_atm);
            case BRANCH:
                return BitmapFactory.decodeResource(getResources(),
                        R.drawable.map_placemarker_cluster_blue_branch);
            case ATM_AND_BRANCH:
                return BitmapFactory.decodeResource(getResources(),
                        R.drawable.map_placemarker_cluster);
            default:
                throw new InvalidParameterException();
        }
    }

    /**
     * Adjust the Size, Color, Margin, X,Y values of "Cluster Size Text"
     *
     * @param type Marker Type
     */
    public void adjustClusterSizeText(MarkerType type) {
        float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, getResources().getDisplayMetrics());
        clusterSizeText.getTextBounds(text, 0, text.length(), bounds);

        clusterSizeText.setFakeBoldText(true);
        clusterSizeText.setTextAlign(Paint.Align.CENTER);

        canvas = new Canvas(bitmap);
        x = (canvas.getWidth() / 2.0f);
        y = ((canvas.getHeight() + bounds.height()) / 3.0f) + 5.0f;

        if (text.length() > 3) {
            y += 4.0f;
        }

        switch (type) {
            case NONE:
                throw new InvalidParameterException();
            case ATM:
                clusterSizeText.setColor(getResources().getColor(R.color.white));
                x += TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, getResources().getDisplayMetrics());
                clusterSizeText.setTextSize(textSize);
                break;
            case BRANCH:
                clusterSizeText.setColor(getResources().getColor(R.color.white));
                x += TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, getResources().getDisplayMetrics());
                clusterSizeText.setTextSize(textSize);
                break;
            case ATM_AND_BRANCH:
                clusterSizeText.setColor(getResources().getColor(R.color.ing_orange));
                textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
                clusterSizeText.setTextSize(textSize);
                break;
            default:

        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    enum MarkerType {
        NONE,
        ATM_AND_BRANCH,
        BRANCH,
        ATM
    }
}
