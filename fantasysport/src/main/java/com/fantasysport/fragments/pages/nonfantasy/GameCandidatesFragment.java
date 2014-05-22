package com.fantasysport.fragments.pages.nonfantasy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.fantasysport.R;
import com.fantasysport.activities.MainActivity;
import com.fantasysport.adapters.nonfantasy.NFCandidateGamesAdapter;
import com.fantasysport.fragments.BaseActivityFragment;
import com.fantasysport.fragments.NFMediator;
import com.fantasysport.fragments.main.NonFantasyFragment;
import com.fantasysport.models.nonfantasy.NFGame;
import com.fantasysport.models.nonfantasy.NFTeam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 5/16/14.
 */

public class GameCandidatesFragment extends BaseActivityFragment implements NFCandidateGamesAdapter.IListener,
        NFMediator.ITeamRemovedListener {

    private final String SAVED_GAMES = "saved_games";

    private NFCandidateGamesAdapter _adapter;
    private NFMediator _mediator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_non_fantasy_candidate_games_page, container, false);
        init();
        return _rootView;
    }

    private void init(){
        NonFantasyFragment fragment = (NonFantasyFragment)((MainActivity) getActivity()).getRootFragment();
        _mediator = fragment.getMediator();
        _mediator.addTeamRemovedListener(this);
        initAdapter();
    }

    private void initAdapter(){
        List<NFGame> games = getStorage().getNFDataContainer().getCandidateGames();
        ListView listView = getViewById(R.id.game_list);
        _adapter = new NFCandidateGamesAdapter(getActivity(), games);
        _adapter.setListener(this);
        listView.setAdapter(_adapter);
    }

    @Override
    public void onSelectedTeam(NFTeam team) {
        _mediator.selectTeam(this, team);
        _adapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       List<NFGame> games = _adapter.getGames();
       outState.putSerializable(SAVED_GAMES, new ArrayList<NFGame>(games));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState == null){
            return;
        }
        List<NFGame> games =  (ArrayList<NFGame>)savedInstanceState.getSerializable(SAVED_GAMES);
        if(games == null){
            return;
        }
        _adapter.setGames(games);
        _adapter.notifyDataSetChanged();
    }

    @Override
    public void onRemovedTeam(Object sender, NFTeam team) {
       List<NFGame> games = _adapter.getGames();
        if(games == null){
            return;
        }
        for (NFGame game : games){
            if(game.getStatsId() == team.getGameStatsId()){
                NFTeam gameTeam = game.getAwayTeam().getStatsId() == team.getStatsId()
                        ? game.getAwayTeam()
                        : game.getHomeTeam();
                gameTeam.setIsSelected(false);
                break;
            }
        }
        _adapter.notifyDataSetChanged();
    }
}