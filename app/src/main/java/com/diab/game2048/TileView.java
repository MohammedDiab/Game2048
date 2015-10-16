package com.diab.game2048;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.test.RenamingDelegatingContext;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * Custom view to represent the tile
 */
public class TileView extends LinearLayout {


    /**
     *  the Textview that should hold the tile number
     */
    private TextView lblTileNumber;

    /**
     * the tile background layout
     */
    private LinearLayout lnrBackground;

    /**
     * the tile number
     */
    private int number;

    /**
     * the string that appears in the tile box
     */
    private String displayText;

    /**
     * copy from the context
     */
    private Context ctx;

    public TileView(Context context) {
        super(context);
        init(context);
    }

    public TileView(Context context, int number, int backgroundColor) {
        super(context);
        init(context);
        setNum(number);
        lblTileNumber.setBackgroundColor(backgroundColor);
    }

    private void init(Context context) {
        inflate(context, R.layout.tile_view, this);
        lblTileNumber = (TextView) findViewById(R.id.lblTileNumber);
        lnrBackground = (LinearLayout) findViewById(R.id.lnrBackground);
        ctx = context;
    }

    /**
     * set the tile number and change the colors based on the number
     * @param tileNumber
     */
    public void setNum(int tileNumber) {
        if (tileNumber == 0) {
            number = 0;
            displayText = "";
            lblTileNumber.setText(displayText);
            lblTileNumber.setVisibility(INVISIBLE);
        } else {
            number = tileNumber;
            lblTileNumber.setVisibility(VISIBLE);
            displayText = String.valueOf(number);
            lblTileNumber.setText(displayText);
            switch (tileNumber) {
                case 2:
                    lblTileNumber.setTextColor(0xff000000);
                    setCompatiableBackground(R.drawable.rounded_tile_2);
                    break;
                case 4:
                    lblTileNumber.setTextColor(0xff000000);
                    setCompatiableBackground(R.drawable.rounded_tile_4);

                    break;
                case 8:
                    lblTileNumber.setTextColor(0xffFFFFFF);
                    setCompatiableBackground(R.drawable.rounded_tile_8);
                    //lblTileNumber.setBackgroundColor(0xffF8BBD0);
                    break;
                case 16:
                    lblTileNumber.setTextColor(0xffFFFFFF);
                    setCompatiableBackground(R.drawable.rounded_tile_16);
                    //lblTileNumber.setBackgroundColor(0xff795548);
                    break;
                case 32:
                    lblTileNumber.setTextColor(0xffFFFFFF);
                    setCompatiableBackground(R.drawable.rounded_tile_32);
                    break;
                case 64:
                    lblTileNumber.setTextColor(0xffFFFFFF);
                    setCompatiableBackground(R.drawable.rounded_tile_64);
                    break;
                case 128:
                    lblTileNumber.setTextColor(0xffFFFFFF);
                    setCompatiableBackground(R.drawable.rounded_tile_128);
                    break;
                case 256:
                    lblTileNumber.setTextColor(0xff000000);
                    setCompatiableBackground(R.drawable.rounded_tile_256);
                    break;
                case 512:
                    lblTileNumber.setTextColor(0xffFFFFFF);
                    setCompatiableBackground(R.drawable.rounded_tile_512);
                    break;
                case 1024:
                    lblTileNumber.setTextColor(0xffFFFFFF);
                    setCompatiableBackground(R.drawable.rounded_tile_1024);
                    break;
                case 2048:
                    lblTileNumber.setTextColor(0xffFFFFFF);
                    setCompatiableBackground(R.drawable.rounded_tile_2048);
                    break;
                default:
                    lblTileNumber.setTextColor(0xffFFFFFF);
                    setCompatiableBackground(R.drawable.rounded_tile_2048);
                    break;
            }
        }
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setCompatiableBackground(int drawableId) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            lblTileNumber.setBackgroundDrawable(getResources().getDrawable(drawableId));
        } else {
            lblTileNumber.setBackground(getResources().getDrawable(drawableId));
        }
    }

    public int getNum() {
        return number;
    }

    public TextView getTextView() {
        return lblTileNumber;
    }


}
