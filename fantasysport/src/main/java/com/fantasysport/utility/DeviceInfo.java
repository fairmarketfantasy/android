package com.fantasysport.utility;

import android.content.Context;
import android.media.AudioManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by bylynka on 3/22/14.
 */
public class DeviceInfo {

    private static Integer _gmtInMinutes = null;

    public static int getGMTInMinutes() {
        if (_gmtInMinutes == null) {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
            String timeZone = new SimpleDateFormat("Z").format(calendar.getTime());
            int hours = Integer.parseInt(timeZone.substring(1, 3));
            int minutes = Integer.parseInt(timeZone.substring(3, 5));
            int sign = timeZone.substring(0, 1).equalsIgnoreCase("+") ? 1 : -1;
            _gmtInMinutes = ((hours * 60) + minutes) * sign;
        }
        return _gmtInMinutes;
    }

    public static int getCurrentMediaVolume(Context context){
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return audio.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public static int getMaxMediaVolume(Context context){
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }
}
