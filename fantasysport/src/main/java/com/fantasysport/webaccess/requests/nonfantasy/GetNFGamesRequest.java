package com.fantasysport.webaccess.requests.nonfantasy;

import android.net.Uri;
import com.fantasysport.models.NFData;
import com.fantasysport.models.NFRoster;
import com.fantasysport.models.nonfantasy.NFGame;
import com.fantasysport.models.nonfantasy.NFTeam;
import com.fantasysport.utility.DateUtils;
import com.fantasysport.webaccess.requests.BaseRequest;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

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
        List<Game> responseGames = response.getCandidateGames();
        if(responseGames != null){
            for (Game g : responseGames){
                NFTeam home = new NFTeam(g.getHomeTeamName(), g.getHomeTeamPt(), g.getHomeTeamStatsId(), g.getHomeTeamLogo(), g.getStatsId());
                home.setIsPredicted(g.isHomePredicted());
                NFTeam away = new NFTeam(g.getAwayTeamName(), g.getAwayTeamPt(), g.getAwayTeamStatsId(), g.getAwayTeamLogo(), g.getStatsId());
                away.setIsPredicted(g.isAwayTeamPredicted());
                games.add(new NFGame(home, away, g.getGameDate(), g.getStatsId()));
            }
        }
        NFData data = new NFData(response.getRoster(), games);
        data.setUpdatedAt(DateUtils.getCurrentDate().getTime());
        _rHelper.loadNFData(data);
        return data;
    }

    public class GetGamesResponse {

        @SerializedName("game_roster")
        private NFRoster _roster;

        @SerializedName("games")
        private List<Game> _candidateGames;

        public NFRoster getRoster() {
            return _roster;
        }

        public List<Game> getCandidateGames() {
            return _candidateGames;
        }

    }

}
