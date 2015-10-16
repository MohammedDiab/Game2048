package com.diab.game2048;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;


/**
 * Custom view that should overlay the panel to support the animation
 */
public class OverLayView extends FrameLayout {

    public OverLayView(Context context) {
        super(context);
        //init(null, 0);
    }

    public OverLayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //init(attrs, 0);
    }

    public OverLayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //init(attrs, defStyle);
    }





}
