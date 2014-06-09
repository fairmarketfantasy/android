package com.fantasysport.fragments.pages.footballwoldcup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.fantasysport.R;
import com.fantasysport.activities.IMainActivity;
import com.fantasysport.adapters.footballwoldcup.TeamsAdapter;
import com.fantasysport.fragments.BaseActivityFragment;
import com.fantasysport.fragments.main.IMainFragment;
import com.fantasysport.models.fwc.FWCData;
import com.fantasysport.models.fwc.Group;
import com.fantasysport.models.fwc.IFWCModel;
import com.fantasysport.models.fwc.Team;
import com.fantasysport.views.ScrollGroupControl;

import java.util.List;

/**
 * Created by bylynka on 6/3/14.
 */
public class GroupsFragment extends BaseActivityFragment implements ScrollGroupControl.IOnGroupSelectedListener,
        FWCMediator.ISubmittedPredictionListener, TeamsAdapter.IListener{

    private ScrollGroupControl _groupsControl;
    private TeamsAdapter _adapter;
    private FWCMediator _mediator;
    private final String _predictionType = "win_groups";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_fwc_groups, container, false);
        init();
        return _rootView;
    }

    private void init(){
        IMainFragment fragment = ((IMainActivity) getActivity()).getRootFragment();
        _mediator = (FWCMediator)fragment.getMediator();
        _mediator.addSubmittedPrediction(this);
        _adapter = new TeamsAdapter(getActivity());
        _adapter.setListener(this);
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

    @Override
    public void onSubmittedPrediction(Object sender, IFWCModel predictableItem, String predictionType, String gameStatsId) {
        if(_adapter == null || predictionType == null || predictionType.compareTo(_predictionType) != 0){
            return;
        }
        List<Team> teams = _adapter.getTeams();
        for (Team team : teams){
            if(team.getStatsId().compareTo(predictableItem.getStatsId()) == 0){
                team.setIsPredicted(predictableItem.isPredicted());
                _adapter.notifyDataSetChanged();
                break;
            }
        }
        if(getStorage() == null || getStorage().getFWCData() == null ||
                getStorage().getFWCData().getGroups() == null ||
                _groupsControl == null ||
                _groupsControl.getGroup() == null){
            return;
        }
        _mediator.updatingData(GroupsFragment.this, getStorage().getFWCData());
    }

    @Override
    public void onSubmittingTeam(Team team) {
        _mediator.submittingPrediction(GroupsFragment.this, team, _predictionType, null);
    }
}
