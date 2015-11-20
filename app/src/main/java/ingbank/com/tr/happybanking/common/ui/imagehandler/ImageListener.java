package ingbank.com.tr.happybanking.common.ui.imagehandler;

import android.graphics.Bitmap;

/**
 * Created by Ozan on 15/09/14.
 */
public interface ImageListener {
    public void onSuccess(Bitmap resultImage);

    public void onFailure();

    public void onProgress();
}
