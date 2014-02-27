package com.fantasysport.activities;

import android.content.Intent;
import android.os.Bundle;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.models.Roster;
import com.fantasysport.webaccess.RequestListeners.CreateRosterResponseListener;
import com.fantasysport.webaccess.RequestListeners.PlayersResponseListener;
import com.fantasysport.webaccess.RequestListeners.RequestError;
import com.fantasysport.webaccess.WebProxy;
import com.fantasysport.webaccess.responses.PlayersRequestResponse;

/**
 * Created by bylynka on 2/26/14.
 */
public class PlayersActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        Intent intent = getIntent();
        final String position = intent.getStringExtra(Const.PLAYER_POSITION);
        final int rosterId = intent.getIntExtra(Const.ROSTER_ID, -1);
        showProgress();
        if(rosterId < 0){
                    WebProxy.createRoster(_storage.getAccessTokenData().getAccessToken(), intent.getIntExtra(Const.MARKET_ID, -1), _spiceManager, new CreateRosterResponseListener() {
                        @Override
                        public void onRequestError(RequestError message) {
                            dismissProgress();
                            showErrorAlert("", getString(R.string.error));
                        }

                        @Override
                        public void onRequestSuccess(Roster roster) {
                            WebProxy.getPlayers(_storage.getAccessTokenData().getAccessToken(), position, true, roster.getId(), _spiceManager, _playersListener);
                        }
                    });
        }else {
            WebProxy.getPlayers(_storage.getAccessTokenData().getAccessToken(), position, true, rosterId, _spiceManager, _playersListener);
        }
    }

    PlayersResponseListener _playersListener = new PlayersResponseListener() {
        @Override
        public void onRequestError(RequestError message) {
            dismissProgress();
            showErrorAlert("", getString(R.string.error));
        }

        @Override
        public void onRequestSuccess(PlayersRequestResponse playersRequestResponse) {
            dismissProgress();
        }
    };

}
