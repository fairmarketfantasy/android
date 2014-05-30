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
import com.fantasysport.fragments.main.INFMainFragment;
import com.fantasysport.models.NFData;
import com.fantasysport.models.NFRoster;
import com.fantasysport.models.nonfantasy.NFAutoFillData;
import com.fantasysport.models.nonfantasy.NFGame;
import com.fantasysport.models.nonfantasy.NFTeam;
import com.fantasysport.views.ConfirmDialog;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.requestListeners.StringResponseListener;
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
        NFMediator.IOnDataUpdatedListener,NFMediator.ISubmitIndividualPredictionListener {

    private final String SAVED_GAMES = "saved_games";

    private NFCandidateGamesAdapter _adapter;
    private NFMediator _mediator;
    private PullToRefreshLayout _swipeRefreshLayout;

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
        _mediator.addSubmitIndividualPrediction(this);
        View autoFill = getViewById(R.id.autofill_holder);
        _swipeRefreshLayout = getViewById(R.id.refresh_games_layout);
        if (getMainFragment().isEditable()) {
            autoFill.setVisibility(View.VISIBLE);
            _swipeRefreshLayout.setVisibility(View.VISIBLE);
            ActionBarPullToRefresh.from(getActivity())
                    .allChildrenArePullable()
                    .listener(this)
                    .setup(_swipeRefreshLayout);
        } else {
            autoFill.setVisibility(View.GONE);
            _swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
        Button autoFillBtn = getViewById(R.id.autofill_btn);
        autoFillBtn.setOnClickListener(_autoFillBtnOnClickListener);
        initAdapter();
    }


    private void initAdapter() {
        NFData data = getMainFragment().getData();
        List<NFGame> games = data != null ? data.getCandidateGames() : null;
        ListView listView = getViewById(R.id.game_list);
        listView.setCacheColorHint(Color.TRANSPARENT);
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
    public void onPT(final NFTeam team) {
        team.setIsPredicted(true);
        _mediator.submitIndividualPrediction(this, team);
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
        if (games == null) {
            return;
        }
        _adapter.setGames(games);
        _adapter.notifyDataSetChanged();
    }

    @Override
    public void onRemovedTeam(Object sender, NFTeam team) {
        List<NFGame> games = _adapter.getGames();
        if (games == null) {
            return;
        }
        for (NFGame game : games) {
            if (game.getStatsId() == team.getGameStatsId()) {
                NFTeam gameTeam = game.getAwayTeam().getStatsId() == team.getStatsId()
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
            return;
        }
        _adapter.setGames(games);
        _adapter.notifyDataSetChanged();
        _swipeRefreshLayout.setRefreshing(false);
    }

    View.OnClickListener _autoFillBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            _mediator.requestAutoFill(GameCandidatesFragment.this);
        }
    };

    @Override
    public void onAutoFillData(Object sender, NFAutoFillData data) {
        if (_adapter == null || data == null || data.getCandidateGames() == null) {
            return;
        }
        _adapter.setGames(data.getCandidateGames());
        _adapter.notifyDataSetChanged();
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
        } else {
            TextView msgLbl = getViewById(R.id.msg_lbl);
            msgLbl.setText(getFinishedMsg(data.getRoster()));
        }
    }

    @Override
    public void onSubmitIndividualPrediction(Object sender, NFTeam team) {
        if(_adapter == null){
            return;
        }
        List<NFGame> games = _adapter.getGames();
        if(games !=null && sender != this){
            for (NFGame g : games){
                if(g.getStatsId() != team.getGameStatsId()){
                    continue;
                }
                if(g.getHomeTeam().getStatsId() == team.getStatsId()){
                    g.getHomeTeam().setIsPredicted(true);
                }else{
                    g.getAwayTeam().setIsPredicted(true);
                }
            }
        }
        _adapter.notifyDataSetChanged();
    }
}