package com.fantasysport.fragments.pages.nonfantasy;

import android.widget.Button;
import com.fantasysport.R;
import com.fantasysport.models.nonfantasy.INFTeam;
import com.fantasysport.models.nonfantasy.NFTeam;

import java.util.List;

/**
 * Created by bylynka on 5/16/14.
 */
public class GameRosterFragment extends BaseGameRosterFragment{

    private Button _pickBtn;

    @Override
    protected void init() {
        super.init();
        _pickBtn = getViewById(R.id.pick_btn);
        _pickBtn.setText(String.format("%s %d", getString(R.string.pick), getRoomNumber()));
        _pickBtn.setOnClickListener(new OnSubmitListener(true));
        updateSubmitBtnsState();
    }

    @Override
    protected void updateSubmitBtnsState() {
        super.updateSubmitBtnsState();
        updatePickBtnState();
    }

    private void updatePickBtnState(){
        if(_adapter == null){
            return;
        }
        List<INFTeam> teams = _adapter.getTeams();
        if(teams == null){
            _pickBtn.setEnabled(false);
            return;
        }
        int counter = 0;
        for (INFTeam team :_adapter.getTeams()){
            if(team instanceof NFTeam){
                counter++;
            }
        }
        _pickBtn.setEnabled(counter >= getRoomNumber());
    }

}
