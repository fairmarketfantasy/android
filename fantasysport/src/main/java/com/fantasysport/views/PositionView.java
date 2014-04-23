package com.fantasysport.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import com.fantasysport.R;
import com.fantasysport.models.Position;

import java.util.List;

/**
 * Created by bylynka on 3/17/14.
 */
public class PositionView extends LinearLayout {

    private OnPositionSelecteListener _positionListener;
    private Position _position;

    public PositionView(Context context) {
        super(context);
        init();
    }

    public PositionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setPositionListener(OnPositionSelecteListener listener){
        _positionListener = listener;
    }

    public void setPositions(List<Position> positions){
        if(positions == null || positions.size() == 0){
            return;
        }
        removeAllViews();
        for (int i = 0; i < positions.size(); i++){
            Position pos = positions.get(i);
            ButtonGravity gravity = ButtonGravity.Center;
            if(i == 0){
                gravity = ButtonGravity.Left;
            }else if(i == positions.size() - 1){
                gravity = ButtonGravity.Right;
            }
            addPositionButton(pos, gravity);
        }
        _position = positions.get(0);
        getChildAt(0).setSelected(true);
    }

    private void addPositionButton(Position position, ButtonGravity gravity){
        Button btn = new Button(getContext());
        LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, getResources().getDimensionPixelOffset(R.dimen.btn_height), 1);
        params.setMargins(1,0,1,0);
        btn.setLayoutParams(params);
        btn.setText(position.getAcronym());
        btn.setTag(position);
        btn.setOnClickListener(_clickBtnListener);
        btn.setPadding(0,0,0,0);
        Drawable drawable = getResources().getDrawable(R.drawable.middle_position_btn);
        switch (gravity){
            case Left:
                drawable = getResources().getDrawable(R.drawable.left_position_btn);
                break;
            case Right:
                drawable = getResources().getDrawable(R.drawable.right_position_button);
                break;
            default:
        }
        btn.setTextColor(Color.WHITE);
        btn.setTextSize(10);
        btn.setBackgroundDrawable(drawable);
        addView(btn);
    }

    private void raisOnPositionSelected(Position position){
        if(_positionListener == null){
            return;
        }
        _positionListener.positionSelected(position);
    }

    private void init(){
        setOrientation(HORIZONTAL);
        setBackgroundColor(Color.parseColor("#FFFFFF"));
        int paddingVer = getResources().getDimensionPixelOffset(R.dimen.side_hor_marging);
        int paddingHor = getResources().getDimensionPixelOffset(R.dimen.side_marging)/2;
        setPadding(paddingHor, paddingVer, paddingHor, paddingVer);
    }

    OnClickListener _clickBtnListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < getChildCount(); i++){
                getChildAt(i).setSelected(false);
            }
            v.setSelected(true);
            _position = (Position)v.getTag();
            raisOnPositionSelected(_position);
        }
    };

    public Position getPosition() {
        return _position;
    }

    public void setPosition(String position) {
        for (int i = 0; i < getChildCount(); i++){
           View v = getChildAt(i);
            if(((Position)v.getTag()).getAcronym().equalsIgnoreCase(position)){
                _clickBtnListener.onClick(v);
            }
        }
    }

    public interface OnPositionSelecteListener{
        public void positionSelected(Position position);
    }

    public enum ButtonGravity{
        Left,
        Center,
        Right
    }

}
