package com.fantasysport.webaccess.responses;

import com.fantasysport.models.NFRoster;
import com.fantasysport.utility.Converter;
import com.fantasysport.utility.DateUtils;
import com.fantasysport.utility.DeviceInfo;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by bylynka on 5/13/14.
 */
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

    public class Game {

        private String _formattedGameTime;

        @SerializedName("game_stats_id")
        private int _statsId;

        @SerializedName("game_time")
        private String _gameTime;

        @SerializedName("home_team_name")
        private String _homeTeam;

        @SerializedName("away_team_name")
        private String _awayTeam;

        @SerializedName("home_team_pt")
        private int _homeTeamPt;

        @SerializedName("away_team_pt")
        private int _awayTeamPt;

        @SerializedName("home_team_stats_id")
        private int _homeTeamStatsId;

        @SerializedName("away_team_stats_id")
        private int _awayTeamStatsId;

        @SerializedName("home_team_logo_url")
        private String _homeTeamLogo;

        @SerializedName("away_team_logo_url")
        private String _awayTeamLogo;

        public int getStatsId() {
            return _statsId;
        }

        public Date getGameDate() {
                Date date = Converter.toDate(_gameTime);
                int gmtInMinutes = DeviceInfo.getGMTInMinutes();
                return DateUtils.addMinutes(date, gmtInMinutes);
        }

        public String getHomeTeamName() {
            return _homeTeam;
        }

        public String getAwayTeamName() {
            return _awayTeam;
        }

        public int getHomeTeamPt() {
            return _homeTeamPt;
        }

        public int getAwayTeamPt() {
            return _awayTeamPt;
        }

        public String getHomeTeamLogo() {
            return _homeTeamLogo;
        }

        public String getAwayTeamLogo() {
            return _awayTeamLogo;
        }

        public int getHomeTeamStatsId() {
            return _homeTeamStatsId;
        }

        public int getAwayTeamStatsId() {
            return _awayTeamStatsId;
        }
    }
}
