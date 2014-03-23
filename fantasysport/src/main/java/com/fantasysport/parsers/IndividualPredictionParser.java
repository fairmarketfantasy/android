package com.fantasysport.parsers;

import com.fantasysport.models.*;
import com.fantasysport.utility.Converter;
import com.fantasysport.utility.DateUtils;
import com.fantasysport.utility.DeviceInfo;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by bylynka on 3/19/14.
 */
public class IndividualPredictionParser extends BaseParser {

    private final String MARKET_NAME = "market_name";
    private final String PLAYER_NAME = "player_name";
    private final String GAME_TIME = "game_time";
    private final String AWARD = "award";
    private final String PT = "pt";
    private final String EVENT_PREDICTIONS = "event_predictions";


    public List<IndividualPrediction> parse(String json) {
        BsonDataWrapper bsonData = getBsonData(json);
        if (bsonData == null) {
            return null;
        }
        initParams(bsonData);

        List<IndividualPrediction> items = new ArrayList<IndividualPrediction>();
        int itemsNumber = (_objects.size() - _fieldsNumber - 1) / _fieldsNumber;
        for (int i = 1; i <= itemsNumber; i++) {
            IndividualPrediction item = parseItem(_fieldsNumber * i);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }

    private IndividualPrediction parseItem(int firstField) {
        IndividualPrediction prediction = new IndividualPrediction();
        String marketName = (String) _objects.get(firstField + _keyMap.get(MARKET_NAME));
        prediction.setMarketName(marketName);
        String playerName = (String) _objects.get(firstField + _keyMap.get(PLAYER_NAME));
        prediction.setPlayerName(playerName);
        Date gameDay = Converter.toDate((String) _objects.get(firstField + _keyMap.get(GAME_TIME)));
        int gmtInMinutes = DeviceInfo.getGMTInMinutes();
        gameDay = DateUtils.addMinutes(gameDay, gmtInMinutes);
        prediction.setGameData(gameDay);
        double award = getDouble(_objects.get(firstField + _keyMap.get(AWARD)));
        prediction.setAward(award);
        double pt = getDouble(_objects.get(firstField + _keyMap.get(PT)));
        prediction.setPT(pt);
        List<StatsItem> statsItems = parseStatsItems((ArrayList) _objects.get(firstField + _keyMap.get(EVENT_PREDICTIONS)));
        prediction.setEventPredictions(statsItems);
        return prediction;
    }

    private List<StatsItem> parseStatsItems(ArrayList objects) {
        if (objects == null ||
                (objects).size() == 0) {
            return null;
        }
        List<StatsItem> items = new ArrayList<StatsItem>();

        for (int i = 0; i < objects.size(); i++) {
            LinkedTreeMap object = (LinkedTreeMap) objects.get(i);
            StatsItem item = parseStatsItem(object);
            items.add(item);
        }
        return items;
    }

    private StatsItem parseStatsItem(LinkedTreeMap object) {
        StatsItem item = new StatsItem();
        String eventType = (String) object.get("event_type");
        item.setName(eventType);
        String mode = (String) object.get("diff");
        item.setMode(mode);
        double value = getDouble(object.get("value"));
        item.setValue(value);
        return item;
    }

    @Override
    protected void fillKeyMap() {
        _keyMap = new HashMap<String, Integer>();
        for (int i = 1; i <= _fieldsNumber; i++) {
            String field = (String) _objects.get(i);
            if (atemptPutKey(MARKET_NAME, field, i)) {
                continue;
            } else if (atemptPutKey(PLAYER_NAME, field, i)) {
                continue;
            } else if (atemptPutKey(GAME_TIME, field, i)) {
                continue;
            } else if (atemptPutKey(AWARD, field, i)) {
                continue;
            } else if (atemptPutKey(PT, field, i)) {
                continue;
            } else if (atemptPutKey(EVENT_PREDICTIONS, field, i)) {
                continue;
            }
        }
    }
}
