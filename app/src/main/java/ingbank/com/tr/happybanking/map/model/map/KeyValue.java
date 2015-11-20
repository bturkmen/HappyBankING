package ingbank.com.tr.happybanking.map.model.map;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 7/1/14.
 * Copyright - Valensas 2014
 *
 * @author Furkan BAYRAKTAR
 */
public class KeyValue {

    @SerializedName("text")
    private String mText;
    @SerializedName("value")
    private int mValue;

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public int getValue() {
        return mValue;
    }

    public void setValue(int value) {
        this.mValue = value;
    }
}
