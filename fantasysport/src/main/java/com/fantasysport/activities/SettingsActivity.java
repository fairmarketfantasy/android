package com.fantasysport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.adapters.SettingsItemEnum;
import com.fantasysport.adapters.SettinsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 3/11/14.
 */
public class SettingsActivity extends BaseActivity {

    private SettinsAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(true);
        setContentView(R.layout.activity_settings);
        setSettingsList();
    }

    private void setSettingsList(){
        ListView listView = getViewById(R.id.settings_list);
        List<SettingsItemEnum> items = new ArrayList<SettingsItemEnum>();
        items.add(SettingsItemEnum.Avatar);
        items.add(SettingsItemEnum.Name);
//        items.add(SettingsItemEnum.Email);
        items.add(SettingsItemEnum.Password);
        _adapter = new SettinsAdapter(this, _storage.getUserData(), items);
        listView.setAdapter(_adapter);
        listView.setOnItemClickListener(_settinsItemClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(_adapter == null){
            return;
        }
        _adapter.setUserData(_storage.getUserData());
        _adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    AdapterView.OnItemClickListener _settinsItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SettingsItemEnum itemEnum = (SettingsItemEnum)_adapter.getItem(position);
            if(itemEnum == SettingsItemEnum.Avatar){
                return;
            }
            Intent intent = new Intent(SettingsActivity.this, UpdateUserActivity.class);
            intent.putExtra(Const.SETTINGS_ITEM_ENUM, itemEnum);
            startActivity(intent);
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
    };

}
