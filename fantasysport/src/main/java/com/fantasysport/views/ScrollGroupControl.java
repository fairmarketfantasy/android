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
import com.fantasysport.models.fwc.Group;
import com.fantasysport.utility.DeviceInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 6/3/14.
 */
public class ScrollGroupControl extends FrameLayout {

    private IOnGroupSelectedListener _groupListener;
    private Group _group;
    private ListView _listView;
    private int _firstItem;
    private List<Group> _groups;
    private GroupAdapter _adapter;
    private int _yOffset;
    private MediaPlayer _mp;
    private SoundPool _soundPool;
    private int _soundId;
    private int _maxMediaVolume;
    private float _volume;


    public ScrollGroupControl(Context context) {
        super(context);
        init();
    }

    public ScrollGroupControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScrollGroupControl(Context context, AttributeSet attrs, int defStyle) {
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
        _adapter = new GroupAdapter();
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


    public Group getGroup() {
        return _group;
    }

    public void setGroups(List<Group> groups) {
        _group = null;
        _groups = prepareGroupList(groups);
        _adapter.notifyDataSetChanged();
        _listView.setSelectionFromTop(0, _yOffset);
        setGroup(1);
    }

    private List<Group> prepareGroupList(List<Group> groups){
        if(groups == null){
            return null;
        }
        for(int i = 0; i < groups.size();i++){
            if(groups.get(i) == null){
                groups.remove(i);
            }
        }
        List<Group> newGroups = new ArrayList<Group>(groups);

        newGroups.add(null);
        newGroups.add(0,null);
        return newGroups;
    }

    private void raiseOnGroupsSelected(Group group, int index) {
        if (_groupListener == null) {
            return;
        }
        _groupListener.groupSelected(group, index);
    }

    public void setGroupListener(IOnGroupSelectedListener listener) {
        _groupListener = listener;
    }

    private void setGroup(int location) {
        Group group = _groups.get(location);
        if (_group == group) {
            return;
        }
        _group = group;
        raiseOnGroupsSelected(_group, 0);
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
            setGroup(_firstItem + 1);
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

    class GroupAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return _groups != null ? _groups.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return _groups.get(position);
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
            Group pos = _groups.get(position);
            String item = pos != null ? pos.getName() : "";
            textView.setText(item);
            return textView;
        }
    }

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public interface IOnGroupSelectedListener {
        public void groupSelected(Group position, int index);
    }
}