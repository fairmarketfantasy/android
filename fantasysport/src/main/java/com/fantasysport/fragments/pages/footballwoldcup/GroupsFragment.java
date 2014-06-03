package com.fantasysport.fragments.pages.footballwoldcup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.fantasysport.R;
import com.fantasysport.adapters.footballwoldcup.TeamsAdapter;
import com.fantasysport.fragments.BaseActivityFragment;
import com.fantasysport.models.fwc.FWCData;
import com.fantasysport.models.fwc.Group;
import com.fantasysport.views.ScrollGroupControl;

/**
 * Created by bylynka on 6/3/14.
 */
public class GroupsFragment extends BaseActivityFragment implements ScrollGroupControl.IOnGroupSelectedListener {

    private ScrollGroupControl _groupsControl;
    private TeamsAdapter _adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_fwc_groups, container, false);
        init();
        return _rootView;
    }

    private void init(){
        _adapter = new TeamsAdapter(getActivity());
        ListView listView = getViewById(R.id.team_list);
        listView.setAdapter(_adapter);
        _groupsControl = getViewById(R.id.group_view);
        _groupsControl.setGroupListener(this);
        FWCData data = getStorage().getFWCData();
        if(data != null){
            _groupsControl.setGroups(data.getGroups());
        }

    }

    @Override
    public void groupSelected(Group group, int index) {
        if(group == null || _adapter == null){
            return;
        }
        _adapter.setTeams(group.getTeams());
        _adapter.notifyDataSetChanged();
    }
}
