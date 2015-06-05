package com.mehdok.profiletoggle;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.RemoteViews;

/**
 * Created by Mehdi Sohrabi (mehdok@gmail.com) on 3/23/2015.
 */
public class ToggleProfileWidget extends AppWidgetProvider
{
    public static final String CHANGE_PROFILE = "com.mehdok.profiletoggle.CHANGE_PROFILE";
    private static int currentState = 0;
    public static final String TAG = "profile_changer";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        //Log.e(TAG, "onUpdate");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        remoteViews.setOnClickPendingIntent(R.id.widgetLayout, buildButtonPendingIntent(context));
        updateWidgetImageFirstTime(context, remoteViews);
        pushWidgetUpdate(context, remoteViews);
        //super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    /**
     *
     * @param context
     * @return pending intent click listener
     */
    public static PendingIntent buildButtonPendingIntent(Context context)
    {
        //Log.e(TAG, "buildButtonPendingIntent");
        Intent intent = new Intent();
        intent.setAction(CHANGE_PROFILE);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * update widget state
     * @param context
     * @param remoteViews
     */
    public static void pushWidgetUpdate(Context context, RemoteViews remoteViews)
    {
        //Log.e(TAG, "buildButtonPendingIntent");
        ComponentName myWidget = new ComponentName(context, ToggleProfileWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }

    protected void updateWidgetImageFirstTime(Context context, RemoteViews remoteViews)
    {
        //Log.e(TAG, "updateWidgetImageFirstTime");
        changeIcon(remoteViews, getCurrentProfile(context));
    }

    private int getCurrentProfile(Context context)
    {
        //Log.e(TAG, "getCurrentProfile");
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return am.getRingerMode();
    }

    private void changeIcon(RemoteViews remoteViews, int current)
    {
        //Log.e(TAG, "changeIcon");
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
}
