package com.fantasysport.webaccess.requests;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;
import com.fantasysport.models.Avatar;
import com.fantasysport.models.User;
import com.fantasysport.models.UserData;
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
        Avatar avatar = new Avatar(imgInBase64, "horror-photo-2.jpg", "horror-photo-2.jpg" );
        _user.setAvatar(avatar);
        UserRequestBody body = new UserRequestBody(_user);
        String js = new Gson().toJson(body);
        HttpContent content = ByteArrayContent.fromString("application/json", js);
        HttpRequest request = getHttpRequestFactory()
                .buildPutRequest(new GenericUrl(url), content);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return new Gson().fromJson(result, UserData.class);
    }

//    @Override
//    public UserData loadDataFromNetwork() throws Exception {
//        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
//        uriBuilder.appendPath("users")
//                .appendPath("uploads")
//                .appendQueryParameter("access_token", getAccessToken());
//        String url = uriBuilder.build().toString();
//
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        _bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] byteArray = stream.toByteArray();
//        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
//
//        MultipartContent content = new MultipartContent();
//        content.setMediaType(new HttpMediaType("multipart/form-data"));
//        HttpContent byteArrayContent = ByteArrayContent.fromString("image/jpeg", encoded);
//        MultipartContent.Part part =  new MultipartContent.Part(byteArrayContent);
//        HttpHeaders partHeaders = new HttpHeaders();
////        partHeaders.setContentType("image/jpeg");
//        partHeaders.set("Content-Disposition","form-data; name=\"field\"; filename=\"horror-photo-2.jpg\"");
//        partHeaders.setAcceptEncoding(null);
//        part.setHeaders(partHeaders);
////        part.getHeaders().set("Content-Disposition","")
//        content.addPart(part);
////        content.addPart(new MultipartContent.Part())
//        HttpRequest request = getHttpRequestFactory()
//                .buildPostRequest(new GenericUrl(url), content);
////        request.getHeaders().setAccept("application/json");
//        String result = request.execute().parseAsString();
//
//        return new Gson().fromJson(result, UserData.class);
//    }
}
