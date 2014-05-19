package com.fantasysport.fragments.pages.nonfantasy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.fantasysport.R;
import com.fantasysport.activities.MainActivity;
import com.fantasysport.adapters.nonfantasy.NFCandidateGamesAdapter;
import com.fantasysport.adapters.nonfantasy.NFGameWrapper;
import com.fantasysport.fragments.BaseActivityFragment;
import com.fantasysport.fragments.NFMediator;
import com.fantasysport.fragments.main.NonFantasyFragment;
import com.fantasysport.models.NFGame;

import java.util.List;

/**
 * Created by bylynka on 5/16/14.
 */

public class GameCandidatesFragment extends BaseActivityFragment implements NFCandidateGamesAdapter.IListener {

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
    public void onSelectedGame(NFGameWrapper game) {
        _mediator.selectGame(this, game);
    }
}