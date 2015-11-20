package ingbank.com.tr.happybanking.common.util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by huseyinatasoy on 08/04/15.
 */
public class FontHelper {


    public static final int FONT_SANSPRO_BLACK = 0;
    public static final int FONT_SANSPRO_BLACK_ITALIC = 1;
    public static final int FONT_SANSPRO_BOLD = 2;
    public static final int FONT_SANSPRO_BOLD_ITALIC = 3;
    public static final int FONT_SANSPRO_EXTRA_LIGHT = 4;
    public static final int FONT_SANSPRO_EXTRA_LIGHT_ITALIC = 5;
    public static final int FONT_SANSPRO_ITALIC = 6;
    public static final int FONT_SANSPRO_LIGHT = 7;
    public static final int FONT_SANSPRO_LIGHT_ITALIC = 8;
    public static final int FONT_SANSPRO_REGULAR = 9;
    public static final int FONT_SANSPRO_SEMIBOLD = 10;
    public static final int FONT_SANSPRO_SEMIBOLD_ITALIC = 11;

    private static final int NUM_OF_CUSTOM_FONTS = 12;

    private static boolean fontsLoaded = false;

    private static Typeface[] fonts = new Typeface[NUM_OF_CUSTOM_FONTS];

    private static String[] fontPath = {

            "fonts/SourceSansPro-Black.ttf",
            "fonts/SourceSansPro-BlackItalic.ttf",
            "fonts/SourceSansPro-Bold.ttf",

            "fonts/SourceSansPro-BoldItalic.ttf",
            "fonts/SourceSansPro-ExtraLight.ttf",
            "fonts/SourceSansPro-ExtraLightItalic.ttf",

            "fonts/SourceSansPro-Italic.ttf",
            "fonts/SourceSansPro-Light.ttf",
            "fonts/SourceSansPro-LightItalic.ttf",

            "fonts/SourceSansPro-Regular.ttf",
            "fonts/SourceSansPro-Semibold.ttf",
            "fonts/SourceSansPro-SemiboldItalic.ttf",

    };


    /**
     * Returns a loaded custom font based on it's identifier.
     *
     * @param context        - the current context
     * @param fontIdentifier = the identifier of the requested font
     * @return Typeface object of the requested font.
     */
    public static Typeface getTypeface(Context context, int fontIdentifier) {
        if (!fontsLoaded) {
            loadFonts(context);
        }
        return fonts[fontIdentifier];
    }

    public static Typeface getTypefaceFromName(Context context, String name) {
        switch (name) {
            case "black":
                return getTypeface(context, FONT_SANSPRO_BLACK);
            case "black_italic":
                return getTypeface(context, FONT_SANSPRO_BLACK_ITALIC);
            case "bold":
                return getTypeface(context, FONT_SANSPRO_BOLD);
            case "bold_italic":
                return getTypeface(context, FONT_SANSPRO_BOLD_ITALIC);
            case "extra_light":
                return getTypeface(context, FONT_SANSPRO_EXTRA_LIGHT);
            case "extra_light_italic":
                return getTypeface(context, FONT_SANSPRO_EXTRA_LIGHT_ITALIC);
            case "italic":
                return getTypeface(context, FONT_SANSPRO_ITALIC);
            case "light":
                return getTypeface(context, FONT_SANSPRO_LIGHT);
            case "light_italic":
                return getTypeface(context, FONT_SANSPRO_LIGHT_ITALIC);
            case "regular":
                return getTypeface(context, FONT_SANSPRO_REGULAR);
            case "semi_bold":
                return getTypeface(context, FONT_SANSPRO_SEMIBOLD);
            case "semi_bold_italic":
                return getTypeface(context, FONT_SANSPRO_SEMIBOLD_ITALIC);
            default:
                return getTypeface(context, FONT_SANSPRO_REGULAR);


        }

    }


    private static void loadFonts(Context context) {
        for (int i = 0; i < NUM_OF_CUSTOM_FONTS; i++) {
            fonts[i] = Typeface.createFromAsset(context.getAssets(), fontPath[i]);
        }
        fontsLoaded = true;
    }


    /**
     * Recursively sets a {@link Typeface} to all
     * Texts in a {@link ViewGroup}.
     */
    public static final void setAppFont(ViewGroup mContainer, boolean reflect) {
        if (mContainer == null) return;

        final int mCount = mContainer.getChildCount();

        // Loop through all oof the children.
        for (int i = 0; i < mCount; ++i) {
            final View mChild = mContainer.getChildAt(i);
            setFont(mChild, reflect);
        }
    }

    public static void setFont(View mChild, boolean reflect) {
        String tag = "regular";
        if (mChild.getTag() != null)
            tag = mChild.getTag().toString();

        if (mChild instanceof TextView) {
            // Set the font if it is a TextView.
            ((TextView) mChild).setTypeface(getTypefaceFromName(mChild.getContext(), tag));
        } else if (mChild instanceof EditText) {
            ((EditText) mChild).setTypeface(getTypefaceFromName(mChild.getContext(), tag));
        } else if (mChild instanceof ViewGroup) {
            // Recursively attempt another ViewGroup.
            setAppFont((ViewGroup) mChild, reflect);
        } else if (reflect) {
            try {
                Method mSetTypeface = mChild.getClass().getMethod("setTypeface", Typeface.class);
                mSetTypeface.invoke(mChild, getTypefaceFromName(mChild.getContext(), tag));
            } catch (Exception e) { /* Do something... */ }
        }
    }


    public static void setDefaultFont(Context context,
                                      String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                fontAssetName);
        try {
            replaceFont(staticTypefaceFieldName, regular);
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }

    }

    /**
     * @param staticTypefaceFieldName
     * @param newTypeface
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    protected static void replaceFont(String staticTypefaceFieldName,
                                      final Typeface newTypeface) throws NoSuchFieldException, IllegalAccessException {

        final Field StaticField = Typeface.class
                .getDeclaredField(staticTypefaceFieldName);
        StaticField.setAccessible(true);
        StaticField.set(null, newTypeface);

    }


}
