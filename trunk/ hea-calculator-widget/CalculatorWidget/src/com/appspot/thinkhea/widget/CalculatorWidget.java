package com.appspot.thinkhea.widget;

import com.appspot.thinkhea.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class CalculatorWidget extends AppWidgetProvider {	
	private static final String ACTION_WIDGET_CONTROL = "com.appspot.thinkhea.widget.WIDGET_CONTROL";
	public static final String URI_SCHEME = "calculator_widget";
	private static final String LOG_TAG = "ImagesWidgetProvider";
	
	@Override 
	public void onReceive(Context context, Intent intent) { 
		final String action = intent.getAction();
		Log.i("CalculatorWidget", "onReceive() ACTION = "+action);
		if (ACTION_WIDGET_CONTROL.equals(action)) {
            // pass this on to the action handler where we'll figure out what to do and update the widget
            final int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            	Log.i(LOG_TAG, "onReceive() ACTION = "+ACTION_WIDGET_CONTROL);
            	Toast.makeText(context, "onReceive() ACTION V", Toast.LENGTH_LONG).show();
            }else{
            	Log.i(LOG_TAG, "onReceive() ACTION = "+ACTION_WIDGET_CONTROL+" INVALID_APPWIDGET_ID");
            	Toast.makeText(context, "onReceive() ACTION F", Toast.LENGTH_LONG).show();
            }
        }else{
			Log.i(LOG_TAG, "super.onReceive");
			super.onReceive(context, intent);
		}
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.i(LOG_TAG, "onUpdate()");
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			
			// Create an Intent to launch CalculatorWidget
			// Intent intent = new Intent(context, CalculatorWidget.class);
            // PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            
            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews remoteView = new RemoteViews(context.getPackageName(),
            		R.layout.simple_calculator);
            
            addOnClickListerners(remoteView,context,appWidgetId);
            
			//appWidgetManager.updateAppWidget(appWidgetId, view);
            AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, remoteView);
		}
	}

	private void addOnClickListerners(RemoteViews remoteView,Context context,int appWidgetId) {
		Log.i(LOG_TAG, "addOnClickListerners()-START");
		remoteView.setOnClickPendingIntent(R.id.Button00, makeControlPendingIntent(context, "Button00", appWidgetId));
		Log.i(LOG_TAG, "addOnClickListerners()-END");
	}
	
	private PendingIntent makeControlPendingIntent(Context context, String command, int appWidgetId) {
		Log.i(LOG_TAG, "makeControlPendingIntent()");
        Intent active = new Intent(ACTION_WIDGET_CONTROL);
//        active.setAction(ACTION_WIDGET_CONTROL);
//        active.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
//        Uri data = Uri.withAppendedPath(Uri.parse(URI_SCHEME + "://widget/id/#"+command), String.valueOf(appWidgetId));
//        active.setData(data);
        return(PendingIntent.getBroadcast(context, 0, active, PendingIntent.FLAG_CANCEL_CURRENT));
    }
}

