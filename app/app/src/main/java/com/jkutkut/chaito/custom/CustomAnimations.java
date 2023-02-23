package com.jkutkut.chaito.custom;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;

import org.jetbrains.annotations.NotNull;

/**
 * Logic to add animations to different views.
 */
public class CustomAnimations {
    /**
     * Changes the background of the given button by the given color when the user touches it.
     * @param btn The button to change its background color.
     * @param color The color to change the background to.
     */
    public static void setBtnClickFeedback(@NotNull Button btn, @ColorInt int color) {
        setBtnClickFeedback(btn, btn, color);
    }

    /**
     * Changes the background of the given button when the given view is touched.
     * @param btn The button to change its background color.
     * @param view The view to listen to touch events.
     * @param color The color to change the background to.
     */
    @SuppressLint("ClickableViewAccessibility")
    public static void setBtnClickFeedback(@NotNull Button btn, @NotNull View view, @ColorInt int color) {
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    btn.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                    btn.invalidate();
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    btn.getBackground().clearColorFilter();
                    btn.invalidate();
                    break;
                }
            }
            return false;
        });
    }

    public static void setEditTextFeedback(@NotNull EditText etxt, @ColorInt int color, @ColorInt int colorFocus) {
        etxt.setOnFocusChangeListener((v, hasFocus) -> {
            etxt.getBackground().setColorFilter(
                (hasFocus)? colorFocus : color,
                PorterDuff.Mode.SRC_ATOP
            );
        });
        etxt.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }
}