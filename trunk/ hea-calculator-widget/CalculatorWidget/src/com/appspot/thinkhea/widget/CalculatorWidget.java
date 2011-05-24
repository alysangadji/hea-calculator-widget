package com.appspot.thinkhea.widget;

import com.appspot.thinkhea.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class CalculatorWidget extends AppWidgetProvider {
		
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.i("CalculatorWidget", "onUpdate()");
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			
			// Create an Intent to launch CalculatorWidget
			Intent intent = new Intent(context, CalculatorWidget.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            
            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(),
            		R.layout.simple_calculator);
            views.setOnClickPendingIntent(R.id.Button00, pendingIntent);
            
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}
}

