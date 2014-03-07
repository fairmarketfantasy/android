package com.fantasysport.parsers;

import com.fantasysport.models.Player;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by bylynka on 2/26/14.
 */
public class PlayersParser extends BaseParser {


    private final String ID = "id";
    private final String TEAM = "team";
    private final String NAME = "name";
    private final String POSITION = "position";
    private final String STATUS = "status";
    private final String PPG = "ppg";
    private final String BUY_PRICE = "buy_price";
    private final String SELL_PRICE = "sell_price";
    private final String SCORE = "score";
    private final String HEADSHOT_URL = "headshot_url";
    private final String PURCHASE_PRICE = "purchase_price";
    private final String STATS_ID = "stats_id";

    private List<Object> _playerObjects;

    public List<Player> parse(String json){
        PlayersListWrapper playersListWrapper = new Gson().fromJson(json, PlayersListWrapper.class);
        if (playersListWrapper == null ||
                playersListWrapper.getPlayerList() == null ||
                playersListWrapper.getPlayerList().size() == 0) {
            return null;
        }
        _playerObjects = playersListWrapper.getPlayerList();
        _fieldsNumber = ((Double)_playerObjects.get(0)).intValue();
        fillKeyMap();
        List<Player> players = new ArrayList<Player>();
        int marketsNumber = (_playerObjects.size() - _fieldsNumber - 1)/_fieldsNumber;
        for (int i = 1; i <= marketsNumber; i++){
            Player player = parsePlayer(_fieldsNumber * i);
            if(player != null){
                players.add(player);
            }
        }
        return players;
    }

    private Player parsePlayer(int firstField){
        Player player = new Player();
        int id = getDouble(_playerObjects.get(firstField + _keyMap.get(ID))).intValue();
        player.setId(id);
        String team = (String)_playerObjects.get(firstField + _keyMap.get(TEAM));
        player.setTeam(team);
        String name = (String)_playerObjects.get(firstField + _keyMap.get(NAME));
        player.setName(name);
        String position = (String)_playerObjects.get(firstField + _keyMap.get(POSITION));
        player.setPosition(position);
        String status = (String)_playerObjects.get(firstField + _keyMap.get(STATUS));
        player.setStatus(status);
        double ppg = getDouble(_playerObjects.get(firstField + _keyMap.get(PPG)));
        player.setPPG(ppg);
        double buyPrice = getDouble(_playerObjects.get(firstField + _keyMap.get(BUY_PRICE)));
        player.setBuyPrice(buyPrice);
        double sellPrice = getDouble(_playerObjects.get(firstField + _keyMap.get(SELL_PRICE)));
        player.setSellPrice(sellPrice);
        double purchasePrice = getDouble(_playerObjects.get(firstField + _keyMap.get(PURCHASE_PRICE)));
        player.setPurchasePrice(purchasePrice);
        double score = getDouble(_playerObjects.get(firstField + _keyMap.get(SCORE)));
        player.setScore(score);
        String imgUrl = (String)_playerObjects.get(firstField + _keyMap.get(HEADSHOT_URL));
        player.setImageUrl(imgUrl);
        String playerIds = (String)_playerObjects.get(firstField + _keyMap.get(STATS_ID));
        player.setStatsId(playerIds);
        return player;
    }

    private void fillKeyMap(){
        _keyMap = new HashMap<String, Integer>();
        for (int i = 1; i <= _fieldsNumber; i++){
            String field = (String)_playerObjects.get(i);
            if(atemptPutKey(ID, field, i)){
                continue;
            }else if(atemptPutKey(NAME, field, i)){
                continue;
            }else if(atemptPutKey(TEAM, field, i)){
                continue;
            }else if(atemptPutKey(POSITION, field, i)){
                continue;
            }else if(atemptPutKey(STATUS, field, i)){
                continue;
            }else if(atemptPutKey(PPG, field, i)){
                continue;
            }else if(atemptPutKey(BUY_PRICE, field, i)){
                continue;
            }else if(atemptPutKey(SELL_PRICE, field, i)){
                continue;
            }else if(atemptPutKey(SCORE, field, i)){
                continue;
            }else if(atemptPutKey(HEADSHOT_URL, field, i)){
                continue;
            }else if(atemptPutKey(PURCHASE_PRICE, field, i)){
                continue;
            }else if(atemptPutKey(STATS_ID, field, i)){
                continue;
            }
        }
    }
}
