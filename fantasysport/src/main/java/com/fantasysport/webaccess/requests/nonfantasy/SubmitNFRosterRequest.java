package com.fantasysport.webaccess.requests.nonfantasy;

import android.net.Uri;
import com.fantasysport.models.nonfantasy.NFTeam;
import com.fantasysport.webaccess.requests.BaseRequest;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 5/19/14.
 */
public class SubmitNFRosterRequest extends BaseRequest<String> {

    private RequestBody _body;

    public SubmitNFRosterRequest(List<NFTeam> teams) {
        super(String.class);
        initRequestBody(teams);
    }

    private void initRequestBody(List<NFTeam> teams){
        if(teams == null){
            return;
        }
        List<RosterTeam> rosterTeams = new ArrayList<RosterTeam>(teams.size());
        for(int i = 0; i < teams.size(); i++){
            NFTeam team = teams.get(i);
            rosterTeams.add(new RosterTeam(team.getGameStatsId(), team.getStatsId(), i));
        }
        _body = new RequestBody(rosterTeams);
    }

    @Override
    public String loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("game_rosters")
                .appendQueryParameter("access_token", getAccessToken());
        String url = uriBuilder.build().toString();
        Gson gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.STATIC)
                .create();
        String js = _body != null? gson.toJson(_body): null;
        HttpContent content = ByteArrayContent.fromString("application/json", js);
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), content);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return result;
    }

    public class RequestBody{
        @SerializedName("teams")
        private List<RosterTeam> _teams;

        public RequestBody(List<RosterTeam> teams){
            _teams = teams;
        }
    }

    public class RosterTeam{

        @SerializedName("game_stats_id")
        private String _gameStatsId;

        @SerializedName("team_stats_id")
        private String _teamStatsId;

        @SerializedName("position_index")
        private int _index;

        public RosterTeam(int gameStatsId, int teamStatsId, int index){
            _gameStatsId = Integer.toString(gameStatsId);
            _teamStatsId  = Integer.toString(teamStatsId);
            _index = index;
        }
    }

}
