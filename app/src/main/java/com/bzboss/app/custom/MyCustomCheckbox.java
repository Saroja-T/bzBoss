package com.bzboss.app.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.bzboss.app.R;


public class MyCustomCheckbox extends AppCompatCheckBox {

    private String setFontType;
    /*private boolean isSetUnderline;
    private int colorCode, spannableStartPosition, spannableEndPosition;*/

    public MyCustomCheckbox(Context context) {
        super(context);
    }

    public MyCustomCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MyCustomCheckbox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context mContext, AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.MyCustomTextView);

        setFontType = typedArray.getString(R.styleable.MyCustomTextView_setTypeFace);

//        isSetUnderline = typedArray.getBoolean(R.styleable.MyCustomTextView_setUnderLine, false);
//
//        colorCode = typedArray.getInteger(R.styleable.MyCustomTextView_setSpannableColor, 0);
//
//        spannableStartPosition = typedArray.getInteger(R.styleable.MyCustomTextView_setSpannableStartPosition, 0);
//
//        spannableEndPosition = typedArray.getInteger(R.styleable.MyCustomTextView_setSpannableEndPosition, 0);

        if (!TextUtils.isEmpty(setFontType))
            setTypeface(Typeface.createFromAsset(mContext.getAssets(), setFontType));

//        if (isSetUnderline){
//            Spannable spannableString = new SpannableString(getText().toString());
//        }

        typedArray.recycle();
    }
}
