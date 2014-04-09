package com.fantasysport.webaccess.requests;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;
import com.fantasysport.models.Avatar;
import com.fantasysport.models.User;
import com.fantasysport.models.UserData;
import com.fantasysport.utility.DateUtils;
import com.fantasysport.utility.Size;
import com.fantasysport.utility.image.BitmapUtils;
import com.google.api.client.http.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Modifier;

/**
 * Created by bylynka on 3/12/14.
 */
public class UploadAvaRequest extends BaseRequest<UserData> {

    private Bitmap _bitmap;
    private User _user;

    public UploadAvaRequest(Bitmap bitmap, User user) {
        super(UserData.class);
        _user = user;
        _bitmap = bitmap;
    }

    @Override
    public UserData loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon()
                .appendPath("users")
                .appendQueryParameter("access_token", getAccessToken());
        String url = uriBuilder.build().toString();
        Bitmap bitmap = BitmapUtils.resize(_bitmap, new Size(100, 100));
        String imgInBase64 = BitmapUtils.toBase64(bitmap);
        bitmap.recycle();
        _bitmap.recycle();
        String avaName = String.format("%d%s", DateUtils.getCurrentDate().getTime(), "horror-photo.jpg");
        Avatar avatar = new Avatar(imgInBase64, avaName, avaName);
        _user.setAvatar(avatar);
        UserRequestBody body = new UserRequestBody(_user);
        String js = new Gson().toJson(body);
        HttpContent content = ByteArrayContent.fromString("application/json", js);
        HttpRequest request = getHttpRequestFactory()
                .buildPutRequest(new GenericUrl(url), content);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        UserData data = new Gson().fromJson(result, UserData.class);
        _rHelper.loadUserData(data);
        return data;
    }

}
