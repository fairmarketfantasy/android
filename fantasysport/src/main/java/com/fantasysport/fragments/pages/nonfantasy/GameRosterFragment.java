package com.fantasysport.fragments.pages.nonfantasy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import com.fantasysport.R;
import com.fantasysport.activities.MainActivity;
import com.fantasysport.adapters.nonfantasy.*;
import com.fantasysport.fragments.BaseActivityFragment;
import com.fantasysport.fragments.NFMediator;
import com.fantasysport.fragments.main.NonFantasyFragment;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.requestListeners.SubmitNFRosterResponseListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 5/16/14.
 */
public class GameRosterFragment extends BaseActivityFragment implements NFRosterAdapter.IListener, NFMediator.IGameSelectedListener {

    private NFRosterAdapter _adapter;
    private NFMediator _mediator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_non_fantasy_game_page, container, false);
        init();
        return _rootView;
    }

    private void init() {
        NonFantasyFragment fragment = (NonFantasyFragment) ((MainActivity) getActivity()).getRootFragment();
        _mediator = fragment.getMediator();
        _mediator.addGameSelectedListener(this);
        Button submitBtn = getViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(_submitBtnOnClickListener);
        initAdapter();
    }

    private void initAdapter() {
        int roomNumber = getStorage().getNFDataContainer().getRoster().getRoomNumber();
        ListView listView = getViewById(R.id.game_list);
        List<INFGame> games = new ArrayList<INFGame>(roomNumber);
        for (int i = 0; i < roomNumber; i++) {
            games.add(new EmptyNFGame());
        }
        _adapter = new NFRosterAdapter(games, getActivity());
        _adapter.setListener(this);
        listView.setAdapter(_adapter);
    }

    @Override
    public void onDismiss(NFGameWrapper gameWrapper) {

    }

    @Override
    public void onSelectedGame(Object sender, NFGameWrapper gameWrapper) {
        List<INFGame> games = _adapter.getGames();
        for (int i = 0; i < games.size(); i++){
            if(games.get(i) instanceof EmptyNFGame){
                games.set(i, gameWrapper);
                _adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    public void submitRoster(List<NFGameWrapper> gameWrappers){
        showProgress();
        getWebProxy().submitNFRoster(gameWrappers, new SubmitNFRosterResponseListener() {
            @Override
            public void onRequestError(RequestError error) {
                dismissProgress();
                showAlert(getString(R.string.error), error.getMessage());
            }

            @Override
            public void onRequestSuccess(String responseMsg) {
                dismissProgress();
                showAlert(getString(R.string.info), responseMsg);
            }
        });
    }

    private List<NFGameWrapper> getGameWrappers(){
        List<INFGame> games = _adapter.getGames();
        List<NFGameWrapper> gameWrappers = new ArrayList<NFGameWrapper>();
        for (INFGame game : games){
            if(game instanceof NFGameWrapper){
                gameWrappers.add((NFGameWrapper)game);
            }
        }
        return gameWrappers;
    }

    View.OnClickListener _submitBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            submitRoster(getGameWrappers());
        }
    };
}
