package com.fantasysport.parsers;

import com.fantasysport.models.Game;
import com.fantasysport.utility.Converter;
import com.fantasysport.utility.DateUtils;
import com.fantasysport.utility.DeviceInfo;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.util.*;

/**
 * Created by bylynka on 2/26/14.
 */
public abstract class BaseParser {

    protected int _fieldsNumber;
    protected HashMap<String, Integer> _keyMap;
    protected List<Object> _objects;

    protected boolean atemptPutKey(String fieldName, String comparedField, int index){
        if(fieldName.equalsIgnoreCase(comparedField)){
            _keyMap.put(fieldName, index);
            return true;
        }
        return false;
    }

    protected BsonDataWrapper getBsonData(String json){
        BsonDataWrapper bsonData = new Gson().fromJson(json, BsonDataWrapper.class);
        if (bsonData == null ||
                bsonData.getDataList() == null ||
                bsonData.getDataList().size() == 0) {
            return null;
        }
        return bsonData;
    }

    protected Double getDouble(Object value){
        if(value == null){
            return  0d;
        }
        if(value instanceof Double){
            return (Double)value;
        }
        return Double.parseDouble((String)value);
    }

    protected void initParams(BsonDataWrapper bsonData){
        _objects = bsonData.getDataList();
        _fieldsNumber = ((Double) _objects.get(0)).intValue();
        fillKeyMap();
    }

    protected List<Game> parseGames(ArrayList gameObjects){
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
        Collections.sort(games);
        return games;
    }

    protected Game parseGame(LinkedTreeMap gameObject){
        int gmt = DeviceInfo.getGMTInMinutes();
        Game game = new Game();
//        String id = (String)gameObject.get("id");
//        game.setId(id);
//        String statsId = (String)gameObject.get("stats_id");
//        game.setStatsId(statsId);
//        String status = (String)gameObject.get("status");
//        game.setStatus(status);
        Date gameTime = Converter.toDate((String) gameObject.get("game_time"));
        gameTime = DateUtils.addMinutes(gameTime, gmt);
        game.setGameTime(gameTime);
        String homeTeam = (String)gameObject.get("home_team");
//        game.setHomeTeam(homeTeam);
//        String awayTeam = (String)gameObject.get("away_team");
//        game.setAwayTeam(awayTeam);
        return game;
    }

    protected abstract void fillKeyMap();

}
