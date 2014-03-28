package com.fantasysport.parsers;

import com.fantasysport.models.Game;
import com.fantasysport.models.Market;
import com.fantasysport.utility.Converter;
import com.fantasysport.utility.DateUtils;
import com.fantasysport.utility.DeviceInfo;
import com.google.gson.internal.LinkedTreeMap;

import java.util.*;

/**
 * Created by bylynka on 2/19/14.
 */
public class MarketParser extends BaseParser {

    private final String ID = "id";
    private final String NAME = "name";
    private final String SHADOW_BETS = "shadow_bets";
    private final String SHADOW_BET_RATE = "shadow_bet_rate";
    private final String SPORT_ID = "sport_id";
    private final String STATE = "state";
    private final String MARKET_DURATION = "market_duration";
    private final String GAME_TYPE = "game_type";
    private final String GAMES = "games";

    private List<Market> _markets;

    public List<Market> parse(String json) {
        BsonDataWrapper marketListWrapper = getBsonData(json);
        if (marketListWrapper == null) {
            return null;
        }
        _objects = marketListWrapper.getDataList();
        _fieldsNumber = ((Double) _objects.get(0)).intValue();
        fillKeyMap();
        _markets = new ArrayList<Market>();
        int marketsNumber = (_objects.size() - _fieldsNumber - 1)/_fieldsNumber;
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
        int id = getDouble(_objects.get(firstField + _keyMap.get(ID))).intValue();
        market.setId(id);
        String name = (String) _objects.get(firstField + _keyMap.get(NAME));
        market.setName(name);
//        double shadowBets = getDouble(_objects.get(firstField + _keyMap.get(SHADOW_BETS)));
//        market.setShadowBets(shadowBets);
//        double shadowBetRate = getDouble(_objects.get(firstField + _keyMap.get(SHADOW_BET_RATE)));
//        market.setShadowBetRate(shadowBetRate);
        int sportId = getDouble(_objects.get(firstField + _keyMap.get(SPORT_ID))).intValue();
        market.setSportId(sportId);
        String state = (String) _objects.get(firstField + _keyMap.get(STATE));
        market.setState(state);
//        String marketDuration = (String) _objects.get(firstField + _keyMap.get(MARKET_DURATION));
//        market.setMarketDuration(marketDuration);
        String gameType = (String) _objects.get(firstField + _keyMap.get(GAME_TYPE));
        market.setGameType(gameType);
        List<Game> games = parseGames((ArrayList) _objects.get(firstField + _keyMap.get(GAMES)));
        market.setGames(games);
        return market;
    }

    @Override
    protected void fillKeyMap(){
        _keyMap = new HashMap<String, Integer>();
        for (int i = 1; i <= _fieldsNumber; i++){
           String field = (String) _objects.get(i);
            if(atemptPutKey(ID, field, i)){
                continue;
            }else if(atemptPutKey(NAME, field, i)){
                continue;
            }else if(atemptPutKey(SHADOW_BETS, field, i)){
                continue;
            }else if(atemptPutKey(SHADOW_BET_RATE, field, i)){
                continue;
            }else if(atemptPutKey(SPORT_ID, field, i)){
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

}
