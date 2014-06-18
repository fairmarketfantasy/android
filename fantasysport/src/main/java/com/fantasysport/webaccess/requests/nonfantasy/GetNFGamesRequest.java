package com.fantasysport.webaccess.requests.nonfantasy;

import android.net.Uri;
import com.fantasysport.models.nonfantasy.NFData;
import com.fantasysport.models.NFRoster;
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
 * Created by bylynka on 5/13/14.
 */
public class GetNFGamesRequest extends BaseRequest<NFData> {

    private String _sport;
    private int _rosterId = -1;

    public GetNFGamesRequest(String sport) {
        super(NFData.class);
        _sport = sport;
    }

    public GetNFGamesRequest(String sport, int rosterId) {
        super(NFData.class);
        _sport = sport;
        _rosterId = rosterId;
    }

    @Override
    public NFData loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("game_predictions")
                .appendPath("new_day_games")
                .appendQueryParameter("access_token", getAccessToken())
                .appendQueryParameter("sport", _sport);
        if (_rosterId > 0) {
            uriBuilder.appendQueryParameter("roster_id", Integer.toString(_rosterId));
        }

        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.setConnectTimeout(1000 * 120);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        GetGamesResponse response = new Gson().fromJson(result, GetGamesResponse.class);
        List<NFGame> games = response.getCandidateGames();
        List<NFTeam> rosterTeams = new ArrayList<NFTeam>();

        List<TeamData> rosterGameDataList = response.getRoster()._rosterTeamData;
        if (rosterGameDataList != null) {
            for (TeamData rgd : rosterGameDataList) {
                NFTeam t = new NFTeam(rgd._name, rgd._pt, rgd._statsId, rgd._logoUrl, rgd._gameStatsId, rgd.getGameName(), rgd._gameDate);
                rosterTeams.add(t);
                for(NFGame g : games){
                    if(g.getAwayTeam().getStatsId().compareTo(t.getStatsId()) == 0){
                        g.getAwayTeam().setIsSelected(true);
                    }else if(g.getHomeTeam().getStatsId().compareTo(t.getStatsId()) == 0){
                        g.getHomeTeam().setIsSelected(true);
                    }
                }
            }
        }
        RosterInResponse rInResponse = response.getRoster();
        NFRoster roster = new NFRoster(rosterTeams, rInResponse._state, rInResponse._roomNumber, rInResponse._amountPaid, rInResponse._contestRank, _rosterId);
        NFData data = new NFData(roster, games);
        data.setUpdatedAt(DateUtils.getCurrentDate().getTime());
        _rHelper.loadNFData(data);
        return data;
    }

    public class GetGamesResponse {

        @SerializedName("game_roster")
        private RosterInResponse _roster;

        @SerializedName("games")
        private List<NFGame> _candidateGames;

        public RosterInResponse getRoster() {
            return _roster;
        }

        public List<NFGame> getCandidateGames() {
            return _candidateGames;
        }

    }

    public class RosterInResponse {

        @SerializedName("id")
        private Integer _id;

        @SerializedName("room_number")
        private int _roomNumber;

        @SerializedName("state")
        private String _state;

        @SerializedName("amount_paid")
        private Double _amountPaid;

        @SerializedName("contest_rank")
        private Integer _contestRank;

        @SerializedName("game_predictions")
        private List<TeamData> _rosterTeamData;

    }

    public class TeamData {

        @SerializedName("game_stats_id")
        String _gameStatsId;

        @SerializedName("team_name")
        String _name;

        @SerializedName("pt")
        double _pt;

        @SerializedName("team_stats_id")
        String _statsId;

        @SerializedName("team_logo")
        String _logoUrl;

        @SerializedName("opposite_team")
        private String _oppositeTeam;

        @SerializedName("home_team")
        boolean _isHomeTeam;

        @SerializedName("game_time")
        String _gameDate;

        String getGameName() {
            String homeTeam;
            String awayTeam;
            if (_isHomeTeam) {
                homeTeam = _name;
                awayTeam = _oppositeTeam;
            } else {
                homeTeam = _oppositeTeam;
                awayTeam = _name;
            }
            return String.format("%s@%s", homeTeam, awayTeam);
        }

    }

}
