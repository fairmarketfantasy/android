package com.fantasysport.parsers;

import com.fantasysport.models.FGame;
import com.fantasysport.utility.Converter;
import com.fantasysport.utility.DateUtils;
import com.fantasysport.utility.DeviceInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

/**
 * Created by bylynka on 2/26/14.
 */
public abstract class BaseParser {

    protected int _fieldsNumber;
    protected HashMap<String, Integer> _keyMap;
    protected List<Object> _objects;

    protected boolean atemptPutKey(String fieldName, String comparedField, int index) {
        if (fieldName.equalsIgnoreCase(comparedField)) {
            _keyMap.put(fieldName, index);
            return true;
        }
        return false;
    }

    protected ObjectMapper getObjectMapper(){
       ObjectMapper mapper = new ObjectMapper();
        return mapper;
    }

    protected BsonDataWrapper getBsonData(String json) {
        BsonDataWrapper bsonData = null;
        try {
            bsonData = getObjectMapper().readValue(json, BsonDataWrapper.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bsonData == null ||
                bsonData.getDataList() == null ||
                bsonData.getDataList().size() == 0) {
            return null;
        }
        return bsonData;
    }

    protected Double getDouble(Object value) {
        try {
            if (value == null) {
                return 0d;
            }
            if (value instanceof Double) {
                return (Double) value;
            }
            if (value instanceof Integer) {
                double res = (double) ((int) ((Integer) value));
                return res;
            }
            return Double.parseDouble((String) value);
        } catch (Exception e) {
            return 0d;
        }
    }

    protected void initParams(BsonDataWrapper bsonData) {
        _objects = bsonData.getDataList();
        _fieldsNumber = ((Double) _objects.get(0)).intValue();
        fillKeyMap();
    }

    protected List<FGame> parseGames(ArrayList gameObjects) {
        if (gameObjects == null ||
                (gameObjects).size() == 0) {
            return null;
        }
        List<FGame> games = new ArrayList<FGame>();

        for (int i = 0; i < gameObjects.size(); i++) {
            LinkedHashMap<String, Object> objectGame = (LinkedHashMap<String, Object>) gameObjects.get(i);
            FGame game = parseGame(objectGame);
            games.add(game);
        }
        Collections.sort(games);
        return games;
    }

    protected FGame parseGame(LinkedHashMap<String, Object> gameObject) {
        int gmt = DeviceInfo.getGMTInMinutes();
        FGame game = new FGame();
//        String id = (String)gameObject.get("id");
//        game.setId(id);
//        String statsId = (String)gameObject.get("stats_id");
//        game.setStatsId(statsId);
//        String status = (String)gameObject.get("status");
//        game.setStatus(status);
        Date gameTime = Converter.toDate((String) gameObject.get("game_time"));
        gameTime = DateUtils.addMinutes(gameTime, gmt);
        game.setGameTime(gameTime);
        String homeTeam = (String) gameObject.get("home_team");
        game.setHomeTeam(homeTeam);
        String awayTeam = (String) gameObject.get("away_team");
        game.setAwayTeam(awayTeam);
        return game;
    }

    protected abstract void fillKeyMap();

}
