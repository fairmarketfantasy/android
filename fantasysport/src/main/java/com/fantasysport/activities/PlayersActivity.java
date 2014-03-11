package com.fantasysport.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.adapters.CandidatePlayersAdapter;
import com.fantasysport.models.Player;
import com.fantasysport.models.Roster;
import com.fantasysport.views.Switcher;
import com.fantasysport.webaccess.requestListeners.AddPlayerResponseListener;
import com.fantasysport.webaccess.requestListeners.CreateRosterResponseListener;
import com.fantasysport.webaccess.requestListeners.PlayersResponseListener;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.WebProxy;
import com.fantasysport.webaccess.responses.PlayersRequestResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 2/26/14.
 */
public class PlayersActivity extends BaseActivity implements CandidatePlayersAdapter.IListener {

    private final String PLAYERS = "players";
    private final String REMOVE_BENCHED_PLAYERS = "remove_benched_players";

    private CandidatePlayersAdapter _playersAdapter;
    private Switcher _switcher;
    private Roster _roster;
    private double _moneyForRoster;
    private int _marketId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        getSupportActionBar().setHomeButtonEnabled(true);
        Intent intent = getIntent();

        _roster = (Roster)intent.getSerializableExtra(Const.ROSTER);
        _switcher = getViewById(R.id.switcher);
        setPlayersListView();
        boolean _removeBooleanExtra = intent.getBooleanExtra(Const.REMOVE_BENCHED_PLAYERS, true);
        _moneyForRoster = intent.getDoubleExtra(Const.MONEY_FOR_ROSTER, 0);
        TextView moneyView = getViewById(R.id.money_lbl);
        moneyView.setTypeface(getProhibitionRound());
        moneyView.setText(String.format("$%.0f",_moneyForRoster));
        _marketId = intent.getIntExtra(Const.MARKET_ID, - 1);
        if (savedInstanceState != null) {
            List<Player> players = (List<Player>) savedInstanceState.getSerializable(PLAYERS);
            _playersAdapter.setItems(players);
            _playersAdapter.notifyDataSetChanged();
            _switcher.setSelected(savedInstanceState.getBoolean(REMOVE_BENCHED_PLAYERS, _removeBooleanExtra));
            _switcher.setSelectedListener(_switcherListener);
            return;
        }
        _switcher.setSelected(_removeBooleanExtra);
        _switcher.setSelectedListener(_switcherListener);
        startWork();
    }

    private void startWork(){

        if (_roster == null) {
            showProgress();
            _webProxy.createRoster(_marketId, new CreateRosterResponseListener() {
                @Override
                public void onRequestError(RequestError message) {
                    dismissProgress();
                    showAlert("", getString(R.string.error));
                }
                @Override
                public void onRequestSuccess(Roster roster) {
                    _roster = roster;
                    loadPlayers();
                }
            });
        } else {
            loadPlayers();
        }
    }

    private void loadPlayers(){
        showProgress();
        Intent intent = getIntent();
        final String position = intent.getStringExtra(Const.PLAYER_POSITION);
        _webProxy.getPlayers(position, _switcher.isSelected(), _roster.getId(), _playersListener);
    }

    private void setPlayersListView() {
        ListView listView = getViewById(R.id.players_list);
        _playersAdapter = new CandidatePlayersAdapter(this);
        _playersAdapter.setListener(this);
        listView.setAdapter(_playersAdapter);
    }


    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        List<Player> players = _playersAdapter.getPlayers();
        outState.putSerializable(PLAYERS, new ArrayList<Player>(players));
        outState.putBoolean(REMOVE_BENCHED_PLAYERS, _switcher.isSelected());
    }

    PlayersResponseListener _playersListener = new PlayersResponseListener() {
        @Override
        public void onRequestError(RequestError message) {
            dismissProgress();
            showAlert("", getString(R.string.error));
        }

        @Override
        public void onRequestSuccess(PlayersRequestResponse response) {
            _playersAdapter.setItems(response.getPlayers());
            _playersAdapter.notifyDataSetInvalidated();
            dismissProgress();
        }
    };

    Switcher.ISelectedListener _switcherListener = new Switcher.ISelectedListener() {
        @Override
        public void onStateChanged(boolean isSelected) {
            loadPlayers();
        }
    };

    @Override
    public void onAddPlayer(Player player) {
        showProgress();
        _webProxy.addPlayer(_roster.getId(), player, _addPlayerListener);
    }

    @Override
    public void onPT25Player(Player player) {
        Intent intent = new Intent(this, IndividuaPredictionsActivity.class);
        intent.putExtra(Const.PLAYER, player);
        intent.putExtra(Const.ROSTER_ID, _roster.getId());
        intent.putExtra(Const.MARKET_ID, _marketId);
        startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    AddPlayerResponseListener _addPlayerListener = new AddPlayerResponseListener() {
        @Override
        public void onRequestError(RequestError error) {
            dismissProgress();
            showAlert("", error.getMessage());
        }

        @Override
        public void onRequestSuccess(Player player) {
            Intent intent = new Intent();
            List<Player> players = _roster.getPlayers();
            if(player == null){
                players = new ArrayList<Player>();
            }
            players.add(player);
            _roster.setPlayers(players);
            double remainingSalary = _roster.getRemainingSalary() - player.getBuyPrice();
            _roster.setRemainingSalary(remainingSalary);
            intent.putExtra(Const.ROSTER, _roster);
            setResult(Activity.RESULT_OK, intent);
            finish();
            dismissProgress();
        }
    };
}
