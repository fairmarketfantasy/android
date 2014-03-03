package com.fantasysport.webaccess;

import com.fantasysport.models.Market;
import com.fantasysport.models.Player;
import com.fantasysport.models.User;
import com.fantasysport.webaccess.RequestListeners.*;
import com.fantasysport.webaccess.Requests.*;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.retry.RetryPolicy;

/**
 * Created by bylynka on 2/6/14.
 */
public final class WebProxy {

    private WebProxy(){
    }

    public static void addPlayer(int rosterId, Player player, String accessToken, SpiceManager spiceManager, AddPlayerResponseListener listener){
        AddPlayerRequest request = new AddPlayerRequest(rosterId, player, accessToken);
        spiceManager.execute(request, listener);
    }

    public static void getPlayers(String accessToken, String position, boolean removedBencedPlayers, int rosterId, SpiceManager spiceManager, PlayersResponseListener listener){
        GetPlayersRequest request = new GetPlayersRequest(accessToken, position, removedBencedPlayers, rosterId);
        spiceManager.execute(request, listener);
    }

    public static void createRoster(String accessToken, int marketId, SpiceManager spiceManager, CreateRosterResponseListener listener){
        CreateRosterRequest request = new CreateRosterRequest(accessToken, marketId);
        spiceManager.execute(request, listener);
    }

    public static void loadPlayersPosition(String accessToken, SpiceManager spiceManager, DefaultRosterResponseListener listener){
        PlayersPositionRequest request = new PlayersPositionRequest(accessToken);
        spiceManager.execute(request, listener);
    }

    public static void getGames(String accessToken, SpiceManager spiceManager, MarketsResponseListener listener){
        MarketsRequest request = new MarketsRequest(accessToken);
        spiceManager.execute(request, listener);
    }

    public static void signIn(String email, String password, SpiceManager spiceManager, SignInResponseListener listener){
        SignInRequest request = new SignInRequest(email, password);
        spiceManager.execute(request, listener);
    }

    public static void getAccessToken(String email, String password, SpiceManager spiceManager, AccessTokenResponseListener listener){
        AccessTokenRequest request = new AccessTokenRequest(email, password);
        spiceManager.execute(request, listener);
    }

    public static void facebookLogin(String accessToken,String uid, SpiceManager spiceManager, FaceBookAuthListener listener){
        FacebookSignInRequest request = new FacebookSignInRequest(accessToken, uid);
        spiceManager.execute(request, listener);
    }

    public static void signUp(User user, SpiceManager spiceManager, SignUpResponseListener listener){
        SignUpRequest request = new SignUpRequest(user);
        request.setRetryPolicy(getRetryPolicy());
        spiceManager.execute(request, listener);
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
