package com.appspot.thinkhea.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.appspot.thinkhea.R;

public class CalculatorWidget extends AppWidgetProvider {
	private static final String ACTION_WIDGET_CONTROL = "com.appspot.thinkhea.widget.WIDGET_CONTROL";
	public static final String URI_SCHEME = "calculator_widget";
	public static final String COMMAND = "COMMAND";
	private static final String LOG_TAG = "ImagesWidgetProvider";
	private static final int[] CONTROLS = { R.id.Button00, R.id.Button01,
			R.id.Button02, R.id.Button03, R.id.Button04, R.id.Button05,
			R.id.Button06, R.id.Button07, R.id.Button08, R.id.Button09,
			R.id.ButtonAC, R.id.ButtonDecimal, R.id.ButtonDivide,
			R.id.ButtonEqual, R.id.ButtonMemoryAdd, R.id.ButtonMemoryClear,
			R.id.ButtonMemoryRecord, R.id.ButtonMemoryRemove, R.id.ButtonMinus,
			R.id.ButtonMutiple, R.id.ButtonNegative, R.id.ButtonPlus,
			R.id.TextHistory, R.id.TextValue };
	private static final String[] CONTROLSNAME = { "Button00", "Button01",
			"Button02", "Button03", "Button04", "Button05", "Button06",
			"Button07", "Button08", "Button09", "ButtonAC", "ButtonDecimal",
			"ButtonDivide", "ButtonEqual", "ButtonMemoryAdd",
			"ButtonMemoryClear", "ButtonMemoryRecord", "ButtonMemoryRemove",
			"ButtonMinus", "ButtonMutiple", "ButtonNegative", "ButtonPlus",
			"TextHistory", "TextValue" };
	
	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();
		Log.i(LOG_TAG, "onReceive(): " + action);
		if (ACTION_WIDGET_CONTROL.equals(action)) {
			final int appWidgetId = intent.getIntExtra(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
				fnHandleCommand(context, intent,appWidgetId);
			}
		} else {
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
			// PendingIntent pendingIntent = PendingIntent.getActivity(context,
			// 0, intent, 0);

			RemoteViews remoteView = new RemoteViews(context.getPackageName(),
					R.layout.simple_calculator);

			addOnClickListerners(remoteView, context, appWidgetId);
			appWidgetManager.updateAppWidget(appWidgetId, remoteView);

		}
	}

	// Add every buttons with listeners
	private void addOnClickListerners(RemoteViews remoteView, Context context,
			int appWidgetId) {
		for (int i = 0; i < CONTROLS.length; i++) {
			Log.i(LOG_TAG, "addOnClickListerners(): " + i + " : " + CONTROLS[i]);
			remoteView.setOnClickPendingIntent(
					CONTROLS[i],
					makeControlPendingIntent(context, CONTROLS[i], appWidgetId,
							i));
		}
	}

	private PendingIntent makeControlPendingIntent(Context context,
			int controlID, int appWidgetId, int request_code) {
		Intent action = new Intent(ACTION_WIDGET_CONTROL);
		action.setAction(ACTION_WIDGET_CONTROL);
		action.putExtra(COMMAND, controlID);
		action.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		PendingIntent p = PendingIntent.getBroadcast(context, request_code,
				action, PendingIntent.FLAG_UPDATE_CURRENT);
		return p;
	}

	// onButton Action received, handle the events.
	private void fnHandleCommand(Context context, Intent intent,int appWidgetId) {
		int control_id = intent.getIntExtra(COMMAND, -1);
		for (int i = 0; i < CONTROLS.length; i++) {
			if (control_id == CONTROLS[i]) {
				fnVibrate(context);
				fnUpdateUI(context,appWidgetId,CONTROLSNAME[i]);
				Toast.makeText(context,
						"onReceive() ACTION :" + CONTROLSNAME[i],
						Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private void fnUpdateUI(Context context,int appWidgetId,String value){
		RemoteViews remoteView = new RemoteViews(context.getPackageName(),
				R.layout.simple_calculator);
		remoteView.setTextViewText(R.id.TextValue, value);
		AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, remoteView);
	}
	
	private void fnVibrate(Context context) {
		Vibrator vibrator =(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(200);
	}
}
