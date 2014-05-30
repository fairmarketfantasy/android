package com.fantasysport.fragments.pages.nonfantasy;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import com.fantasysport.R;
import com.fantasysport.activities.IMainActivity;
import com.fantasysport.adapters.nonfantasy.*;
import com.fantasysport.fragments.BaseActivityFragment;
import com.fantasysport.fragments.main.IMainFragment;
import com.fantasysport.fragments.main.INFMainFragment;
import com.fantasysport.models.NFData;
import com.fantasysport.models.nonfantasy.*;
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
        NFMediator.ITeamSelectedListener, NFMediator.IGamesUpdatedListener, NFMediator.IAutoFillDataListener,
        NFMediator.IOnDataUpdatedListener, NFMediator.ISubmitIndividualPredictionListener {

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
        IMainFragment fragment = ((IMainActivity) getActivity()).getRootFragment();
        _mediator = (NFMediator) fragment.getMediator();
        _mediator.addTeamSelectedListener(this);
        _mediator.addGamesUpdatedListener(this);
        _mediator.addAutoFillDataListener(this);
        _mediator.addOnDataUpdatedListener(this);
        _mediator.addSubmitIndividualPrediction(this);
        View autoFill = getViewById(R.id.autofill_holder);
        View bottomBar = getViewById(R.id.bottom_bar);
        if(getMainFragment().isEditable()){
            autoFill.setVisibility(View.VISIBLE);
            bottomBar.setVisibility(View.VISIBLE);
        }else {
            autoFill.setVisibility(View.GONE);
            bottomBar.setVisibility(View.GONE);
        }
        _submitBtn = getViewById(R.id.submit_btn);
        _submitBtn.setOnClickListener(_submitBtnOnClickListener);
        initAdapter();
        Button autoFillBtn = getViewById(R.id.autofill_btn);
        autoFillBtn.setOnClickListener(_autoFillBtnOnClickListener);
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
        listView.setCacheColorHint(Color.TRANSPARENT);
        _adapter = new NFRosterAdapter(getActivity(), getMainFragment().isEditable());
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
        if (_adapter == null) {
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
    public void onPT(NFTeam team) {
        _mediator.submitIndividualPrediction(this, team);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        List<INFTeam> teams = _adapter.getTeams();
        if (teams == null) {
            return;
        }
        outState.putSerializable("SELECTED_TEAMS", new ArrayList<INFTeam>(teams));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            return;
        }
        List<INFTeam> teams = (ArrayList<INFTeam>) savedInstanceState.getSerializable(SELECTED_TEAMS);
        if (teams == null) {
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
        showAlert("INFO", "Roster Is Full");
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
                if(getMainFragment().isPredicted()){
                    getActivity().finish();
                    return;
                }
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
            if (lhs.getClass() == rhs.getClass()) {
                return 0;
            }
            return lhs instanceof EmptyNFTeam ? 1 : -1;
        }
    };

    @Override
    public void onGamesUpdated(Object sender, List<NFGame> games) {
        setEmptyRoster();
    }

    View.OnClickListener _autoFillBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _mediator.requestAutoFill(GameRosterFragment.this);
        }
    };

    @Override
    public void onAutoFillData(Object sender, NFAutoFillData data) {
        _adapter.setTeams(new ArrayList<INFTeam>(data.getRosterTeams()));
        _adapter.notifyDataSetChanged();
        updateSubmitBtnState();
    }

    @Override
    public void onDataUpdated(Object sender) {
        NFData data = getMainFragment().getData();
        List<INFTeam> teams = new ArrayList<INFTeam>();
        List<NFTeam> rosterTeams = data.getRoster().getTeams();
        for (int i = 0; i < data.getRoster().getRoomNumber(); i++){
            if(rosterTeams.size() > i){
                teams.add(rosterTeams.get(i));
            }else {
                teams.add(new EmptyNFTeam());
            }
        }
        _adapter.setTeams(teams);
        _adapter.notifyDataSetChanged();
        updateSubmitBtnState();
    }

    public INFMainFragment getMainFragment() {
        return (INFMainFragment) ((IMainActivity) getActivity()).getRootFragment();
    }

    @Override
    public void onSubmitIndividualPrediction(Object sender, NFTeam team) {
        if(_adapter == null){
            return;
        }
        List<INFTeam> teams = _adapter.getTeams();
        if(sender != this && teams != null){
            for (INFTeam t : teams){
                if(t instanceof NFTeam && ((NFTeam)t).getStatsId() == team.getStatsId()){
                    ((NFTeam)t).setIsSelected(true);
                }
            }
        }
        _adapter.notifyDataSetChanged();
    }
}
