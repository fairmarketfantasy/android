package com.fantasysport.fragments.pages.footballwoldcup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.fantasysport.R;
import com.fantasysport.activities.IMainActivity;
import com.fantasysport.adapters.footballwoldcup.PlayersAdapter;
import com.fantasysport.fragments.BaseActivityFragment;
import com.fantasysport.fragments.main.IMainFragment;
import com.fantasysport.models.fwc.FWCData;
import com.fantasysport.models.fwc.IFWCModel;
import com.fantasysport.models.fwc.Player;

import java.util.List;

/**
 * Created by bylynka on 6/3/14.
 */
public class PlayersFragment extends BaseActivityFragment implements FWCMediator.ISubmittedPredictionListener, PlayersAdapter.IListener {

    private PlayersAdapter _adapter;
    private FWCMediator _mediator;
    private final String _predictionType = "mvp";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_fwc_players, container, false);
        init();
        return _rootView;
    }

    private void init(){
        IMainFragment fragment = ((IMainActivity) getActivity()).getRootFragment();
        _mediator = (FWCMediator)fragment.getMediator();
        _mediator.addSubmittedPrediction(this);
        _adapter = new PlayersAdapter(getActivity());
        _adapter.setListener(this);
        FWCData data = getStorage().getFWCData();
        if(data != null){
            _adapter.setPlayers(data.getPlayers());
        }
        ListView listView = getViewById(R.id.players_list);
        listView.setAdapter(_adapter);
    }

    @Override
    public void onSubmittedPrediction(Object sender, IFWCModel predictableItem, String predictionType, String gameStatsId) {
        if(_adapter == null || predictionType == null || predictionType.compareTo(_predictionType) != 0){
            return;
        }
        List<Player> players = _adapter.getPlayers();
        if(players == null){
            return;
        }
        for (Player player : players){
            if(player.getStatsId().compareTo(predictableItem.getStatsId()) == 0){
                player.setIsPredicted(predictableItem.isPredicted());
                _adapter.notifyDataSetChanged();
                break;
            }
        }
        if(getStorage() == null || getStorage().getFWCData() == null){
            return;
        }
        getStorage().getFWCData().setPlayers(players);
        _mediator.updatingData(PlayersFragment.this, getStorage().getFWCData());
    }

    @Override
    public void onSubmittingTeam(Player player) {
        _mediator.submittingPrediction(PlayersFragment.this, player, _predictionType, null);
    }
}
