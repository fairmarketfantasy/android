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

/**
 * Created by bylynka on 6/3/14.
 */
public class TeamsFragment extends BaseActivityFragment {

    private TeamsAdapter _adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_fwc_teams, container, false);
        init();
        return _rootView;
    }

    private void init(){
        _adapter = new TeamsAdapter(getActivity());
        FWCData data = getStorage().getFWCData();
        if(data != null){
            _adapter.setTeams(data.getTeams());
        }
        ListView listView = getViewById(R.id.team_list);
        listView.setAdapter(_adapter);
    }
}
