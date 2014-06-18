package com.fantasysport.fragments.pages.nonfantasy.footballwoldcup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.fantasysport.R;
import com.fantasysport.activities.IMainActivity;
import com.fantasysport.adapters.footballwoldcup.GamesAdapter;
import com.fantasysport.fragments.BaseActivityFragment;
import com.fantasysport.fragments.main.IMainFragment;
import com.fantasysport.models.fwc.FWCData;
import com.fantasysport.models.fwc.Game;
import com.fantasysport.models.fwc.IFWCModel;
import com.fantasysport.models.fwc.Team;

import java.util.List;

/**
 * Created by bylynka on 6/3/14.
 */
public class GamesFragment extends BaseActivityFragment implements FWCMediator.ISubmittedPredictionListener, GamesAdapter.IListener {

    private GamesAdapter _adapter;
    private FWCMediator _mediator;
    private final String _predictionType = "daily_wins";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_fwc_games, container, false);
        init();
        return _rootView;
    }

    private void init(){
        IMainFragment fragment = ((IMainActivity) getActivity()).getRootFragment();
        _mediator = (FWCMediator)fragment.getMediator();
        _mediator.addSubmittedPrediction(this);
        _adapter = new GamesAdapter(getActivity());
        _adapter.addListener(this);
        FWCData data = getStorage().getFWCData();
        if(data != null){
            _adapter.setGames(data.getGames());
        }
        ListView listView = getViewById(R.id.game_list);
        listView.setAdapter(_adapter);
    }


    @Override
    public void onSubmittedPrediction(Object sender, IFWCModel predictableItem, String predictionType, String gameStatsId) {
        if(_adapter == null || predictionType == null || !predictionType.equalsIgnoreCase(_predictionType)){
            return;
        }
        List<Game> games = _adapter.getGames();
        if(games == null){
            return;
        }
        for (Game game : games){
            if(game.getStatsId().compareTo(gameStatsId) != 0){
               continue;
            }
            if(game.getHomeTeam().getStatsId().compareTo(predictableItem.getStatsId()) == 0){
                game.getHomeTeam().setIsPredicted(predictableItem.isPredicted());
            }else {
                game.getAwayTeam().setIsPredicted(predictableItem.isPredicted());
            }
            _adapter.notifyDataSetChanged();
            break;
        }
        if(getStorage() == null || getStorage().getFWCData() == null){
            return;
        }
        getStorage().getFWCData().setGames(games);
        _mediator.updatingData(GamesFragment.this, getStorage().getFWCData());
    }

    @Override
    public void onSubmittingTeam(Team team) {
        _mediator.submittingPrediction(GamesFragment.this, team, _predictionType, team.getGameStatsId());
    }
}
