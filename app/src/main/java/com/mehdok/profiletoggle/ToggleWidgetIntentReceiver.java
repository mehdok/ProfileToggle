package com.mehdok.profiletoggle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Vibrator;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Created by Mehdi Sohrabi (mehdok@gmail.com) on 3/27/2015.
 */
public class ToggleWidgetIntentReceiver extends BroadcastReceiver
{
    private final int VIBRATE_TIME = 300; //milli
    @Override
    public void onReceive(Context context, Intent intent)
    {
        //Log.e(ToggleProfileWidget.TAG, "onReceive");
        if (intent.getAction().equals(ToggleProfileWidget.CHANGE_PROFILE))
        {
            //Log.e(ToggleProfileWidget.TAG, "onReceive - CHANGE_PROFILE");
            setNextWidgetState(context);
        }
        else if(intent.getAction().equals("android.media.RINGER_MODE_CHANGED"))
        {
            //Log.e(ToggleProfileWidget.TAG, "onReceive - RINGER_MODE_CHANGED");
            updateWidgetState(context);
        }
    }

    private void setNextWidgetState(Context context)
    {
        //Log.e(ToggleProfileWidget.TAG, "setNextWidgetState");
        setNextProfile(context, getCurrentProfile(context));
        //RemoteViews rv = updateWidgetImage(context, getCurrentProfile(context));
        //ToggleProfileWidget.pushWidgetUpdate(context.getApplicationContext(), rv);
    }

    private void updateWidgetState(Context context)
    {
        RemoteViews rv = updateWidgetImage(context, getCurrentProfile(context));
        ToggleProfileWidget.pushWidgetUpdate(context.getApplicationContext(), rv);
        RemoteViews rv2 = updateWidgetImageLarge(context, getCurrentProfile(context));
        ToggleProfileWidgetLarge.pushWidgetUpdate(context.getApplicationContext(), rv2);
    }

    private int getCurrentProfile(Context context)
    {
        //Log.e(ToggleProfileWidget.TAG, "getCurrentProfile");
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return am.getRingerMode();
    }

    private void setNextProfile(Context context, int current)
    {
        //Log.e(ToggleProfileWidget.TAG, "setNextProfile");
        if(current == AudioManager.RINGER_MODE_NORMAL)
        {
            AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            Toast.makeText(context, R.string.silent, Toast.LENGTH_SHORT).show();
        }
        else if(current == AudioManager.RINGER_MODE_SILENT)
        {
            //String vs = Context.VIBRATOR_SERVICE;
            Vibrator mVibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
            boolean isVibrator = true;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            {
                isVibrator = mVibrator.hasVibrator();
            }

            if ( isVibrator )
            {
                AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                vibrateDevice(mVibrator);
                Toast.makeText(context, R.string.vibrate,Toast.LENGTH_SHORT).show();
            }
            else
            {
                AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                Toast.makeText(context, R.string.normal,Toast.LENGTH_SHORT).show();
            }
        }
        else if(current == AudioManager.RINGER_MODE_VIBRATE)
        {
            AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            Toast.makeText(context, R.string.normal,Toast.LENGTH_SHORT).show();
        }
        else
        {
            AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            Toast.makeText(context, R.string.normal,Toast.LENGTH_SHORT).show();
        }
    }

    private RemoteViews updateWidgetImage(Context context, int state)
    {
        //Log.e(ToggleProfileWidget.TAG, "updateWidgetImage");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        remoteViews.setOnClickPendingIntent(R.id.widgetLayout, ToggleProfileWidget.buildButtonPendingIntent(context));
        changeIcon(remoteViews, state);
        return remoteViews;
    }

    private RemoteViews updateWidgetImageLarge(Context context, int state)
    {
        //Log.e(ToggleProfileWidget.TAG, "updateWidgetImageLarge");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout_large);
        remoteViews.setOnClickPendingIntent(R.id.widgetLayoutLarge, ToggleProfileWidgetLarge.buildButtonPendingIntent(context));
        changeIconLarge(remoteViews, state);
        return remoteViews;
    }

    private void changeIcon(RemoteViews remoteViews, int current)
    {
        //Log.e(ToggleProfileWidget.TAG, "changeIcon");

        if(current == AudioManager.RINGER_MODE_NORMAL)
        {
            remoteViews.setImageViewResource(R.id.profileImage, R.drawable.volume);
        }
        else if(current == AudioManager.RINGER_MODE_SILENT)
        {
            remoteViews.setImageViewResource(R.id.profileImage, R.drawable.silent);
        }
        else if(current == AudioManager.RINGER_MODE_VIBRATE)
        {
            remoteViews.setImageViewResource(R.id.profileImage, R.drawable.vibration);
        }
        else
        {
            remoteViews.setImageViewResource(R.id.profileImage, R.drawable.volume);
        }
    }

    private void changeIconLarge(RemoteViews remoteViews, int current)
    {
        //Log.e(ToggleProfileWidget.TAG, "changeIconLarge");

        if(current == AudioManager.RINGER_MODE_NORMAL)
        {
            remoteViews.setImageViewResource(R.id.profileImageLarge, R.drawable.volume_x2);
        }
        else if(current == AudioManager.RINGER_MODE_SILENT)
        {
            remoteViews.setImageViewResource(R.id.profileImageLarge, R.drawable.silent_x2);
        }
        else if(current == AudioManager.RINGER_MODE_VIBRATE)
        {
            remoteViews.setImageViewResource(R.id.profileImageLarge, R.drawable.vibration_x2);
        }
        else
        {
            remoteViews.setImageViewResource(R.id.profileImageLarge, R.drawable.volume_x2);
        }

    }

    private void vibrateDevice(Vibrator mVibrator)
    {
        mVibrator.vibrate(VIBRATE_TIME);
    }
}
