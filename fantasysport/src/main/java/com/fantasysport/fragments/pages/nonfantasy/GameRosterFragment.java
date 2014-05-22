package com.fantasysport.fragments.pages.nonfantasy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import com.fantasysport.R;
import com.fantasysport.activities.MainActivity;
import com.fantasysport.adapters.nonfantasy.*;
import com.fantasysport.fragments.BaseActivityFragment;
import com.fantasysport.fragments.NFMediator;
import com.fantasysport.fragments.main.NonFantasyFragment;
import com.fantasysport.models.nonfantasy.EmptyNFTeam;
import com.fantasysport.models.nonfantasy.INFTeam;
import com.fantasysport.models.nonfantasy.NFGame;
import com.fantasysport.models.nonfantasy.NFTeam;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.requestListeners.SubmitNFRosterResponseListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by bylynka on 5/16/14.
 */
public class GameRosterFragment extends BaseActivityFragment implements NFRosterAdapter.IListener,
        NFMediator.ITeamSelectedListener, NFMediator.IGamesUpdatedListener {

    private final String SELECTED_TEAMS = "selected_teams";

    private NFRosterAdapter _adapter;
    private NFMediator _mediator;
    private Button _submitBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_non_fantasy_game_page, container, false);
        init();
        return _rootView;
    }

    private void init() {
        NonFantasyFragment fragment = (NonFantasyFragment) ((MainActivity) getActivity()).getRootFragment();
        _mediator = fragment.getMediator();
        _mediator.addTeamSelectedListener(this);
        _mediator.addGamesUpdatedListener(this);
        _submitBtn = getViewById(R.id.submit_btn);
        _submitBtn.setOnClickListener(_submitBtnOnClickListener);
        initAdapter();
        updateSubmitBtnState();
    }


    private void updateSubmitBtnState() {
        if (_adapter == null) {
            return;
        }
        List<INFTeam> games = _adapter.getTeams();
        _submitBtn.setEnabled(false);
        if (games == null) {
            return;
        }
        for (INFTeam game : games) {
            if (game instanceof NFTeam) {
                _submitBtn.setEnabled(true);
            }
        }
    }

    private void initAdapter() {
        ListView listView = getViewById(R.id.game_list);
        _adapter = new NFRosterAdapter(getActivity());
        _adapter.setGames(getStorage().getNFDataContainer().getCandidateGames());
        _adapter.setListener(this);
        listView.setAdapter(_adapter);
        setEmptyRoster();
    }

    private void setEmptyRoster() {
        int roomNumber = getStorage().getNFDataContainer().getRoster().getRoomNumber();
        List<INFTeam> games = new ArrayList<INFTeam>(roomNumber);
        for (int i = 0; i < roomNumber; i++) {
            games.add(new EmptyNFTeam());
        }
        _adapter.setTeams(games);
        _adapter.notifyDataSetChanged();
    }

    @Override
    public void onDismiss(NFTeam team) {
        if(_adapter == null){
            return;
        }
        List<INFTeam> teams = _adapter.getTeams();
        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i) == team) {
                teams.set(i, new EmptyNFTeam());
                Collections.sort(teams, _teamComparator);
                _adapter.notifyDataSetChanged();
                updateSubmitBtnState();
                _mediator.removeTeamFromRoster(this, team);
                return;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        List<INFTeam> teams = _adapter.getTeams();
        if(teams == null){
            return;
        }
        outState.putSerializable("SELECTED_TEAMS", new ArrayList<INFTeam>(teams));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState == null){
            return;
        }
        List<INFTeam> teams = (ArrayList<INFTeam>)savedInstanceState.getSerializable(SELECTED_TEAMS);
        if(teams == null){
            return;
        }
        _adapter.setTeams(teams);
        _adapter.notifyDataSetChanged();
    }

    @Override
    public void onSelectedTeam(Object sender, NFTeam team) {
        List<INFTeam> games = _adapter.getTeams();
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i) instanceof EmptyNFTeam) {
                games.set(i, team);
                _adapter.notifyDataSetChanged();
                updateSubmitBtnState();
                return;
            }
        }
    }

    public void submitRoster(List<NFTeam> teams) {
        showProgress();
        _submitBtn.setEnabled(false);
        getWebProxy().submitNFRoster(teams, new SubmitNFRosterResponseListener() {
            @Override
            public void onRequestError(RequestError error) {
                dismissProgress();
                showAlert(getString(R.string.error), error.getMessage());
                updateSubmitBtnState();
            }

            @Override
            public void onRequestSuccess(String responseMsg) {
                dismissProgress();
                showAlert(getString(R.string.info), responseMsg);
                setEmptyRoster();
                updateSubmitBtnState();
            }
        });
    }

    private List<NFTeam> getTeams() {
        List<INFTeam> teamItems = _adapter.getTeams();
        List<NFTeam> teams = new ArrayList<NFTeam>();
        for (INFTeam team : teamItems) {
            if (team instanceof NFTeam) {
                teams.add((NFTeam) team);
            }
        }
        return teams;
    }

    View.OnClickListener _submitBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            submitRoster(getTeams());
        }
    };

    Comparator _teamComparator = new Comparator<INFTeam>() {
        @Override
        public int compare(INFTeam lhs, INFTeam rhs) {
            if(lhs.getClass() == rhs.getClass()){
                return 0;
            }
            return  lhs instanceof EmptyNFTeam ? 1: -1;
        }
    };

    @Override
    public void onGamesUpdated(Object sender, List<NFGame> games) {
        setEmptyRoster();
        _adapter.setGames(games);
    }
}
