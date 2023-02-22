package com.jkutkut.chaito.custom;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

/**
 * My own version of a button.
 */
public class CustomButton extends AppCompatButton {
    public CustomButton(@NonNull Context context) {
        super(context);
    }

    public CustomButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Set's an animation to the button's background using ${@link CustomAnimations}.
     * @param color The color to animate to.
     */
    public void setClickFeedback(int color) {
        CustomAnimations.setBtnClickFeedback(this, color);
    }
}