package ingbank.com.tr.happybanking.common.util;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ingbank.com.tr.happybanking.common.Constants;

public class Utils {
    public static final int PIN_LENGTH = 6;
    public static final int PIN_MAX_CONSECUTIVE_ALLOWED = 3;
    public static final int PIN_MAX_REPEATING_ALLOWED = 2;

    public static final int TCKN_LENGTH = 11;
    public static final int PHONE_LENGTH = 10;
    public static final String MONEY_FORMAT = "###,###,###.00";
    public static final String CVV_MASK_CHAR = "*";
    /**
     * Phone Type for Mobile Phones...
     */
    public static final String PHONE_TYPE_FOR_MOBILE = "5";
    public static final String COUNTRY_CODE_FOR_TURKEY = "90";
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
    public static Locale TargetLocal = new Locale("tr-TR");
    private static NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("tr", "TR"));

    public static String appendWildcard(String query) {
        if (TextUtils.isEmpty(query)) return query;

        query = query.toLowerCase(Utils.TargetLocal);
        final StringBuilder builder = new StringBuilder();
        final String[] splits = TextUtils.split(query, " ");
        for (String split : splits)
            builder.append("%").append(split).append("%").append(" ");

        return builder.toString().trim();
    }

    public static String strNormalize(String str) {
        String nameNormalize = str;
        nameNormalize = nameNormalize.replace("ı", "i");
        nameNormalize = nameNormalize.replace("ğ", "g");
        nameNormalize = nameNormalize.replace("ü", "u");
        nameNormalize = nameNormalize.replace("ş", "s");
        nameNormalize = nameNormalize.replace("ö", "o");
        nameNormalize = nameNormalize.replace("ç", "c");
        nameNormalize = nameNormalize.replace("İ", "I");
        nameNormalize = nameNormalize.replace("Ğ", "G");
        nameNormalize = nameNormalize.replace("Ü", "U");
        nameNormalize = nameNormalize.replace("Ş", "S");
        nameNormalize = nameNormalize.replace("Ö", "O");
        nameNormalize = nameNormalize.replace("Ç", "C");
        return nameNormalize;
    }

    public static boolean validatePhoneNumber(String phoneNo) {
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{10}")) return true;
            //validating phone number with -, . or spaces
        else if (phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
            //validating phone number with extension length from 3 to 5
        else if (phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
            //validating phone number where area code is in braces ()
        else if (phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
            //return false if nothing matches the input

        else return false;

    }


    public static List<String> splitEqually(String text, int size) {
        // Give the list the right capacity to start with. You could use an array
        // instead if you wanted.
        List<String> ret = new ArrayList<String>((text.length() + size - 1) / size);

        for (int start = 0; start < text.length(); start += size) {
            ret.add(text.substring(start, Math.min(text.length(), start + size)));
        }
        return ret;
    }

    public static String formatPhoneNumber(String phone) {
        phone = phone.replace(" ", "");
        phone = phone.replace("+9", "");
        phone = phone.replace(" ", "");

        if (phone.length() > 10 && phone.startsWith("0")) {
            phone = phone.substring(1);
        }

        if (phone.length() != 10 && !phone.startsWith("5")) {
            phone = "";
        }

        if (TextUtils.isEmpty(phone))
            return "";
        if (!TextUtils.isDigitsOnly(phone))
            return "";

        String formattedNumber = String.format("%s %s %s %s",
                phone.substring(0, 3),
                phone.substring(3, 6),
                phone.substring(6, 8), phone.substring(8, 10));
        return formattedNumber;
    }

    public static String formatPhoneNumberWithParanthesis(String phone) {
        phone = phone.replace(" ", "");
        phone = phone.replace("+9", "");
        phone = phone.replace(" ", "");

        if (phone.length() > 10 && phone.startsWith("0")) {
            phone = phone.substring(1);
        }

        if (phone.length() != 10 && !phone.startsWith("5")) {
            phone = "";
        }

        if (TextUtils.isEmpty(phone))
            return "";
        if (!TextUtils.isDigitsOnly(phone))
            return "";

        String formattedNumber = String.format("(%s) %s %s %s",
                phone.substring(0, 3),
                phone.substring(3, 6),
                phone.substring(6, 8), phone.substring(8, 10));
        return formattedNumber;
    }

    public static String formatPhoneNumberMaskedWithParanthesis(String phone) {
        phone = phone.replace(" ", "");
        phone = phone.replace("+9", "");
        phone = phone.replace(" ", "");

        if (phone.length() > 10 && phone.startsWith("0")) {
            phone = phone.substring(1);
        }

        if (phone.length() != 10 && !phone.startsWith("5")) {
            phone = "";
        }

        if (TextUtils.isEmpty(phone))
            return "";
        if (!TextUtils.isDigitsOnly(phone))
            return "";

        String formattedNumber = String.format("(%s) %s %s %s",
                phone.substring(0, 2) + "*",
                "***",
                "**", phone.substring(8, 10));
        return formattedNumber;
    }

    public static boolean validateTckn(String tckn) {
        if (tckn.length() != TCKN_LENGTH)
            return false;

        int total = 0;
        for (int i = 0; i < TCKN_LENGTH - 1; i++) {
            total += Integer.parseInt(tckn.substring(i, i + 1));
        }

        String lastChar = Integer.toString(total).substring(1, 2);
        return lastChar.equals(tckn.substring(TCKN_LENGTH - 1, TCKN_LENGTH));
    }

    /**
     * Format the iban TRxx xxxx xxxx xxxx xxxx xxxx xx
     *
     * @param iban related iban number
     * @return formatted iban number
     */
    public static String formatIban(String iban) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < iban.length(); i++) {
            if (i % 4 == 0 && i != 0) {
                result.append(" ");
            }

            result.append(iban.charAt(i));
        }

        return result.toString();
    }

    public static String formatCurrency(double balance) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("tr", "TR"));
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);
        String formatted = formatter.format((balance));
        return formatted;
    }

    public static String formatCurrencyForSellingPrice(double balance) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("tr", "TR"));
        formatter.setMaximumFractionDigits(3);
        formatter.setMinimumFractionDigits(3);
        String formatted = formatter.format((balance));
        return formatted;
    }

    public static String formatCurrencyForExchange(double balance) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("tr", "TR"));
        formatter.setMaximumFractionDigits(6);
        formatter.setMinimumFractionDigits(6);
        String formatted = formatter.format((balance));
        return formatted;
    }

    public static boolean validateEmail(String email) {
        Pattern p = Pattern.compile(EMAIL_PATTERN);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static String removeWhiteSpaces(String string) {
        return string.replaceAll("\\s+", "");
    }

    public static String getSoftwareVersion(Context context) {
        PackageInfo pi;
        try {
            pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (final PackageManager.NameNotFoundException e) {
            return "na";
        }
    }

    public static String getDateString(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    public static String GetUnixTimeGMTString(String format) {
        SimpleDateFormat smf = new SimpleDateFormat(format);
        return smf.format(getUnixTimeGMT());

    }

    public static long getUnixTimeGMT() {
        return System.currentTimeMillis();
    }


    public static String getUtcDateAsString(String format) {
        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(getUnixTimeGMT());
    }

    public static String getUtcDate() {
        DateFormat df = DateFormat.getTimeInstance();
        df.setTimeZone(TimeZone.getTimeZone("gmt"));
        String gmtTime = df.format(new Date());
        return gmtTime;
    }

    public static Date parseRawDateStrToDate(String rawDate) {
        try {
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            return parser.parse(rawDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static String dateStringToSimpleDate(Date date) {
        try {
            SimpleDateFormat dt1 = new SimpleDateFormat("dd.MM.yyyy");
            return dt1.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String dateStringToFormattedDateString(String source) {
        String target = null;
        try {
            DateFormat sourceDf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = sourceDf.parse(source);
            DateFormat targetDf = new SimpleDateFormat("d MMMM yyyy',' HH:mm", new Locale("tr"));
            target = targetDf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return target;
    }

    public static HashMap<String, Long> calculateLeftTimeToExpiration(String trxDateStr) {
        DateFormat sourceDf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date trxDate = null;
        try {
            trxDate = sourceDf.parse(trxDateStr);
        } catch (Exception e) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(trxDate);
        cal.add(Calendar.DATE, 7);
        Date timeoutDate = cal.getTime();

        //in milliseconds
        Date now = new Date();
        long diff = timeoutDate.getTime() - now.getTime();

        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        HashMap<String, Long> map = new HashMap<String, Long>();
        map.put("m", diffMinutes);
        map.put("h", diffHours);
        map.put("d", diffDays);

        return map;
    }

    //returns true if there is a consecutive pattern longer than PIN_MAX_CONSECUTIVE_ALLOWED in str
    public static boolean checkConsecutiveNumbers(String str) {
        int maxLength = 1;
        int cur, prev = -1;

        char[] values = str.toCharArray();

        //count increasing consecutive numbers
        for (int i = 0; i < values.length; i++) {
            cur = Integer.parseInt(Character.toString(values[i]));
            if (cur == prev + 1) {
                maxLength++;
            } else {
                maxLength = 1;
            }
            prev = cur;
        }
        if (PIN_MAX_CONSECUTIVE_ALLOWED < maxLength) {
            return true;
        }

        //count decreasing consecutive numbers
        maxLength = 1;
        prev = -1;
        for (int i = 0; i < values.length; i++) {
            cur = Integer.parseInt(Character.toString(values[i]));
            if (cur == prev - 1) {
                maxLength++;
            } else {
                maxLength = 1;
            }
            prev = cur;
        }
        if (PIN_MAX_CONSECUTIVE_ALLOWED < maxLength) {
            return true;
        }

        return false;
    }

    //returns true if any digit in str is repating more than PIN_MAX_REPEATING_ALLOWED
    public static boolean checkRepeatingNumbers(String str) {
        char[] values = str.toCharArray();

        for (int num = 0; num <= 9; num++) {
            int maxLength = 0;
            int tempLength = 0;

            for (char value : values) {
                tempLength = (value == num + '0') ? 1 + tempLength : 0;
                if (tempLength > maxLength) {
                    maxLength = tempLength;
                }
            }

            if (PIN_MAX_REPEATING_ALLOWED < maxLength) {
                return true;
            }
        }

        return false;
    }

    public static boolean checkPinBlacklist(String str) {

        if (str.equals("123123")) {
            return true;
        }

        return false;
    }

    public static boolean checkPinBeginsWith19(String str) {

        if (str.startsWith("19")) {
            return true;
        }

        return false;
    }

    public static boolean checkPinMismatch(String str1, String str2) {

        if (str1.equals(str2)) {
            return false;
        }

        return true;
    }

    public static String[] cleanNullValues(String[] v) {
        List<String> list = new ArrayList<String>(Arrays.asList(v));
        list.removeAll(Collections.singleton(null));
        return list.toArray(new String[list.size()]);
    }


    public static boolean isPinValid(String string) {
        if (string.length() != 4)
            return false;

        if (Utils.checkConsecutiveNumbers(string))
            return false;

//        if (Utils.CheckAllCharsAreSame(string))
//            return false;

        if (string.startsWith("19"))
            return false;

        return true;
    }

    public static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static String getVersionName(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException ex) {
            return Constants.EMPTY_STRING;
        }
    }

    /**
     * Creates bitmap image with according to given sample size.
     *
     * @param file
     * @param sampleSize
     * @return Bitmap
     * @throws IOException
     */
    public static Bitmap createBitmap(File file, int sampleSize) throws IOException {
        return createBitmap(file.getAbsolutePath(), sampleSize);
    }

    /**
     * Creates bitmap image with according to given sample size.
     *
     * @param path
     * @param sampleSize
     * @return Bitmap
     * @throws IOException
     */
    public static Bitmap createBitmap(String path, int sampleSize) throws IOException {
        ExifInterface exif = new ExifInterface(
                path);
        int rotate = 0;
        int orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);

        Bitmap bitmap;
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = sampleSize;
        bitmap = BitmapFactory.decodeFile(path,
                bitmapOptions);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bitmap;

    }

    /**
     * Create bitmap from Base64 string. If operation fails, returns null.
     *
     * @param encodedBase64Image
     * @return Bitmap
     */
    public static Bitmap convertBase64StringToBitmap(String encodedBase64Image) {
        Bitmap decodedBitmap = null;
        if (TextUtils.isEmpty(encodedBase64Image)) {
            return decodedBitmap;
        }

        byte[] decodedByte = Base64.decode(encodedBase64Image, Base64.DEFAULT);
        decodedBitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        return decodedBitmap;
    }

    public static String convertBitmapToBase64String(Bitmap rawBitmap) {
        if (rawBitmap == null) {
            return null;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        rawBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        rawBitmap.recycle();

        String encodedImage = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stream = null;
        }

        return encodedImage;
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap scaleBitmap(Bitmap bm, int maxWidth, int maxHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();


        if (width > height) {
            // landscape
            int ratio = width / maxWidth;
            width = maxWidth;
            if (ratio != 0) {
                height = height / ratio;
            }
        } else if (height > width) {
            // portrait
            int ratio = height / maxHeight;
            height = maxHeight;
            if (ratio != 0) {
                width = width / ratio;
            }
        } else {
            // square
            height = maxHeight;
            width = maxWidth;
        }

        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
    }

    /**
     * TODO: Formata bagli olarak methodlar azaltildi (ozan), regex'e gerek yok artik
     *
     * @param cardNumber
     * @param format
     * @return
     */
    public static String convertCardNumberToFormattedCardNumber(String cardNumber, FormattedCardFormat format) {
        String maskedCardNumber = null;

        if (TextUtils.isEmpty(cardNumber) || cardNumber.length() != 16) {
            return maskedCardNumber;
        }

        if (format == FormattedCardFormat.masked) {
            String cardPart1 = cardNumber.substring(0, 4);
            String cardPart2 = "";
            for (int j = 0; j < 8; j++) {
                cardPart2 = cardPart2.concat("*");
            }
            String cardPart3 = cardNumber.substring(12);

            maskedCardNumber = cardPart1.concat(cardPart2).concat(cardPart3);
        } else if (format == FormattedCardFormat.separate_blank) {
            String cardPart1 = cardNumber.substring(0, 4);
            String cardPart2 = cardNumber.substring(4, 8);
            String cardPart3 = cardNumber.substring(8, 12);
            String cardPart4 = cardNumber.substring(12, 16);

            maskedCardNumber = cardPart1 + " " + cardPart2 + " " + cardPart3 + " " + cardPart4;
        }

        return maskedCardNumber;
    }

    public static String convertFormettedCardNumberToCardNumber(String formattedCardNumber, FormattedCardFormat format) {
        String cardNumber = null;

        if (TextUtils.isEmpty(formattedCardNumber)) {
            return cardNumber;
        }

        if (format == FormattedCardFormat.separate_blank) {
            cardNumber = formattedCardNumber.replace(" ", "");
        }
        return cardNumber;
    }

    public static String convertPhoneNumberToFormattedPhoneNumber(String phoneNumber, FormattedPhoneFormat format) {
        String formattedPhoneNumber = null;

        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 10) {
            return formattedPhoneNumber;
        }

        String phonePart1 = phoneNumber.substring(0, 3);
        String phonePart2 = phoneNumber.substring(3, 6);
        String phonePart3 = phoneNumber.substring(6, 8);
        String phonePart4 = phoneNumber.substring(8, 10);

        if (format == FormattedPhoneFormat.separate_parentheses) {
            formattedPhoneNumber = "(" + phonePart1 + ") " + phonePart2 + " " + phonePart3 + " " + phonePart4;
        } else if (format == FormattedPhoneFormat.separate_blank) {
            formattedPhoneNumber = phonePart1 + " " + phonePart2 + " " + phonePart3 + " " + phonePart4;
        }

        return formattedPhoneNumber;
    }

    public static String convertFormattedPhoneNumberToPhoneNumber(String formattedPhoneNumber, FormattedPhoneFormat format) {
        String phoneNumber = null;

        if (TextUtils.isEmpty(formattedPhoneNumber)) {
            return null;
        }

        if (format == FormattedPhoneFormat.separate_blank) {
            phoneNumber = formattedPhoneNumber.replace(" ", "");
        }

        if (phoneNumber == null || TextUtils.isEmpty(phoneNumber))
            return null;

        phoneNumber = phoneNumber.replace("(", "");
        phoneNumber = phoneNumber.replace(")", "");

        return phoneNumber;
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() != 10)
            return false;

        for (int i = 0; i < phoneNumber.length(); i++) {
            if (!(phoneNumber.charAt(i) >= '0' && phoneNumber.charAt(i) <= '9'))
                return false;
        }

        return true;
    }

    public static void deleteFiles(String path) {
        File file = new File(path);
        if (file.exists()) {
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) {
            }
        }
    }

    public static String formatCardNumber(String string) {
        return string.replaceAll("\\d{4}", "$0 ").trim();
    }


    public static boolean validateCardNumber(String cardNumber) {
        if (TextUtils.isEmpty(cardNumber))
            return false;
        if (!TextUtils.isDigitsOnly(cardNumber))
            return false;
        if (cardNumber.length() != 16)
            return false;

        int sum2 = 0, sum3 = 0;

        for (int i = 0; i < 16; i += 2) {
            int sum1 = 2 * Integer.parseInt(cardNumber.substring(i, i + 1));
            sum2 += (sum1 / 10) + (sum1 % 10);
        }

        for (int i = 1; i < 16; i += 2) {
            sum3 += Integer.parseInt(cardNumber.substring(i, i + 1));
        }

        return (sum2 + sum3) % 10 == 0;
    }

    public static String writeBitmapToFile(Bitmap bitmap, Context context) {
        String fileName = null;
        if (bitmap != null) {
            fileName = "moneyRequestImage";//no .png or .jpg needed
            try {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                FileOutputStream fo = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (Exception e) {
                e.printStackTrace();
                fileName = null;
            }
        }
        return fileName;
    }

    public static boolean isDefaultDatabaseDate(String date) {
        if (date == null || date.equals("1 Ocak 1900"))
            return false;

        return true;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static <T> Iterable<Iterable<T>> chunk(Iterable<T> in, int size) {
        List<Iterable<T>> lists = new ArrayList();
        Iterator<T> i = in.iterator();
        while (i.hasNext()) {
            List<T> list = new ArrayList();
            for (int j = 0; i.hasNext() && j < size; j++) {
                list.add(i.next());
            }
            lists.add(list);
        }
        return lists;
    }

    public static int dpToPx(double dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static String getIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();

                    // for getting IPV4 format
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
        return null;
    }

    public static void hideKeyboard(Activity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void hideKeyboard(View view) {
        if (view != null) {
            view.clearFocus();
            InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void showKeyboard(Activity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            showKeyboard(view);
        }
    }

    public static void showKeyboard(View view) {
        view.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, 0);
    }

    public static String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    public static String getDeviceModel() {
        return Build.MODEL;

    }

    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static String maskFullName(String fullName) {
        String maskedNameSurname = "";
        if (fullName != null && fullName.length() > 0) {
            String[] names = fullName.split(" ");
            for (int i = 0; i < names.length; i++) {
                if (names[i].length() > 0) {
                    names[i] = names[i].substring(0, 1);
                    maskedNameSurname += names[i] + "*** ";
                }
            }
            maskedNameSurname = maskedNameSurname.trim();
        }
        return maskedNameSurname;
    }

    public static String maskTckn(String tckn) {
        String maskedTckn = "";
        if (tckn != null && tckn.length() > 0) {
            maskedTckn = tckn.substring(0, 1);
            for (int i = 0; i < tckn.length() - 2; i++)
                maskedTckn = maskedTckn + "*";
            maskedTckn = maskedTckn + tckn.substring(tckn.length() - 1);
        }

        return maskedTckn;
    }

    public static String cardIbanFormated(String s) {
        int groupDigits = 0;
        String retVal = "";
        for (int i = 0; i < s.length(); ++i) {
            retVal += s.charAt(i);
            ++groupDigits;
            if (groupDigits == 4) {
                retVal += " ";
                groupDigits = 0;
            }
        }
        return retVal;
    }


    public static String getTurkishMonthName(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        int month = calendar.get(Calendar.MONTH) + 1;

        HashMap<Integer, String> months4Turkish = new HashMap<>();
        months4Turkish.put(1, "Ocak");
        months4Turkish.put(2, "Şubat");
        months4Turkish.put(3, "Mart");
        months4Turkish.put(4, "Nisan");
        months4Turkish.put(5, "Mayıs");
        months4Turkish.put(6, "Haziran");
        months4Turkish.put(7, "Temmuz");
        months4Turkish.put(8, "Ağustos");
        months4Turkish.put(9, "Eylül");
        months4Turkish.put(10, "Ekim");
        months4Turkish.put(11, "Kasım");
        months4Turkish.put(12, "Aralık");

        return months4Turkish.get(month);
    }

    public enum DateTodayOrTomorrow {
        today,
        tomorrow,
        undefined
    }

    public enum FormattedCardFormat {
        masked,
        separate_blank
    }

    public enum FormattedPhoneFormat {
        separate_parentheses,
        separate_blank
    }

    public static String getTextWithElipSize(String text, int maxCharachterSize) {
        if (text != null && text.trim().length() >= maxCharachterSize) {
            text = text.substring(0, maxCharachterSize) + "...";
        }
        return text;
    }

}