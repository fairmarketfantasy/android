package com.fantasysport.parsers;

import com.fantasysport.models.FGame;
import com.fantasysport.models.Market;
import com.fantasysport.models.Prediction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by bylynka on 3/19/14.
 */
public class PredictionParser extends BaseParser {

    private final String MARKET = "market";
    private final String STATE = "state";
    private final String SCORE = "score";
    private final String CONTEST_RANK = "contest_rank";
    private final String CONTEST_TYPE = "contest_type";
    private final String ID = "id";
    private final String CONTEST_RANK_PAYOUT = "contest_rank_payout";

    public List<Prediction> parse(String json){
        BsonDataWrapper bsonData = getBsonData(json);
        if(bsonData == null){
            return  null;
        }
        initParams(bsonData);

        List<Prediction> items = new ArrayList<Prediction>();
        int itemsNumber = (_objects.size() - _fieldsNumber - 1)/_fieldsNumber;
        for (int i = 1; i <= itemsNumber; i++){
            Prediction item = parseItem(_fieldsNumber * i);
            if(item != null){
                items.add(item);
            }
        }
        return items;
    }

    private Prediction parseItem(int firstField){
        Prediction prediction  = new Prediction();
        int id = getDouble(_objects.get(firstField + _keyMap.get(ID))).intValue();
        prediction.setId(id);
        String state = (String) _objects.get(firstField + _keyMap.get(STATE));
        prediction.setState(state);
        double score = getDouble(_objects.get(firstField + _keyMap.get(SCORE)));
        prediction.setScore(score);
        int rank = getDouble(_objects.get(firstField + _keyMap.get(CONTEST_RANK))).intValue();
        prediction.setRank(rank);
        int award = getDouble(_objects.get(firstField + _keyMap.get(CONTEST_RANK_PAYOUT))).intValue();
        prediction.setAward(award);
        Market market = parseMarket((LinkedHashMap<String, Object>)_objects.get(firstField + _keyMap.get(MARKET)));
        prediction.setMarket(market);
        LinkedHashMap<String, Object> predictionDetail = (LinkedHashMap<String, Object>)_objects.get(firstField + _keyMap.get(CONTEST_TYPE));
        String contestType = parseContestType(predictionDetail);
        prediction.setContestType(contestType);
        int maxEntries = parseMaxEntries(predictionDetail);
        prediction.setMaxEntries(maxEntries);
        return prediction;
    }

    private String parseContestType(LinkedHashMap<String, Object> object){
       return (String)object.get("name");
    }

    private int parseMaxEntries(LinkedHashMap<String, Object> object){
        return getDouble(object.get("max_entries")).intValue();
    }

    private Market parseMarket(LinkedHashMap<String, Object> object){
        Market market = new Market();
        String name = (String)object.get("name");
        market.setName(name);
        List<FGame> games = parseGames((ArrayList)object.get("games"));
        market.setGames(games);
        return market;
    }

    @Override
    protected void fillKeyMap(){
        _keyMap = new HashMap<String, Integer>();
        for (int i = 1; i <= _fieldsNumber; i++){
            String field = (String) _objects.get(i);
            if(atemptPutKey(MARKET, field, i)){
                continue;
            }else if(atemptPutKey(SCORE, field, i)){
                continue;
            }else if(atemptPutKey(CONTEST_RANK, field, i)){
                continue;
            }else if(atemptPutKey(STATE, field, i)){
                continue;
            }else if(atemptPutKey(CONTEST_TYPE, field, i)){
                continue;
            }else if(atemptPutKey(ID, field, i)){
                continue;
            }else if(atemptPutKey(CONTEST_RANK_PAYOUT, field, i)){
                continue;
            }
        }
    }
}
