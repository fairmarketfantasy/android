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
                .appendPath("day_games")
                .appendQueryParameter("access_token", getAccessToken())
                .appendQueryParameter("sport", _sport);
        if (_rosterId > 0) {
            uriBuilder.appendQueryParameter("roster_id", Integer.toString(_rosterId));
        }

        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        GetGamesResponse response = new Gson().fromJson(result, GetGamesResponse.class);
        List<NFGame> games = new ArrayList<NFGame>();
        List<Game> responseGames = response.getCandidateGames();
        List<NFTeam> rosterTeams = new ArrayList<NFTeam>();
        List<RosterTeamData> rosterGameDataList = response.getRoster()._rosterTeamData;
        if (responseGames != null) {
            for (Game g : responseGames) {
                NFTeam home = new NFTeam(g.getHomeTeamName(), g.getHomeTeamPt(), g.getHomeTeamStatsId(), g.getHomeTeamLogo(), g.getStatsId());
                home.setIsPredicted(g.isHomePredicted());
                NFTeam away = new NFTeam(g.getAwayTeamName(), g.getAwayTeamPt(), g.getAwayTeamStatsId(), g.getAwayTeamLogo(), g.getStatsId());
                away.setIsPredicted(g.isAwayTeamPredicted());
                games.add(new NFGame(home, away, g.getGameDate(), g.getStatsId()));
                if (rosterGameDataList != null) {
                    for (RosterTeamData rgd : rosterGameDataList) {
                        if (rgd.getGameStatsId() == g.getStatsId()) {
                            rosterTeams.add(home.getStatsId() == rgd.getTeamStatsId() ? home : away);
                        }
                    }
                }
            }
        }
        RosterInResponse rInResponse = response.getRoster();
        NFRoster roster = new NFRoster(rosterTeams, rInResponse._state, rInResponse._roomNumber, rInResponse._amountPaid, rInResponse._contestRank);
        NFData data = new NFData(roster, games);
        data.setUpdatedAt(DateUtils.getCurrentDate().getTime());
        _rHelper.loadNFData(data);
        return data;
    }

    public class GetGamesResponse {

        @SerializedName("game_roster")
        private RosterInResponse _roster;

        @SerializedName("games")
        private List<Game> _candidateGames;

        public RosterInResponse getRoster() {
            return _roster;
        }

        public List<Game> getCandidateGames() {
            return _candidateGames;
        }

    }

    public class RosterInResponse {

        @SerializedName("room_number")
        private int _roomNumber;

        @SerializedName("state")
        private String _state;

        @SerializedName("amount_paid")
        private Double _amountPaid;

        @SerializedName("contest_rank")
        private Integer _contestRank;

        @SerializedName("game_predictions")
        private List<RosterTeamData> _rosterTeamData;

    }

}
