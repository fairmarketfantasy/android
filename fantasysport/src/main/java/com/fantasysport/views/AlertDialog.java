package com.fantasysport.views;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.fantasysport.R;

/**
 * Created by bylynka on 4/18/14.
 */
public class AlertDialog extends PopupWindow{

    private Context _context;
    private TextView _titleLbl;
    private TextView _contentLbl;
    private View _popupView;

    public AlertDialog(Context context){
        super(context);
        _context = context;
        init();
    }

    public AlertDialog setTitle(String title){
        _titleLbl.setText(title);
        return this;
    }

    public AlertDialog setContent(String content){
        _contentLbl.setText(content);
        return this;
    }

    public void show(){
        showAtLocation(_popupView, Gravity.CENTER, 0, 0);
    }

    private void init(){
        LayoutInflater layoutInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _popupView = layoutInflater.inflate(R.layout.popup_alert, null);
        Button okBtn = (Button)_popupView.findViewById(R.id.ok_btn);
        okBtn.setOnClickListener(_okBtnOnClickListener);
        setContentView(_popupView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);
        _titleLbl = (TextView)_popupView.findViewById(R.id.title_lbl);
        _contentLbl = (TextView)_popupView.findViewById(R.id.content_lbl);
    }

    View.OnClickListener _okBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

}