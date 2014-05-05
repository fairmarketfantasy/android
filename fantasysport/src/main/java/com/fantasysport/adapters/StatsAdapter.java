package com.fantasysport.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.fantasysport.R;
import com.fantasysport.models.Player;
import com.fantasysport.models.StatsItem;
import com.fantasysport.utility.StringHelper;
import com.fantasysport.views.ConfirmDialog;

import java.util.List;

/**
 * Created by bylynka on 3/5/14.
 */
public class StatsAdapter extends BaseAdapter {

    private List<StatsItem> _items;
    private Context _context;
    private Player _player;
    private Typeface _prohibitionRoundTypeFace;
    private ISubmitListener _submitListener;

    public StatsAdapter(Context context, Player player) {
        _context = context;
        _player = player;
        _prohibitionRoundTypeFace = Typeface.createFromAsset(_context.getAssets(), "fonts/ProhibitionRound.ttf");
    }

    public void setSubmitListener(ISubmitListener listener){
        _submitListener = listener;
    }

    public void setItems(List<StatsItem> items) {
        _items = items;
    }

    public List<StatsItem> getItems() {
        return _items;
    }

    @Override
    public int getCount() {
        return _items != null ? _items.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return _items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.stats_item, null);
            Button moreBtn = (Button) convertView.findViewById(R.id.more_btn);
            moreBtn.setTypeface(_prohibitionRoundTypeFace);
            Button lessBtn = (Button) convertView.findViewById(R.id.less_btn);
            lessBtn.setTypeface(_prohibitionRoundTypeFace);
        }
        StatsItem item = _items.get(position);
        setPredictionView(convertView, item);
        return convertView;
    }

    private void raiseSubmitListener(){
        if(_submitListener == null){
            return;
        }
        _submitListener.onSubmit();
    }

    private void setPredictionView(View view, StatsItem item) {
        TextView headerLbl = (TextView) view.findViewById(R.id.header_lbl);

        headerLbl.setText(String.format("%s: %.2f", StringHelper.capitalizeFirstLetter(item.getName()), item.getValue()));
        View predictionBlock = view.findViewById(R.id.prediction_block);
        predictionBlock.setVisibility(View.INVISIBLE);
        View cancelBtn = view.findViewById(R.id.cancel_btn);
        Button moreBtn = (Button) view.findViewById(R.id.more_btn);
        Button lessBtn = (Button) view.findViewById(R.id.less_btn);


        setBtn(lessBtn, new ModeOnClickListener(StatsItem.LESS_MODE, item, view), false, true);
        setBtn(moreBtn, new ModeOnClickListener(StatsItem.MORE_MODE, item, view), false, true);
        if (item.getBidLess()) {
            setBtn(lessBtn, null, true, false);
        }
        if (item.getBidMore()) {
            setBtn(moreBtn, null, true, false);
        }
    }

    private void setBtn(Button btn, View.OnClickListener listener, boolean isSelected, boolean isEnabled) {
        btn.setOnClickListener(listener);
        btn.setSelected(isSelected);
        btn.setEnabled(isEnabled);
    }

    class ModeOnClickListener implements View.OnClickListener {

        private String _mode;
        private StatsItem _item;
        private View _itemView;

        public ModeOnClickListener(String mode, StatsItem item, View itemView) {
            _mode = mode;
            _item = item;
            _itemView = itemView;
        }

        private void updateView(View v) {
            _item.setMode(_mode);
            setPredictionView(_itemView, _item);
            v.setSelected(true);
        }

        @Override
        public void onClick(final View v) {
            if (_mode.equalsIgnoreCase(StatsItem.DEFAULT_MODE)) {
                updateView(v);
                return;
            }

            new ConfirmDialog(_context)
                    .setTitle(StatsAdapter.this._player.getName())
                    .setContent(String.format("%s than %.2f %s?", (_mode.equalsIgnoreCase(StatsItem.LESS_MODE) ? "Less" : "More"), _item.getValue(), _item.getName()))
                    .setOkAction(new Runnable() {
                        @Override
                        public void run() {
                            if (_mode.equalsIgnoreCase(StatsItem.LESS_MODE)) {
                                _item.setBidLess(true);
                            } else {
                                _item.setBidMore(true);
                            }
                            updateView(v);
                            raiseSubmitListener();
                        }
                    })
                    .show();
        }
    }

    public interface ISubmitListener{
        public void onSubmit();
    }
}
