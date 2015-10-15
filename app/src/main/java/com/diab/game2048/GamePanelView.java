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
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * TODO: document your custom view class.
 */
public class GamePanelView extends GridLayout {

    Context ctx;
    public GamePanelView(Context context) {
        super(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int cardWidth = (w-10)/4;
        int cardHeight = (h-10)/4;
        for (int row = 0 ; row <16 ; row++) {
            TileView tmp = new TileView(ctx);
            //tmp.setTextColor(ctx.getResources().getColor(R.color.colorAccent));
            addView(tmp,cardWidth,cardHeight);
        }
    }

    public GamePanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx = context;
        setColumnCount(4);
        setBackgroundColor(context.getResources().getColor(R.color.colorSecondaryBackground));

    }

    public GamePanelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
