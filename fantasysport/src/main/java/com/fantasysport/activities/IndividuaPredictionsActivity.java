package com.fantasysport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.adapters.StatsAdapter;
import com.fantasysport.models.FGame;
import com.fantasysport.models.Player;
import com.fantasysport.models.StatsItem;
import com.fantasysport.utility.image.ImageLoader;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.requestListeners.StatEventsResponseListener;
import com.fantasysport.webaccess.requestListeners.SubmitPredictionResponseListener;
import com.fantasysport.webaccess.responses.StatEventsResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 3/5/14.
 */
public class IndividuaPredictionsActivity extends BaseActivity implements StatsAdapter.ISubmitListener {

    private StatsAdapter _statsAdapter;
    private int _rosterId;
    private int _marketId;
    private Player _player;
    private FGame _game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_individual_predictions);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setStartParameters(savedInstanceState);

        _statsAdapter = new StatsAdapter(this, _player);
//        _player.getTeam()
        _statsAdapter.setSubmitListener(this);
        ListView listView = getViewById(R.id.stats_list);
        listView.setAdapter(_statsAdapter);
        TextView nameLbl = getViewById(R.id.name_lbl);
        boolean isHomeGame = _player.getTeam().equalsIgnoreCase(_game.getHomeTeam());
        nameLbl.setText(String.format("%s %s %s",_player.getName(), isHomeGame?"vs":"@", _game.getAwayTeam()));
        View customBar = getLayoutInflater().inflate(R.layout.action_bar_title, null);
        TextView textView = (TextView)customBar.findViewById(R.id.fair_martet_txt);
        textView.setText("PT");
        getSupportActionBar().setCustomView(customBar);

        ImageView playerImg = getViewById(R.id.player_img);
        ImageLoader imageLoader = new ImageLoader(this);
        imageLoader.displayImage(_player.getImageUrl(), playerImg);

        if (savedInstanceState == null) {
            loadStatEvents(_player);
            return;
        }
        List<StatsItem> items = (List<StatsItem>) savedInstanceState.getSerializable(Const.STATS_LIST);
        if(items == null){
            loadStatEvents(_player);
            return;
        }
        _statsAdapter.setItems(items);
        _statsAdapter.notifyDataSetChanged();
    }

    private void setStartParameters(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            _player = (Player) intent.getSerializableExtra(Const.PLAYER);
            _rosterId = intent.getIntExtra(Const.ROSTER_ID, -1);
            _marketId = intent.getIntExtra(Const.MARKET_ID, -1);
            _game = (FGame) intent.getSerializableExtra(Const.GAME);
        } else {
            _game = (FGame) savedInstanceState.getSerializable(Const.GAME);
            _player = (Player) savedInstanceState.getSerializable(Const.PLAYER);
            _rosterId = savedInstanceState.getInt(Const.ROSTER_ID);
            _marketId = savedInstanceState.getInt(Const.MARKET_ID);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Const.STATS_LIST, new ArrayList<StatsItem>(_statsAdapter.getItems()));
        outState.putInt(Const.ROSTER_ID, _rosterId);
        outState.putInt(Const.MARKET_ID, _marketId);
        outState.putSerializable(Const.PLAYER, _player);
        outState.putSerializable(Const.GAME, _game);
    }

    private void loadStatEvents(Player player) {
        showProgress();
        _webProxy.getStatEvens(player, _marketId, new StatEventsResponseListener() {
            @Override
            public void onRequestError(RequestError error) {
                dismissProgress();
                showAlert(getString(R.string.error), error.getMessage());
            }

            @Override
            public void onRequestSuccess(StatEventsResponse response) {
                _statsAdapter.setItems(response.getStatEvents());
                _statsAdapter.notifyDataSetChanged();
                dismissProgress();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void submit(){
        List<StatsItem> items = _statsAdapter.getItems();
        List<StatsItem> resultItems = new ArrayList<StatsItem>();
        for (int i = 0; i < items.size(); i++) {
            if (!items.get(i).getMode().equalsIgnoreCase(StatsItem.DEFAULT_MODE)) {
                resultItems.add(items.get(i));
            }
        }
        showProgress();
        _webProxy.submitPrediction(_rosterId, _marketId, _player.getStatsId(), resultItems, new SubmitPredictionResponseListener() {
            @Override
            public void onRequestError(RequestError error) {
                dismissProgress();
                showAlert(getString(R.string.error), error.getMessage());
            }

            @Override
            public void onRequestSuccess(Object o) {
                dismissProgress();
            }
        });
    }

    View.OnClickListener _submitBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           submit();
        }
    };

    @Override
    public void onSubmit() {
        submit();
    }
}