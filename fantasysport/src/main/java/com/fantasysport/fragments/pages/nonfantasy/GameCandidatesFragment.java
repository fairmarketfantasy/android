package com.fantasysport.fragments.pages.nonfantasy;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.fantasysport.R;
import com.fantasysport.activities.IMainActivity;
import com.fantasysport.adapters.nonfantasy.NFCandidateGamesAdapter;
import com.fantasysport.fragments.BaseActivityFragment;
import com.fantasysport.fragments.main.IMainFragment;
import com.fantasysport.fragments.main.nonfantasy.INFMainFragment;
import com.fantasysport.models.nonfantasy.NFData;
import com.fantasysport.models.NFRoster;
import com.fantasysport.models.nonfantasy.NFAutoFillData;
import com.fantasysport.models.nonfantasy.NFGame;
import com.fantasysport.models.nonfantasy.NFTeam;
import uk.co.senab.actionbarpulltorefresh.extras.actionbarcompat.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 5/16/14.
 */

public class GameCandidatesFragment extends BaseActivityFragment implements NFCandidateGamesAdapter.IListener,
        NFMediator.ITeamRemovedListener, OnRefreshListener, NFMediator.IGamesUpdatedListener, NFMediator.IAutoFillDataListener,
        NFMediator.IOnDataUpdatedListener,NFMediator.ISubmittedIndividualPredictionListener,
        NFMediator.ISubmittedPredictionListener{

    private final String SAVED_GAMES = "saved_games";

    private NFCandidateGamesAdapter _adapter;
    private NFMediator _mediator;
    private PullToRefreshLayout _swipeRefreshLayout;
    private TextView _msgLbl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_non_fantasy_candidate_games_page, container, false);
        init();
        return _rootView;
    }

    private void init() {
        IMainFragment fragment = ((IMainActivity) getActivity()).getRootFragment();
        _mediator = (NFMediator) fragment.getMediator();
        _mediator.addTeamRemovedListener(this);
        _mediator.addGamesUpdatedListener(this);
        _mediator.addAutoFillDataListener(this);
        _mediator.addSubmittedIndividualPrediction(this);
        _mediator.addSubmittedPredictions(this);
        View autoFill = getViewById(R.id.autofill_holder);
        _msgLbl = getViewById(R.id.msg_lbl);
        _swipeRefreshLayout = getViewById(R.id.refresh_games_layout);
        initAdapter();
        if (getMainFragment().isEditable()) {
            autoFill.setVisibility(View.VISIBLE);
            _swipeRefreshLayout.setVisibility(View.VISIBLE);
            ActionBarPullToRefresh.from(getActivity())
                    .allChildrenArePullable()
                    .listener(this)
                    .setup(_swipeRefreshLayout);
        } else {
            autoFill.setVisibility(View.GONE);
            _swipeRefreshLayout.setVisibility(View.INVISIBLE);
//            _msgLbl.setText(getFinishedMsg());
        }
        Button autoFillBtn = getViewById(R.id.autofill_btn);
        autoFillBtn.setOnClickListener(_autoFillBtnOnClickListener);
    }


    private void initAdapter() {
        NFData data = getMainFragment().getData();
        List<NFGame> games = data != null ? data.getCandidateGames() : null;
        ListView listView = getViewById(R.id.game_list);
        listView.setCacheColorHint(Color.TRANSPARENT);
        _adapter = new NFCandidateGamesAdapter(getActivity(), games);
        _adapter.setListener(this);
        listView.setAdapter(_adapter);
        checkForEmpty();
    }

    @Override
    public void onSelectedTeam(NFTeam team) {
        _mediator.selectTeam(this, team);
        _adapter.notifyDataSetChanged();
    }

    @Override
    public void onPT(final NFTeam team) {
        _mediator.submittingIndividualPrediction(this, team);
    }


    public String getFinishedMsg(NFRoster roster) {
        String end;
        if (roster.getState() == NFRoster.State.Undefined ||
                roster.getState() == NFRoster.State.Canceled ||
                roster.getContestRank() == null) {
            return "N/A";
        }
        switch (roster.getContestRank()) {
            case 1:
                end = "st";
                break;
            case 2:
                end = "nd";
                break;
            case 3:
                end = "rd";
                break;
            default:
                end = "th";
                break;
        }

        String msg = String.format("YOU TOOK %d%s PLACE\n", roster.getContestRank(), end);
        if (roster.getAmountPaid() == null) {
            return msg;
        }
        if (roster.getAmountPaid() == 0) {
            msg += "DIDN'T WIN THIS TIME";
        } else {
            msg += String.format("AND WON %.2f", roster.getAmountPaid());
        }
        return msg;
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
        if (savedInstanceState == null) {
            return;
        }
        List<NFGame> games = (ArrayList<NFGame>) savedInstanceState.getSerializable(SAVED_GAMES);
        _adapter.setGames(games);
        _adapter.notifyDataSetChanged();
        checkForEmpty();
    }

    @Override
    public void onRemovedTeam(Object sender, NFTeam team) {
        List<NFGame> games = _adapter.getGames();
        if (games == null) {
            return;
        }
        for (NFGame game : games) {
            if (game.getStatsId().compareTo(team.getGameStatsId()) == 0) {
                NFTeam gameTeam = game.getAwayTeam().getStatsId().compareTo(team.getStatsId()) == 0
                        ? game.getAwayTeam()
                        : game.getHomeTeam();
                gameTeam.setIsSelected(false);
                break;
            }
        }
        _adapter.notifyDataSetChanged();
    }


    @Override
    public void onRefreshStarted(View view) {
        _swipeRefreshLayout.setRefreshing(true);
        _mediator.requestUpdateGames(this);
    }

    @Override
    public void onGamesUpdated(Object sender, List<NFGame> games) {
        if(!getMainFragment().isEditable()){
            NFData data = getMainFragment().getData();
            if(data == null){
                return;
            }
            _msgLbl.setText(getFinishedMsg(data.getRoster()));
            return;
        }
        _adapter.setGames(games);
        _adapter.notifyDataSetChanged();
        _swipeRefreshLayout.setRefreshing(false);
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
            _mediator.requestAutoFill(GameCandidatesFragment.this);
        }
    };

    @Override
    public void onAutoFillData(Object sender, NFAutoFillData data) {
        if (_adapter == null || data == null) {
            return;
        }
        List<NFGame> games = data.getCandidateGames();
        _adapter.setGames(games);
        _adapter.notifyDataSetChanged();
       checkForEmpty();
    }

    private void checkForEmpty(){
        NFData data = getMainFragment().getData();
        if(data == null){
            return;
        }
        List<NFGame> games = data.getCandidateGames();
        if(games == null || games.size() == 0){
            _swipeRefreshLayout.setVisibility(View.INVISIBLE);
            _msgLbl.setText(getString(R.string.no_contents_now));
        }else {
            _swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    }

    public INFMainFragment getMainFragment() {
        return (INFMainFragment) ((IMainActivity) getActivity()).getRootFragment();
    }

    @Override
    public void onDataUpdated(Object sender) {
        NFData data = getMainFragment().getData();
        if (getMainFragment().isEditable()) {
            _adapter.setGames(data.getCandidateGames());
            _adapter.notifyDataSetChanged();
            checkForEmpty();
        } else {
            _swipeRefreshLayout.setVisibility(View.INVISIBLE);
            _msgLbl.setText(getFinishedMsg(data.getRoster()));
        }
    }

    @Override
    public void onSubmittedIndividualPrediction(Object sender, NFTeam team) {
        if(_adapter == null){
            return;
        }
        List<NFGame> games = _adapter.getGames();
        if(games !=null && sender != this){
            for (NFGame g : games){
                if(g.getStatsId().compareTo(team.getGameStatsId()) != 0){
                    continue;
                }
                if(g.getHomeTeam().getStatsId().compareTo(team.getStatsId()) == 0){
                    g.getHomeTeam().setIsPredicted(true);
                }else{
                    g.getAwayTeam().setIsPredicted(true);
                }
            }
        }
        _adapter.notifyDataSetChanged();
    }

    @Override
    public void onSubmittedPredictions(Object sender, List<NFTeam> team) {
        if(_adapter == null){
            return;
        }
        List<NFGame> games = _adapter.getGames();
        if(games == null){
           return;
        }
        for (NFGame g : games){
            g.getAwayTeam().setIsSelected(false);
            g.getHomeTeam().setIsSelected(false);
        }
        _adapter.notifyDataSetChanged();
    }
}