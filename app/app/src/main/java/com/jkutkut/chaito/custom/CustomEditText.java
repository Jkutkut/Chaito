package com.jkutkut.chaito.custom;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomEditText extends androidx.appcompat.widget.AppCompatEditText {
    public CustomEditText(@NonNull Context context) {
        super(context);
    }

    public CustomEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setClickFeedback(@ColorInt int color, @ColorInt int colorFocus) {
        CustomAnimations.setEditTextFeedback(this, color, colorFocus);
    }
}
