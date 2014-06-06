package com.fantasysport.webaccess.requests.nonfantasy;

import android.net.Uri;
import com.fantasysport.models.nonfantasy.NFAutoFillData;
import com.fantasysport.models.nonfantasy.NFGame;
import com.fantasysport.models.nonfantasy.NFTeam;
import com.fantasysport.utility.Converter;
import com.fantasysport.utility.DateUtils;
import com.fantasysport.utility.DeviceInfo;
import com.fantasysport.webaccess.requests.BaseRequest;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
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
            for (Game g : candidateGames) {
                String gameName = String.format("%s@%s", g.getHomeTeamName(), g.getAwayTeamName());
                NFTeam home = new NFTeam(g.getHomeTeamName(), g.getHomeTeamPt(), g.getHomeTeamStatsId(), g.getHomeTeamLogo(), g.getStatsId(), gameName, g.getGameDate());
                home.setIsPredicted(g.isHomePredicted());
                home.setIsSelected(g.isHomeSelected());
                NFTeam away = new NFTeam(g.getAwayTeamName(), g.getAwayTeamPt(), g.getAwayTeamStatsId(), g.getAwayTeamLogo(), g.getStatsId(), gameName, g.getGameDate());
                away.setIsPredicted(g.isAwayTeamPredicted());
                away.setIsSelected(g.isAwaySelected());
                games.add(new NFGame(home, away, g.getGameDate(), g.getStatsId()));
            }
        }
        for (RosterTeamData rgd : rosterGameDataList){
            rosterTeams.add(new NFTeam(rgd._name, rgd._pt, rgd._teamStatsId, rgd._logoUrl, rgd._gameStatsId, rgd._gameName, rgd.getGameDate()));
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

        @SerializedName("team_name")
        String _name;

        @SerializedName("pt")
        double _pt;

        @SerializedName("team_logo")
        String _logoUrl;

        @SerializedName("Reds @ Giants")
        String _gameName;

        @SerializedName("game_time")
        String _gameDate;

        public Date getGameDate() {
            Date date = Converter.toDate(_gameDate);
            int gmtInMinutes = DeviceInfo.getGMTInMinutes();
            return DateUtils.addMinutes(date, gmtInMinutes);
        }

    }

}
