package com.appspot.thinkhea;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class CalculatorActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_calculator);
        Log.i("CalculatorActivity", "onCreate()");
        
        addListenerToButton();
    }

	private void addListenerToButton() {
		Button button00 = (Button)this.findViewById(R.id.Button00);
		button00.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Log.i("Button00", "setOnClickListener()");
            }
        });
		
	}
}