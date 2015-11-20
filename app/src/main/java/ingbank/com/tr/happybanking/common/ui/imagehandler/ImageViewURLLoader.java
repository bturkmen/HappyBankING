package ingbank.com.tr.happybanking.common.ui.imagehandler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;

/**
 * Created by Ozan on 15/09/14.
 */
public class ImageViewURLLoader extends AsyncTask<String, Void, Bitmap> {
    private ImageListener _listener;

    public ImageViewURLLoader(ImageListener listener) {
        this._listener = listener;
    }

    @Override
    protected void onPreExecute() {
        _listener.onProgress();
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String imageURL = urls[0];
        Bitmap bitmap = null;
        try {
            InputStream in = new java.net.URL(imageURL).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap resultImage) {
        if (resultImage != null) {
            _listener.onSuccess(resultImage);
        } else {
            _listener.onFailure();
        }
    }


}