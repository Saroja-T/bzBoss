package com.bzboss.app.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatButton;

import com.bzboss.app.R;


public class MyCustomButton extends AppCompatButton {

    private String setFontType;

    public MyCustomButton(Context context) {
        super(context);
    }

    public MyCustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);


    }

    public MyCustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context mContext, AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.MyCustomButton);


        setFontType = typedArray.getString(R.styleable.MyCustomButton_setCustomFontType);

        if (!TextUtils.isEmpty(setFontType))
            setTypeface(Typeface.createFromAsset(mContext.getAssets(), setFontType));

        typedArray.recycle();
    }
}