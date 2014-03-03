package com.fantasysport.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;
import com.fantasysport.models.Player;

/**
 * Created by bylynka on 3/3/14.
 */
public class PlayerProgressTextView extends TextView {

    public PlayerProgressTextView(Context context) {
        super(context);
    }

    public PlayerProgressTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayerProgressTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setPlayer(Player player){
        double progress = player.getSellPrice() - player.getPurchasePrice();
        int color = Color.parseColor("#51B24B");
        String textValue = null;
        if(progress < 0){
            color = Color.parseColor("#E74B3C");
            textValue = String.format(" %.0f", progress);
        }else {
            textValue = String.format(" +%.0f", progress);
        }
        setTextColor(color);
        setText(textValue);
    }
}
