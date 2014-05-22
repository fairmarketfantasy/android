package com.fantasysport.webaccess.requests;

import android.net.Uri;
import com.fantasysport.models.NFData;
import com.fantasysport.models.nonfantasy.NFGame;
import com.fantasysport.models.nonfantasy.NFTeam;
import com.fantasysport.webaccess.responses.GetGamesResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 5/13/14.
 */
public class GetNFGamesRequest extends BaseRequest<NFData> {

    public String _sport;

    public GetNFGamesRequest(String sport) {
        super(NFData.class);
        _sport = sport;
    }

    @Override
    public NFData loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("game_predictions")
                .appendPath("day_games")
                .appendQueryParameter("access_token", getAccessToken())
                .appendQueryParameter("sport", _sport);
        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        GetGamesResponse response = new Gson().fromJson(result, GetGamesResponse.class);
        List<NFGame> games = new ArrayList<NFGame>();
        List<GetGamesResponse.Game> responseGames = response.getCandidateGames();
        if(responseGames != null){
            for (GetGamesResponse.Game rGame : responseGames){
                NFTeam home = new NFTeam(rGame.getHomeTeamName(), rGame.getHomeTeamPt(), rGame.getHomeTeamStatsId(), rGame.getHomeTeamLogo(), rGame.getStatsId());
                NFTeam away = new NFTeam(rGame.getAwayTeamName(), rGame.getAwayTeamPt(), rGame.getAwayTeamStatsId(), rGame.getAwayTeamLogo(), rGame.getStatsId());
                games.add(new NFGame(home, away, rGame.getGameDate(), rGame.getStatsId()));
            }
        }
        NFData data = new NFData(response.getRoster(), games);
        _rHelper.loadNFData(data);
        return data;
    }
}
