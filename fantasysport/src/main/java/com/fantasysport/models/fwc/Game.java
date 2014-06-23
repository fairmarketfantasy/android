package com.fantasysport.models.fwc;

import com.fantasysport.parsers.jackson.DateDeserializer;
import com.fantasysport.parsers.jackson.DateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

/**
 * Created by bylynka on 6/2/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Game {

    @JsonProperty("stats_id")
    private String _statsId;

    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonProperty("game_time")
    private Date _gameTime;

    @JsonProperty("get_home_team")
    private Team _homeTeam;

    @JsonProperty("get_away_team")
    private Team _awayTeam;

    public Date getGameDate() {

        return _gameTime;
    }

    public Team getHomeTeam(){
        return _homeTeam;
    }

    public Team getAwayTeam(){
        return _awayTeam;
    }

    public String getStatsId(){
        return _statsId;
    }
}
