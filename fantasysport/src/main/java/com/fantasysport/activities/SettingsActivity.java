package com.fantasysport.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.adapters.SettingsItemEnum;
import com.fantasysport.adapters.SettinsAdapter;
import com.fantasysport.models.User;
import com.fantasysport.models.UserData;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.requestListeners.UpdateUserResponseListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 3/11/14.
 */
public class SettingsActivity extends BaseActivity {

    private final int TAKE_PHOTO = 42;
    private final int CHOOSE_PHOTO = 24;

    private SettinsAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHeaderText("SETTINGS");
        setSettingsList();
    }

    private void setSettingsList() {
        ListView listView = getViewById(R.id.settings_list);
        List<SettingsItemEnum> items = new ArrayList<SettingsItemEnum>();
        items.add(SettingsItemEnum.Avatar);
        items.add(SettingsItemEnum.Name);
        items.add(SettingsItemEnum.Email);
        items.add(SettingsItemEnum.Password);
        _adapter = new SettinsAdapter(this, _storage.getUserData(), items);
        listView.setAdapter(_adapter);
        listView.setOnItemClickListener(_settinsItemClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (_adapter == null) {
            return;
        }
        _adapter.setUserData(_storage.getUserData());
        _adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        try {
            Uri selectedImageUri = data.getData();
            Bitmap bm;
            if(selectedImageUri != null){
                bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
            }else {
                Bundle extras = data.getExtras();
                bm = (Bitmap) extras.get("data");
            }
            uploadAvatar(bm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadAvatar(Bitmap bitmap){
        showProgress();
        _webProxy.uploadAva(bitmap,SettingsActivity.this.getCurrentUser(), new UpdateUserResponseListener() {
            @Override
            public void onRequestError(RequestError message) {
                dismissProgress();
            }

            @Override
            public void onRequestSuccess(UserData userData) {
                dismissProgress();
                SettingsActivity.this.setResult(Const.NEW_AVATAR);
                _adapter.notifyDataSetChanged();
//                SettingsActivity.this.finish();
            }
        });
    }

    private User getCurrentUser(){
        User user = new User();
        UserData data = _storage.getUserData();
        user.setRealName(data.getRealName());
        user.setId(data.getId());
        user.setEmail(data.getEmail());
        return user;
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
            SettingsItemEnum itemEnum = (SettingsItemEnum) _adapter.getItem(position);
            if (itemEnum == SettingsItemEnum.Avatar) {
                selectImage();
                return;
            }
            Intent intent = new Intent(SettingsActivity.this, UpdateUserActivity.class);
            intent.putExtra(Const.SETTINGS_ITEM_ENUM, itemEnum);
            startActivity(intent);
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
    };

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, TAKE_PHOTO);
                } else if (items[item].equals("Choose from Library")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(
                            pickPhoto, CHOOSE_PHOTO);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

}
