package ingbank.com.tr.happybanking.common.ui.inputwatcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class BaseTextWatcher implements TextWatcher {

    public int mMaxLenght;
    protected TextWatcherListener mListener = null;
    private EditText mView = null;

    public BaseTextWatcher() {
    }


    public BaseTextWatcher(EditText editText) {
        this.mView = editText;
    }

    public BaseTextWatcher(TextWatcherListener mListener, int maxLenght) {
        this.mListener = mListener;
        this.mMaxLenght = maxLenght;
    }

    public BaseTextWatcher(EditText editText, TextWatcherListener mListener) {
        this.mView = editText;
        this.mListener = mListener;
    }

    public EditText getEditText() {
        return this.mView;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        if (mView != null)
            mView.setError(null);
        if (mListener != null)
            mListener.onTextChanged(mView, charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.length() == mMaxLenght && this.mListener != null) mListener.onTextFilledMax();

    }

    public void animate(View view) {
//        Animation shake = AnimationUtils.loadAnimation(mView.getContext(), R.anim.shake);
//        mView.startAnimation(shake);
    }

    public interface TextWatcherListener {

        public void onTextChanged(EditText view, String text);

        public void onTextFilledMax();
    }
}