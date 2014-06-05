package com.fantasysport.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.fantasysport.R;
import com.fantasysport.models.fantasy.Position;
import com.fantasysport.utility.DeviceInfo;

import java.util.*;

/**
 * Created by bylynka on 4/24/14.
 */
public class ScrollPositionControl extends FrameLayout implements IPositionView {

    private IOnPositionSelectedListener _positionListener;
    private Position _position;
    private ListView _listView;
    private int _firstItem;
    private List<Position> _positions;
    private PositionAdapter _adapter;
    private int _yOffset;
    private MediaPlayer _mp;
    private SoundPool _soundPool;
    private int _soundId;
    private int _maxMediaVolume;
    private float _volume;


    public ScrollPositionControl(Context context) {
        super(context);
        init();
    }

    public ScrollPositionControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScrollPositionControl(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        _soundPool = new SoundPool(7, AudioManager.STREAM_MUSIC, 0);
        _soundId = _soundPool.load(getContext(), R.raw.click2, 1);
        _maxMediaVolume = DeviceInfo.getMaxMediaVolume(getContext());
        _yOffset = (int) dipToPixels(getContext(), 7) * -1;
        initListView();
        addView(_listView);
        setGradient();
    }

    private void initListView() {
        _listView = new ListView(getContext());
        _listView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        _listView.setVerticalScrollBarEnabled(false);
        _listView.setHorizontalScrollBarEnabled(false);
        _listView.setDivider(null);
        _listView.setDividerHeight(0);
        _listView.setBackgroundColor(Color.parseColor("#666666"));
        _listView.setOnScrollListener(_scrollListener);
        _adapter = new PositionAdapter();
        _listView.setAdapter(_adapter);
    }

    private void setGradient() {
        View view = new View(getContext());
        view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Drawable drawable = getContext().getResources().getDrawable(R.drawable.picker_gradient);
        view.setBackgroundDrawable(drawable);
        view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        addView(view);
    }

    @Override
    public void setPosition(String positionAcronym) {
        if (_positions == null) {
            return;
        }
        for (int i = 1; i < _positions.size(); i++) {
            Position position = _positions.get(i);
            if (position != null && position.getAcronym().equalsIgnoreCase(positionAcronym)) {
                    _listView.setSelectionFromTop(i - 1, _yOffset);
                    setPosition(i);
            }
        }
    }

    @Override
    public Position getPosition() {
        return _position;
    }

    @Override
    public void setPositions(List<Position> positions) {
        _position = null;
        _positions = preparePositionList(positions);
        _adapter.notifyDataSetChanged();
        _listView.setSelectionFromTop(0, _yOffset);
        setPosition(1);
    }

    private List<Position> preparePositionList(List<Position> positions){
        if(positions == null){
            return null;
        }
        for(int i = 0; i < positions.size();i++){
            if(positions.get(i) == null){
                positions.remove(i);
            }
        }
        List<Position> newPositions = new ArrayList<Position>();
        for (Position position : positions){
            if(!newPositions.contains(position)){
                newPositions.add(position);
            }
        }
        newPositions.add(null);
        newPositions.add(0,null);
        return newPositions;
    }

    private void raisOnPositionSelected(Position position, int index) {
        if (_positionListener == null) {
            return;
        }
        _positionListener.positionSelected(position, index);
    }

    @Override
    public void setPositionListener(IOnPositionSelectedListener listener) {
        _positionListener = listener;
    }

    private void setPosition(int location) {
        Position position = _positions.get(location);
        if (_position == position) {
            return;
        }
        _position = position;
        raisOnPositionSelected(_position, 0);
    }

    private float getVolume() {
        float curVolume = DeviceInfo.getCurrentMediaVolume(getContext());
        if (_maxMediaVolume == 0) {
            return 0.3f;
        }

        return (0.3f * _maxMediaVolume) / curVolume;
    }

    AbsListView.OnScrollListener _scrollListener = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState != 0) {
                return;
            }
            _listView.setSelectionFromTop(_firstItem, _yOffset);
            setPosition(_firstItem + 1);
        }


        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (_firstItem == firstVisibleItem) {
                return;
            }
            _soundPool.play(_soundId, 0.5f, 0.5f, 0, 0, 1);
            _firstItem = firstVisibleItem;
        }
    };

    class PositionAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return _positions != null ? _positions.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return _positions.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView;
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                textView = (TextView) mInflater.inflate(R.layout.position_item, null);
                textView.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) dipToPixels(getContext(), 27)));
            } else {
                textView = (TextView) convertView;
            }
            Position pos = _positions.get(position);
            String item = pos != null ? pos.getName() : "";
            textView.setText(item);
            return textView;
        }
    }

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }
}