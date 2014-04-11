package com.fantasysport.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.fantasysport.R;
import com.fantasysport.models.Roster;

/**
 * Created by bylynka on 3/25/14.
 */
public class PlayersHistoryPredictionFragment extends BasePlayersFragment {

    private TextView _msgLbl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_history_prediction_players, container, false);
        init();
        return _rootView;
    }

    @Override
    protected void init() {
        //
        _moneyTxt = getViewById(R.id.money_lbl);
        getMainActivity().addListener(this);
        _pager = getViewById(R.id.pager);
        getMainActivity().addRosterLoadedListener(this);
        //

        setPager(123);
        Roster roster = getRoster();
        setMoneyTxt(roster != null ? roster.getRemainingSalary() : getStorage().getDefaultRosterData().getRemainingSalary());
        _msgLbl = getViewById(R.id.msg_lbl);
    }

    @Override
    protected void loadPlayers(final String position) {
    }

    @Override
    public void onRosterLoaded(Roster roster) {
        String end;
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
        if(roster.getAmountPaid() == 0){
            msg += "DIDN'T WIN THIS TIME";
        }else{
            msg += String.format("AND WON %.2f", roster.getAmountPaid());
        }
        _msgLbl.setText(msg);
    }

    @Override
    public void onUpdated(Object initiator) {

    }

    @Override
    public void onRefreshStarted(View view) {

    }
}
