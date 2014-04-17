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
 * Created by bylynka on 4/17/14.
 */
public class ConfirmDialog extends PopupWindow {

    private Context _context;
    private Runnable _yesRunnable;
    private Runnable _noRunnable;
    private TextView _titleLbl;
    private TextView _contentLbl;
    private View _popupView;

    public ConfirmDialog(Context context){
        _context = context;
        init();
    }

    public ConfirmDialog setCancelAction(Runnable cancelAction){
        _noRunnable = cancelAction;
        return this;
    }

    public ConfirmDialog setOkAction(Runnable okAction){
        _yesRunnable = okAction;
        return this;
    }

    public ConfirmDialog setTitle(String title){
        _titleLbl.setText(title);
        return this;
    }

    public ConfirmDialog setContent(String content){
        _contentLbl.setText(content);
        return this;
    }

    private void init(){
        LayoutInflater layoutInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _popupView = layoutInflater.inflate(R.layout.confirm_popup, null);
        Button okBtn = (Button)_popupView.findViewById(R.id.ok_btn);
        Button cancelBtn = (Button)_popupView.findViewById(R.id.cancel_btn);
        okBtn.setOnClickListener(_okBtnOnClickListener);
        cancelBtn.setOnClickListener(_cancelBtnOnClickListener);
        setContentView(_popupView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);
        _titleLbl = (TextView)_popupView.findViewById(R.id.title_lbl);
        _contentLbl = (TextView)_popupView.findViewById(R.id.content_lbl);
    }

    public void show(){
        showAtLocation(_popupView, Gravity.CENTER, 0, 0);
    }

    View.OnClickListener _okBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(_yesRunnable != null){
                _yesRunnable.run();
            }
            dismiss();
        }
    };

    View.OnClickListener _cancelBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(_noRunnable != null){
                _noRunnable.run();
            }
            dismiss();
        }
    };

}
