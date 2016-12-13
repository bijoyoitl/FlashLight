package com.optimalbd.flashlight;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Created by ripon on 12/1/2016.
 */

public class LightWidget extends AppWidgetProvider {
    private static final String ACTION_CLICK = "com.optimalbd.flashlight.action.widget.click";
    private static RemoteViews rv;
    PowerWork powerWork;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appwidgetId : appWidgetIds) {
            appWidgetUpdate(context, appWidgetManager, appwidgetId);
        }

    }

    private void appWidgetUpdate(Context context, AppWidgetManager appWidgetManager, int appwidgetId) {
        rv = new RemoteViews(context.getPackageName(), R.layout.light_widget);
        rv.setOnClickPendingIntent(R.id.lightImageView, PendingIntent.getBroadcast(context,0,new Intent(ACTION_CLICK),0));
        appWidgetManager.updateAppWidget(appwidgetId, rv);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean on_off = false;
        super.onReceive(context, intent);
        if (rv == null) {
            rv= new RemoteViews(context.getPackageName(),R.layout.light_widget);
        }

        if (intent.getAction().equals(ACTION_CLICK)){

            if (Utility.powerOnOff) {
                if (!Utility.powerOnOff) {
                    on_off = true;
                }
                Utility.powerOnOff = on_off;
                rv.setImageViewResource(R.id.lightImageView, R.drawable.w_deactive);
                Utility.powerWork.playSound(context);
                Utility.powerWork.lightOff();
                Utility.powerWork.Destroy();
            } else {
                if (!Utility.powerOnOff) {
                    on_off = true;
                }
                Utility.powerOnOff = on_off;
                rv.setImageViewResource(R.id.lightImageView,R.drawable.w_active);

                Utility.powerWork = new PowerWork();
                Utility.powerWork.playSound(context);
                Utility.powerWork.lightOn();

            }
        }
        AppWidgetManager appWidgetManger = AppWidgetManager.getInstance(context);
        appWidgetManger.updateAppWidget(appWidgetManger.getAppWidgetIds(new ComponentName(context, LightWidget.class)), rv);
    }
}
