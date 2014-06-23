package com.fantasysport.webaccess.requests.nonfantasy;

import android.net.Uri;
import com.fantasysport.models.nonfantasy.NFTeam;
import com.fantasysport.webaccess.requests.BaseRequest;
import com.fantasysport.webaccess.responses.MsgResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 5/19/14.
 */
public class SubmitNFRosterRequest extends BaseRequest<String> {

    private RequestBody _body;
    private int _rosterId = 0;

    public SubmitNFRosterRequest(List<NFTeam> teams) {
        super(String.class);
        initRequestBody(teams);
    }

    public SubmitNFRosterRequest(List<NFTeam> teams, int rosterId) {
        super(String.class);
        initRequestBody(teams);
        _rosterId = rosterId;
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
        if(_rosterId > 0){
            uriBuilder.appendPath(Integer.toString(_rosterId));
        }
        String url = uriBuilder.build().toString();
        String js = _body != null? getObjectMapper().writeValueAsString(_body): null;
        HttpContent content = ByteArrayContent.fromString("application/json", js);
        HttpRequest request = _rosterId > 0
                ? getHttpRequestFactory().buildPutRequest(new GenericUrl(url), content)
                : getHttpRequestFactory().buildPostRequest(new GenericUrl(url), content);

        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        MsgResponse msgRes = getObjectMapper().readValue(result, MsgResponse.class);
        return msgRes.getMessage();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RequestBody{
        @JsonProperty("teams")
        private List<RosterTeam> _teams;

        public RequestBody(List<RosterTeam> teams){
            _teams = teams;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RosterTeam{

        @JsonProperty("game_stats_id")
        private String _gameStatsId;

        @JsonProperty("team_stats_id")
        private String _teamStatsId;

        @JsonProperty("position_index")
        private int _index;

        public RosterTeam(String gameStatsId, String teamStatsId, int index){
            _gameStatsId = gameStatsId;
            _teamStatsId  = teamStatsId;
            _index = index;
        }
    }

}
