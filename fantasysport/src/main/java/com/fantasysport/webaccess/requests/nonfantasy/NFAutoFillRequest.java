package com.fantasysport.webaccess.requests.nonfantasy;

import android.net.Uri;
import com.fantasysport.models.nonfantasy.NFAutoFillData;
import com.fantasysport.models.nonfantasy.NFGame;
import com.fantasysport.models.nonfantasy.NFTeam;
import com.fantasysport.webaccess.requests.BaseRequest;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 5/22/14.
 */
public class NFAutoFillRequest extends BaseRequest<NFAutoFillData> {

    private String _sport;

    public NFAutoFillRequest(String sport) {
        super(NFAutoFillData.class);
        _sport = sport;

    }

    @Override
    public NFAutoFillData loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("game_rosters")
                .appendPath("autofill")
                .appendQueryParameter("sport", _sport)
                .appendQueryParameter("access_token", getAccessToken());
        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), null);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        AutoFillResponseBody response = new Gson().fromJson(result, AutoFillResponseBody.class);
        List<NFGame> games = new ArrayList<NFGame>();
        List<NFTeam> rosterTeams = new ArrayList<NFTeam>();
        List<Game> candidateGames = response.getGames();
        List<RosterTeamData> rosterGameDataList= response.getRosterGamesData();
        if (candidateGames != null) {
            for (Game rGame : candidateGames) {
                NFTeam home = new NFTeam(rGame.getHomeTeamName(), rGame.getHomeTeamPt(), rGame.getHomeTeamStatsId(), rGame.getHomeTeamLogo(), rGame.getStatsId());
                NFTeam away = new NFTeam(rGame.getAwayTeamName(), rGame.getAwayTeamPt(), rGame.getAwayTeamStatsId(), rGame.getAwayTeamLogo(), rGame.getStatsId());
                games.add(new NFGame(home, away, rGame.getGameDate(), rGame.getStatsId()));
                for (RosterTeamData rgd : rosterGameDataList){
                    if(rgd.getGameStatsId() == rGame.getStatsId()){
                        rosterTeams.add(home.getStatsId()== rgd.getTeamStatsId()? home: away);
                    }
                }
            }
        }
        return new NFAutoFillData(rosterTeams, games);
    }

    public class AutoFillResponseBody{

        @SerializedName("games")
        private List<Game> _games;

        @SerializedName("predictions")
        private List<RosterTeamData> _candidateGamesData;


        public List<RosterTeamData> getRosterGamesData() {
            return _candidateGamesData;
        }

        public List<Game> getGames() {
            return _games;
        }
    }

    public class RosterTeamData {

        @SerializedName("team_stats_id")
        private int _teamStatsId;

        @SerializedName("game_stats_id")
        private int _gameStatsId;

        public int getTeamStatsId(){
            return _teamStatsId;
        }

        public int getGameStatsId(){
            return _gameStatsId;
        }
    }
}
