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
import com.fantasysport.models.fwc.IFWCModel;
import com.fantasysport.models.fwc.Team;

import java.util.List;

/**
 * Created by bylynka on 6/3/14.
 */
public class TeamsFragment extends BaseActivityFragment implements FWCMediator.ISubmittedPredictionListener, TeamsAdapter.IListener {

    private TeamsAdapter _adapter;
    private FWCMediator _mediator;
    private final String _predictionType = "win_the_cup";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_fwc_teams, container, false);
        init();
        return _rootView;
    }

    private void init(){
        IMainFragment fragment = ((IMainActivity) getActivity()).getRootFragment();
        _mediator = (FWCMediator)fragment.getMediator();
        _mediator.addSubmittedPrediction(this);
        _adapter = new TeamsAdapter(getActivity());
        _adapter.setListener(this);
        FWCData data = getStorage().getFWCData();
        if(data != null){
            _adapter.setTeams(data.getTeams());
        }
        ListView listView = getViewById(R.id.team_list);
        listView.setAdapter(_adapter);
    }

    @Override
    public void onSubmittedPrediction(Object sender, IFWCModel predictableItem, String predictionType, String gameStatsId) {
        if(_adapter == null || predictionType == null || predictionType.compareTo(_predictionType) != 0){
            return;
        }
        List<Team> teams = _adapter.getTeams();
        if(teams == null){
            return;
        }
        for (Team team : teams){
            if(team.getStatsId().compareTo(predictableItem.getStatsId()) == 0){
                team.setIsPredicted(predictableItem.isPredicted());
                _adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void onSubmittingTeam(Team team) {
        _mediator.submittingPrediction(TeamsFragment.this, team, _predictionType, null);
    }
}
