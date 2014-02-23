package com.fantasysport.parsers;

import com.fantasysport.models.Game;
import com.fantasysport.models.Market;
import com.fantasysport.utility.Converter;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by bylynka on 2/19/14.
 */
public class MarketParser {

    private final String ID = "id";
    private final String NAME = "name";
    private final String SHADOW_BETS = "shadow_bets";
    private final String SHADOW_BET_RATE = "shadow_bet_rate";
    private final String STARTED_AT = "started_at";
    private final String OPENED_AT = "opened_at";
    private final String CLOSED_AT = "closed_at";
    private final String SPORT_ID = "sport_id";
    private final String TOTAL_BETS = "total_bets";
    private final String STATE = "state";
    private final String MARKET_DURATION = "market_duration";
    private final String GAME_TYPE = "game_type";
    private final String GAMES = "games";

    private HashMap<String, Integer> _keyMap;
    private int _fieldsNumber;
    private List<Object> _marketObjects;
    private List<Market> _markets;

    public List<Market> parse(String json) {
        MarketListWrapper marketListWrapper = new Gson().fromJson(json, MarketListWrapper.class);
        if (marketListWrapper == null ||
                marketListWrapper.getMarketList() == null ||
                marketListWrapper.getMarketList().size() == 0) {
            return null;
        }
        _marketObjects= marketListWrapper.getMarketList();
        _fieldsNumber = ((Double)_marketObjects.get(0)).intValue();
        fillKeyMap();
        _markets = new ArrayList<Market>();
        int marketsNumber = (_marketObjects.size() - _fieldsNumber - 1)/_fieldsNumber;
        for (int i = 1; i <= marketsNumber; i++){
           Market market = parseMarket(_fieldsNumber * i);
           if(market != null){
               _markets.add(market);
           }
        }
        return _markets;
    }

    private Market parseMarket(int firstField){
        Market market = new Market();
        int id = getDouble(_marketObjects.get(firstField + _keyMap.get(ID))).intValue();
        market.setId(id);
        String name = (String)_marketObjects.get(firstField + _keyMap.get(NAME));
        market.setName(name);
        double shadowBets = getDouble(_marketObjects.get(firstField + _keyMap.get(SHADOW_BETS)));
        market.setShadowBets(shadowBets);
        double shadowBetRate = getDouble(_marketObjects.get(firstField + _keyMap.get(SHADOW_BET_RATE)));
        market.setShadowBetRate(shadowBetRate);
        Date startedAt = Converter.toDate((String)_marketObjects.get(firstField + _keyMap.get(STARTED_AT)));
        market.setStartedAt(startedAt);
        Date openedAt = Converter.toDate((String)_marketObjects.get(firstField + _keyMap.get(OPENED_AT)));
        market.setOpenedAt(openedAt);
        Date closedAt = Converter.toDate((String)_marketObjects.get(firstField + _keyMap.get(CLOSED_AT)));
        market.setClosedAt(closedAt);
        int sportId = getDouble(_marketObjects.get(firstField + _keyMap.get(SPORT_ID))).intValue();
        market.setSportId(sportId);
        double totalBets = getDouble(_marketObjects.get(firstField + _keyMap.get(TOTAL_BETS)));
        market.setTotalBets(totalBets);
        String state = (String)_marketObjects.get(firstField + _keyMap.get(STATE));
        market.setState(state);
        String marketDuration = (String)_marketObjects.get(firstField + _keyMap.get(MARKET_DURATION));
        market.setMarketDuration(marketDuration);
        String gameType = (String)_marketObjects.get(firstField + _keyMap.get(GAME_TYPE));
        market.setGameType(gameType);
        List<Game> games = parseGames((ArrayList)_marketObjects.get(firstField + _keyMap.get(GAMES)));
        market.setGames(games);
        return market;
    }

    private List<Game> parseGames(ArrayList gameObjects){
        if(gameObjects == null ||
           (gameObjects).size() == 0){
            return null;
        }
        List<Game> games = new ArrayList<Game>();

        for (int i = 0; i < gameObjects.size(); i++){
            LinkedTreeMap objectGame = (LinkedTreeMap)gameObjects.get(i);
            Game game = parseGame(objectGame);
            games.add(game);
        }
        return games;
    }

    private Game parseGame(LinkedTreeMap gameObject){
        Game game = new Game();
        String id = (String)gameObject.get("id");
        game.setId(id);
        String statsId = (String)gameObject.get("stats_id");
        game.setStatsId(statsId);
        String status = (String)gameObject.get("status");
        game.setStatus(status);
        Date gameDay = Converter.toDate((String)gameObject.get("game_day"));
        game.setGameDay(gameDay);
        Date gameTime = Converter.toDate((String)gameObject.get("game_time"));
        game.setGameTime(gameTime);
        String homeTeam = (String)gameObject.get("home_team");
        game.setHomeTeam(homeTeam);
        String awayTeam = (String)gameObject.get("away_team");
        game.setAwayTeam(awayTeam);
        String seasonType = (String)gameObject.get("season_type");
        game.setSeasonType(seasonType);
        int seasonWeek = getDouble(gameObject.get("season_week")).intValue();
        game.setSeasonWeek(seasonWeek);
        int seasonYear = getDouble (gameObject.get("season_year")).intValue();
        game.setSeasonYear(seasonYear);
        String network = (String)gameObject.get("network");
        game.setNetwork(network);
        return game;
    }

    private void fillKeyMap(){
        _keyMap = new HashMap<String, Integer>();
        for (int i = 1; i <= _fieldsNumber; i++){
           String field = (String)_marketObjects.get(i);
            if(atemptPutKey(ID, field, i)){
                continue;
            }else if(atemptPutKey(NAME, field, i)){
                continue;
            }else if(atemptPutKey(SHADOW_BETS, field, i)){
                continue;
            }else if(atemptPutKey(SHADOW_BET_RATE, field, i)){
                continue;
            }else if(atemptPutKey(STARTED_AT, field, i)){
                continue;
            }else if(atemptPutKey(OPENED_AT, field, i)){
                continue;
            }else if(atemptPutKey(CLOSED_AT, field, i)){
                continue;
            }else if(atemptPutKey(SPORT_ID, field, i)){
                continue;
            }else if(atemptPutKey(TOTAL_BETS, field, i)){
                continue;
            }else if(atemptPutKey(STATE, field, i)){
                continue;
            }else if(atemptPutKey(MARKET_DURATION, field, i)){
                continue;
            }else if(atemptPutKey(GAME_TYPE, field, i)){
                continue;
            }else if(atemptPutKey(GAMES, field, i)){
                continue;
            }
        }
    }

    private boolean atemptPutKey(String fieldName, String comparedField, int index){
        if(fieldName.equalsIgnoreCase(comparedField)){
            _keyMap.put(fieldName, index);
            return true;
        }
        return false;
    }

    private Double getDouble(Object value){
        if(value instanceof Double){
            return (Double)value;
        }
        return Double.parseDouble((String)value);
    }

}
