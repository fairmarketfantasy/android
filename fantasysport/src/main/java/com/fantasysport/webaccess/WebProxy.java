package com.fantasysport.webaccess;

import android.graphics.Bitmap;
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

    private SpiceManager _spiceManager;

    public WebProxy(){
    }

    public void setSpiceManager(SpiceManager manager){
        _spiceManager = manager;
    }


    public void getUserData(int userId, UserResponseListener listener){
        UserRequest request = new UserRequest(userId);
        _spiceManager.execute(request, listener);
    }

    public void removeBenchedPlayers(int rosterId, RosterResponseListener listener){
        RemoveBenchedPlayersRequest request = new RemoveBenchedPlayersRequest(rosterId);
        _spiceManager.execute(request, listener);
    }

    public void resetPassword(String email, ResetPasswordResponse listener){
        ResetPasswordRequest request = new ResetPasswordRequest(email);
        _spiceManager.execute(request, listener);
    }


    public void getMainData(int userId, UpdateMainDataResponseListener listener){
        UpdateMainDataRequest request = new UpdateMainDataRequest(userId);
        _spiceManager.execute(request, listener);
    }

    public void getIndividualPredictions(IndividualPredictionsResponseListener listener){
        IndividualPredictionRequest request = new IndividualPredictionRequest();
        _spiceManager.execute(request, listener);
    }

    public void getIndividualPredictions(IndividualPredictionsResponseListener listener, int page){
        IndividualPredictionRequest request = new IndividualPredictionRequest(page);
        _spiceManager.execute(request, listener);
    }

    public void getPredictions(PredictionsResponseListener listener){
        PredictionRequest request = new PredictionRequest();
        _spiceManager.execute(request, listener);
    }

    public void getPredictions(PredictionsResponseListener listener, int page){
        PredictionRequest request = new PredictionRequest(page);
        _spiceManager.execute(request, listener);
    }

    public void signOut(SignOutResponseListener listener){
        SignOutRequest request = new SignOutRequest();
        _spiceManager.execute(request, listener);
    }

    public void uploadAva(Bitmap bitmap, User currentUser, UserResponseListener listener){
        UploadAvaRequest request = new UploadAvaRequest(bitmap, currentUser);
        _spiceManager.execute(request, listener);
    }

    public void updateUser(User user, UserResponseListener listener){
        UpdateUserRequest request = new UpdateUserRequest(user);
        _spiceManager.execute(request, listener);
    }

    public void submitPrediction(int rosterId, int marketId, String statsId, List<StatsItem> events, SubmitPredictionResponseListener listener){
        SubmitPredictionRequest request = new SubmitPredictionRequest(rosterId, marketId, statsId, events);
        _spiceManager.execute(request, listener);
    }

    public void getHTMLContent(String path, StringResponseListener listener){
        HTMLContentRequest request = new HTMLContentRequest(path);
        _spiceManager.execute(request, listener);
    }

    public void getStatEvens(Player player,int marketId, StatEventsResponseListener listener){
        GetStatEventsRequest request = new GetStatEventsRequest(player, marketId);
        _spiceManager.execute(request, listener);
    }

    public void submitRoster(int rosterId, String contestType, SubmitRosterResponseListener listener){
        SubmitRosterRequest request = new SubmitRosterRequest(rosterId, contestType);
        _spiceManager.execute(request, listener);
    }

    public void autofillRoster(int marketId, int rosterId, AutofillResponseListener listener){
        AutofillRequest request = new AutofillRequest(marketId, rosterId);
        request.setRetryPolicy(getRetryPolicy());
        _spiceManager.execute(request, listener);
    }

    public void tradePlayer(int rosterId, Player player, TradePlayerResponseListener listener){
        TradePlayerRequest request = new TradePlayerRequest(rosterId, player);
        _spiceManager.execute(request, listener);
    }

    public void addPlayer(int rosterId, Player player, AddPlayerResponseListener listener){
        AddPlayerRequest request = new AddPlayerRequest(rosterId, player);
        _spiceManager.execute(request, listener);
    }

    public void getPlayers(String position, boolean removedBencedPlayers, int rosterId, PlayersResponseListener listener){
        GetPlayersRequest request = new GetPlayersRequest(position, removedBencedPlayers, rosterId);
        _spiceManager.execute(request, listener);
    }

    public void createRoster(int marketId, RosterResponseListener listener){
        CreateRosterRequest request = new CreateRosterRequest(marketId);
        _spiceManager.execute(request, listener);
    }

    public void getRoster(int rosterId, RosterResponseListener listener){
        GetRosterRequest request = new GetRosterRequest(rosterId);
        _spiceManager.execute(request, listener);
    }

    public void loadPlayersPosition(DefaultRosterResponseListener listener){
        PlayersPositionRequest request = new PlayersPositionRequest();
        _spiceManager.execute(request, listener);
    }

    public void getMarkets(MarketsResponseListener listener){
        MarketsRequest request = new MarketsRequest();
        _spiceManager.execute(request, listener);
    }

    public void signIn(String email, String password, SignInResponseListener listener){
        SignInRequest request = new SignInRequest(email, password);
        _spiceManager.execute(request, listener);
    }

    public void getAccessToken(String email, String password, AccessTokenResponseListener listener){
        AccessTokenRequest request = new AccessTokenRequest(email, password);
        _spiceManager.execute(request, listener);
    }

    public void facebookLogin(String accessToken,String uid, FaceBookAuthListener listener){
        FacebookSignInRequest request = new FacebookSignInRequest(accessToken, uid);
        _spiceManager.execute(request, listener);
    }

    public void signUp(User user, SignUpResponseListener listener){
        SignUpRequest request = new SignUpRequest(user);
        request.setRetryPolicy(getRetryPolicy());
        _spiceManager.execute(request, listener);
    }

    private RetryPolicy getRetryPolicy(){
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