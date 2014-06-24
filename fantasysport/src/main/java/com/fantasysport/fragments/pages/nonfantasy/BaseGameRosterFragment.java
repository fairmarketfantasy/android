package com.fantasysport.fragments.pages.nonfantasy;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.fantasysport.R;
import com.fantasysport.activities.IMainActivity;
import com.fantasysport.adapters.nonfantasy.NFRosterAdapter;
import com.fantasysport.fragments.BaseActivityFragment;
import com.fantasysport.fragments.main.IMainFragment;
import com.fantasysport.fragments.main.nonfantasy.INFMainFragment;
import com.fantasysport.models.nonfantasy.*;
import com.fantasysport.webaccess.responseListeners.RequestError;
import com.fantasysport.webaccess.responseListeners.SubmitNFRosterResponseListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by bylynka on 6/24/14.
 */
public abstract class BaseGameRosterFragment extends BaseActivityFragment implements NFRosterAdapter.IListener,
        NFMediator.ITeamSelectedListener, NFMediator.IGamesUpdatedListener, NFMediator.IAutoFillDataListener,
        NFMediator.IOnDataUpdatedListener, NFMediator.ISubmittedIndividualPredictionListener {

    private final String SELECTED_TEAMS = "selected_teams";

    protected NFRosterAdapter _adapter;
    private NFMediator _mediator;
    private Button _submitBtn;
    protected int _layoutId = R.layout.fragment_non_fantasy_game_page;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _rootView = inflater.inflate(_layoutId, container, false);
        init();
        return _rootView;
    }

    protected void init() {
        IMainFragment fragment = ((IMainActivity) getActivity()).getRootFragment();
        _mediator = (NFMediator) fragment.getMediator();
        _mediator.addTeamSelectedListener(this);
        _mediator.addGamesUpdatedListener(this);
        _mediator.addAutoFillDataListener(this);
        _mediator.addOnDataUpdatedListener(this);
        _mediator.addSubmittedIndividualPrediction(this);
        _submitBtn = getViewById(R.id.submit_btn);
        _submitBtn.setOnClickListener(new OnSubmitListener(false));
        initAdapter();
        Button autoFillBtn = getViewById(R.id.autofill_btn);
        autoFillBtn.setOnClickListener(_autoFillBtnOnClickListener);
    }

    protected int getRoomNumber(){
        NFData data = getStorage().getNFDataContainer();
        if(data == null){
            return 5;
        }
        return data.getRoster().getRoomNumber();
    }

    protected void updateSubmitBtnsState() {
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
        listView.setOnItemClickListener(_itemClickListener);
        _adapter = new NFRosterAdapter(getActivity(), getMainFragment().isEditable() && getMainFragment().canSubmit());
        _adapter.setListener(this);
        listView.setAdapter(_adapter);
        setEmptyRoster();
    }

    private void setEmptyRoster() {
        NFData data = getStorage().getNFDataContainer();
        if(data == null){
            return;
        }
        int roomNumber = data.getRoster().getRoomNumber();
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
                updateSubmitBtnsState();
                _mediator.removeTeamFromRoster(this, team);
                return;
            }
        }
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
                updateSubmitBtnsState();
                return;
            }
        }
        showAlert("INFO", "Roster Is Full");
    }

    public void submitRoster(List<NFTeam> teams, boolean isPick) {
        showProgress();
        _submitBtn.setEnabled(false);
        NFData data = getMainFragment().getData();
        if(data != null &&
                data.getRoster() != null &&
                data.getRoster().getId() != null &&
                data.getRoster().getId() > 0){
            getWebProxy().submitNFRoster(teams, data.getRoster().getId(), _submitNFRosterResponseListener);
        }else {
            getWebProxy().submitNFRoster(teams, isPick, _submitNFRosterResponseListener);
        }

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

    SubmitNFRosterResponseListener _submitNFRosterResponseListener = new SubmitNFRosterResponseListener() {
        @Override
        public void onRequestError(RequestError error) {
            dismissProgress();
            showAlert(getString(R.string.error), error.getMessage());
            updateSubmitBtnsState();
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
            updateSubmitBtnsState();
            _mediator.submittedPrediction(BaseGameRosterFragment.this, null);
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
        updateSubmitBtnsState();
    }

    View.OnClickListener _autoFillBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            NFData nfdata = getStorage().getNFDataContainer();
            if(nfdata == null ||
                    nfdata.getCandidateGames() == null ||
                    nfdata.getCandidateGames().size() == 0){
                showAlert("INFO", "There are no games scheduled");
                return;
            }
            _mediator.requestAutoFill(BaseGameRosterFragment.this);
        }
    };

    @Override
    public void onAutoFillData(Object sender, NFAutoFillData data) {
        if(data == null || data.getRosterTeams() == null){
            return;
        }

        NFData nfdata = getStorage().getNFDataContainer();
        int roomNumber = nfdata.getRoster().getRoomNumber();
        List<INFTeam> teams = new ArrayList<INFTeam>(data.getRosterTeams());
        if(teams.size() < roomNumber){
            for (int i = teams.size(); i < roomNumber; i++){
                teams.add(new EmptyNFTeam());
            }
        }
        _adapter.setTeams(teams);
        _adapter.notifyDataSetChanged();
        updateSubmitBtnsState();
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
        updateSubmitBtnsState();
    }

    public INFMainFragment getMainFragment() {
        return (INFMainFragment) ((IMainActivity) getActivity()).getRootFragment();
    }

    @Override
    public void onSubmittedIndividualPrediction(Object sender, NFTeam team) {
        if(_adapter == null){
            return;
        }
        List<INFTeam> teams = _adapter.getTeams();
        if(sender != this && teams != null){
            for (INFTeam t : teams){
                if(t instanceof NFTeam && ((NFTeam)t).getStatsId().compareTo(team.getStatsId()) == 0){
                    ((NFTeam)t).setIsSelected(true);
                }
            }
        }
        _adapter.notifyDataSetChanged();
    }

    AdapterView.OnItemClickListener _itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(_adapter == null ||
                    _adapter.getItem(position) == null ||
                    _adapter.getItem(position) instanceof NFTeam){
                return;
            }
            _mediator.showingGames(BaseGameRosterFragment.this);
        }
    };

    protected class OnSubmitListener implements View.OnClickListener{

        private boolean _isPick;

        public OnSubmitListener(boolean isPick){
            _isPick = isPick;
        }

        @Override
        public void onClick(View v) {
            submitRoster(getTeams(), _isPick);
        }
    }

}