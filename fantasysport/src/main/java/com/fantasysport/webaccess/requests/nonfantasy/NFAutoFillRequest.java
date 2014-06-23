package com.fantasysport.webaccess.requests.nonfantasy;

import android.net.Uri;
import com.fantasysport.models.nonfantasy.NFAutoFillData;
import com.fantasysport.models.nonfantasy.NFGame;
import com.fantasysport.models.nonfantasy.NFTeam;
import com.fantasysport.parsers.jackson.DateDeserializer;
import com.fantasysport.webaccess.requests.BaseRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;

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
                .appendPath("new_autofill")
                .appendQueryParameter("sport", _sport)
                .appendQueryParameter("access_token", getAccessToken());
        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), null);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        AutoFillResponseBody response = getObjectMapper().readValue(result, AutoFillResponseBody.class);
        List<NFGame> games = response.getGames();
        List<NFTeam> rosterTeams = new ArrayList<NFTeam>();
        List<RosterTeamData> rosterGameDataList= response.getRosterGamesData();
        for (RosterTeamData rgd : rosterGameDataList){
            String gameName = rgd._gameName != null?rgd._gameName.trim(): "";
                    rosterTeams.add(new NFTeam(rgd._name, rgd._pt, rgd._teamStatsId, rgd._logoUrl, rgd._gameStatsId, gameName , rgd.getGameDate()));
        }
        return new NFAutoFillData(rosterTeams, games);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AutoFillResponseBody{

        @JsonProperty("games")
        private List<NFGame> _games;

        @JsonProperty("predictions")
        private List<RosterTeamData> _candidateGamesData;

        public List<RosterTeamData> getRosterGamesData() {
            return _candidateGamesData;
        }

        public List<NFGame> getGames() {
            return _games;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RosterTeamData {

        @JsonProperty("team_stats_id")
        private String _teamStatsId;

        @JsonProperty("game_stats_id")
        private String _gameStatsId;

        @JsonProperty("team_name")
        String _name;

        @JsonProperty("pt")
        double _pt;

        @JsonProperty("team_logo")
        String _logoUrl;

        @JsonProperty("market_name")
        String _gameName;

        @JsonDeserialize(using = DateDeserializer.class)
        @JsonProperty("game_time")
        Date _gameDate;

        public Date getGameDate() {
            return _gameDate;
        }

    }

}
