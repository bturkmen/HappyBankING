package ingbank.com.tr.happybanking.common.ui.imagehandler;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;


import java.io.File;
import java.io.InputStream;

import ingbank.com.tr.happybanking.common.util.Utils;

/**
 * Created by Ozan on 17/09/14.
 */
public class ImageHandler {

    public static final String IMAGE_FOLDER_PATH = Environment.getExternalStorageDirectory().toString() + "/ingprofile";
    public static final String IMAGE_FILE = "temp.png";
    public static final int IMAGE_CAMERA_REQUEST = 1;
    public static final int IMAGE_GALLERY_REQUEST = 2;
    public static final int THUMBNAIL_SIZE = 500;
    public static Uri IMAGE_CAMERA_URI;

    public static Intent capturePhotoFromCameraIntent() {
        File imagesFolder = new File(IMAGE_FOLDER_PATH);
        if (!imagesFolder.exists()) {
            imagesFolder.mkdirs();
        }
        File image = new File(IMAGE_FOLDER_PATH, IMAGE_FILE);
        IMAGE_CAMERA_URI = Uri.fromFile(image);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, IMAGE_CAMERA_URI);
        //act.startActivityForResult(takePictureIntent, IMAGE_CAMERA_REQUEST);

        return takePictureIntent;
    }

    public static void removePhotoFromCamera() {
        Utils.deleteFiles(IMAGE_FOLDER_PATH);
    }

    public static Intent pickPhotoFromGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //activity.startActivityForResult(intent, IMAGE_GALLERY_REQUEST);
        return intent;
    }

    public static Bitmap rotateImageForService(Context context, Bitmap bitmap, Uri imageUri) {
        Bitmap rotatedBitmap = null;
        try {
            rotatedBitmap = bitmap;
            //rotatedBitmap.recycle();

            int orientation = getOrientation(context, imageUri);
            if (orientation > 0) {
                switch (orientation) {
                    case 90:
                        rotatedBitmap = Utils.rotateBitmap(bitmap, 90);
                        break;
                    case 180:
                        rotatedBitmap = Utils.rotateBitmap(bitmap, 180);
                        break;
                    case 270:
                        rotatedBitmap = Utils.rotateBitmap(bitmap, 270);
                        break;
                }
            } else {
                ExifInterface ei = new ExifInterface(imageUri.getPath());
                orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotatedBitmap = Utils.rotateBitmap(bitmap, 90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotatedBitmap = Utils.rotateBitmap(bitmap, 180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotatedBitmap = Utils.rotateBitmap(bitmap, 270);
                        break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return rotatedBitmap;
    }

    public static int getOrientation(Context context, Uri photoUri) {
        /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);


        if (cursor == null || cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public static Bitmap scaleImageForService(Bitmap bitmap) {
        Bitmap resultBitmap = null;
        try {
            Bitmap scaledBitmap = Utils.scaleBitmap(bitmap, 128, 128);
            resultBitmap = scaledBitmap;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return resultBitmap;
    }


    public static Bitmap scaleImageForService(Bitmap bitmap, int width, int height) {
        Bitmap resultBitmap = null;
        try {
            Bitmap scaledBitmap = Utils.scaleBitmap(bitmap, width, width);
            resultBitmap = scaledBitmap;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return resultBitmap;
    }

    public static Bitmap getContactPhoto(ContentResolver cr, String photoId) {
        if (photoId == null) {
            return null;
        }
        Cursor c = cr.query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO}, ContactsContract.Data._ID + "=?", new String[]{photoId}, null);
        byte[] imageBytes = null;
        if (c != null) {
            if (c.moveToFirst()) {
                imageBytes = c.getBlob(0);
            }
            c.close();
        }

        if (imageBytes != null) {
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } else {
            return null;
        }
    }

    public static Bitmap getThumbnailImage(ContentResolver cr, Uri uri) {
        Bitmap bitmap = null;
        try {
            InputStream input = cr.openInputStream(uri);

            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;
            onlyBoundsOptions.inDither = true;//optional
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            input.close();

            if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
                return null;

            int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

            double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;

            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
            bitmapOptions.inDither = true;//optional
            bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
            input = cr.openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
            input.close();

        } catch (Exception ex) {

        }

        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }
}
