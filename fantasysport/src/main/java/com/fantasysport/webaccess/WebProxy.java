package com.fantasysport.webaccess;

import com.fantasysport.models.Player;
import com.fantasysport.models.StatsItem;
import com.fantasysport.models.User;
import com.fantasysport.webaccess.requestListeners.*;
import com.fantasysport.webaccess.requests.*;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.retry.RetryPolicy;

import java.util.List;

/**
 * Created by bylynka on 2/6/14.
 */
public final class WebProxy {

    private WebProxy(){
    }

    public static void submitPrediction(int rosterId, int marketId, String statsId, List<StatsItem> events, String accessToken, SpiceManager manager, SubmitPredictionResponseListener listener){
        SubmitPredictionRequest request = new SubmitPredictionRequest(rosterId, marketId, statsId, events, accessToken);
        manager.execute(request, listener);
    }

    public static void getStatEvens(Player player, String accessToken, SpiceManager manager, StatEventsResponseListener listener){
        GetStatEventsRequest request = new GetStatEventsRequest(player, accessToken);
        manager.execute(request, listener);
    }

    public static void submitRoster(int rosterId, String contestType, String accessToken, SpiceManager manager, SubmitRosterResponseListener listener){
        SubmitRosterRequest request = new SubmitRosterRequest(rosterId, contestType, accessToken);
        manager.execute(request, listener);
    }

    public static void autofillRoster(int marketId, int rosterId, String accessToken, SpiceManager manager, AutofillResponseListener listener){
        AutofillRequest request = new AutofillRequest(marketId, rosterId, accessToken);
        manager.execute(request, listener);
    }

    public static void tradePlayer(int rosterId, Player player, String accessToken, SpiceManager manager, TradePlayerResponseListener listener){
        TradePlayerRequest request = new TradePlayerRequest(rosterId, player, accessToken);
        manager.execute(request, listener);
    }

    public static void addPlayer(int rosterId, Player player, String accessToken, SpiceManager manager, AddPlayerResponseListener listener){
        AddPlayerRequest request = new AddPlayerRequest(rosterId, player, accessToken);
        manager.execute(request, listener);
    }

    public static void getPlayers(String accessToken, String position, boolean removedBencedPlayers, int rosterId, SpiceManager manager, PlayersResponseListener listener){
        GetPlayersRequest request = new GetPlayersRequest(accessToken, position, removedBencedPlayers, rosterId);
        manager.execute(request, listener);
    }

    public static void createRoster(String accessToken, int marketId, SpiceManager manager, CreateRosterResponseListener listener){
        CreateRosterRequest request = new CreateRosterRequest(accessToken, marketId);
        manager.execute(request, listener);
    }

    public static void loadPlayersPosition(String accessToken, SpiceManager manager, DefaultRosterResponseListener listener){
        PlayersPositionRequest request = new PlayersPositionRequest(accessToken);
        manager.execute(request, listener);
    }

    public static void getGames(String accessToken, SpiceManager manager, MarketsResponseListener listener){
        MarketsRequest request = new MarketsRequest(accessToken);
        manager.execute(request, listener);
    }

    public static void signIn(String email, String password, SpiceManager manager, SignInResponseListener listener){
        SignInRequest request = new SignInRequest(email, password);
        manager.execute(request, listener);
    }

    public static void getAccessToken(String email, String password, SpiceManager manager, AccessTokenResponseListener listener){
        AccessTokenRequest request = new AccessTokenRequest(email, password);
        manager.execute(request, listener);
    }

    public static void facebookLogin(String accessToken,String uid, SpiceManager manager, FaceBookAuthListener listener){
        FacebookSignInRequest request = new FacebookSignInRequest(accessToken, uid);
        manager.execute(request, listener);
    }

    public static void signUp(User user, SpiceManager manager, SignUpResponseListener listener){
        SignUpRequest request = new SignUpRequest(user);
        request.setRetryPolicy(getRetryPolicy());
        manager.execute(request, listener);
    }

    private static RetryPolicy getRetryPolicy(){
        return new RetryPolicy() {
            @Override
            public int getRetryCount() {
                return 0;
            }

            @Override
            public void retry(SpiceException e) {

            }

            @Override
            public long getDelayBeforeRetry() {
                return 0;
            }
        };
    }

}
