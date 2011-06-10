package com.appspot.thinkhea;

import java.util.Stack;

import com.appspot.thinkhea.model.Calculator;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.PorterDuff;

public class CalculatorActivity extends Activity {

	private static final int[] NUMBERBUTTONCONTROLS = { R.id.Button00,
			R.id.Button01, R.id.Button02, R.id.Button03, R.id.Button04,
			R.id.Button05, R.id.Button06, R.id.Button07, R.id.Button08,
			R.id.Button09, R.id.ButtonDecimal };
	private static final int[] FUNCTIONBUTTONCONTROLS = { R.id.ButtonAC,
			R.id.ButtonDivide, R.id.ButtonEqual, R.id.ButtonEvalOpen,
			R.id.ButtonEvalClose, R.id.ButtonBack, R.id.ButtonPercentage,
			R.id.ButtonMinus, R.id.ButtonMutiple, R.id.ButtonNegative,
			R.id.ButtonPlus };
	private static final int[] TEXTCONTROLS = { R.id.TextHistory,
			R.id.TextValue };
	private static final String[] CONTROLSNAME = { "Button00", "Button01",
			"Button02", "Button03", "Button04", "Button05", "Button06",
			"Button07", "Button08", "Button09", "ButtonAC", "ButtonDecimal",
			"ButtonDivide", "ButtonEqual", "ButtonEvalOpen", "ButtonEvalClose",
			"ButtonBack", "ButtonPercentage", "ButtonMinus", "ButtonMutiple",
			"ButtonNegative", "ButtonPlus", "TextHistory", "TextValue" };
	private Calculator calculator = null;

	private TextView equationText = null;
	private TextView answerText = null;

	private boolean flagResetOnNextAction = false;
	private boolean flagAllowVibratation = true;
	private Stack<Integer> undoStack = new Stack<Integer>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_calculator);
		Log.i("CalculatorActivity", "onCreate()");
		addListenerToButton(NUMBERBUTTONCONTROLS);
		addListenerToButton(FUNCTIONBUTTONCONTROLS);
		addListenerToTextView();

		// 0xFF00FF00 Green // 0xFFFF0000 Red
		setButtonStyle(FUNCTIONBUTTONCONTROLS, 0xFFFF0000, 32);
		setButtonStyle(NUMBERBUTTONCONTROLS, -1, 32);
		setTextViewStyle(TEXTCONTROLS,-1,24);
		
		SharedPreferences settings = loadPreferenceFile();
		flagAllowVibratation = settings.getBoolean("Vibrate", true);
	}
	
	private SharedPreferences loadPreferenceFile(){
		return getSharedPreferences("Preferences", MODE_PRIVATE);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.preference_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.isVirbrate:
	    	flagAllowVibratation = !flagAllowVibratation;
	    	SharedPreferences settings = loadPreferenceFile();
	    	settings.edit().putBoolean("Vibrate", flagAllowVibratation);
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	private void fnUndo() {
		try {
			int len = undoStack.pop();
			CharSequence charS = getEquationText().getText();
			CharSequence undoCharS = charS.subSequence(0, charS.length() - len);
			getEquationText().setText(undoCharS);
		} catch (Exception e) {
			Toast.makeText(this, "Unable to undo.", Toast.LENGTH_SHORT);
		}
	}

	private void fnAllClear() {
		getEquationText().setText("");
		getAnserText().setText("");
		undoStack.clear();
	}

	private Calculator getCalculator() {
		if (calculator == null) {
			calculator = new Calculator();
		}
		return calculator;
	}

	private TextView getEquationText() {
		if (equationText == null) {
			equationText = (TextView) findViewById(R.id.TextHistory);
		}
		return equationText;
	}

	private TextView getAnserText() {
		if (answerText == null) {
			answerText = (TextView) findViewById(R.id.TextValue);
		}
		return answerText;
	}

	private void setEquation(String input) {
		undoStack.push(input.length());
		getEquationText().append(input);
	}

	private void fnDisplayAnswer(String s) {
		getAnserText().setText(s);
	}

	private String getAnswer() {
		TextView textEquation = (TextView) findViewById(R.id.TextHistory);
		String equation = textEquation.getText().toString();
		String res = "";
		double ans = 0;
		try {
			ans = getCalculator().getResult(equation);
			res += ans;
		} catch (Exception e) {
			res = "Cannot find the answer , please check your equation...";
		}
		return res;
	}

	private void addListenerToTextView() {
		final ClipboardManager clipBoard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		final TextView tv = (TextView) this.findViewById(TEXTCONTROLS[0]);
		tv.setOnLongClickListener(new View.OnLongClickListener() {
			public boolean onLongClick(View v) {
				clipBoard.setText(tv.getText());
				return false;
			}
		});
	}

	private void setTextViewStyle(int[] array, int color, int fontSize) {
		for (int i = 0; i < array.length; i++) {
			TextView vi = (TextView) this.findViewById(array[i]);
			vi.setTextIsSelectable(true);
			if(color != -1){
				vi.setTextColor(color);				
			}
			if(fontSize != -1){
				vi.setTextSize(fontSize);				
			}
		}
	}
	
	
	private void setButtonStyle(int[] array, int color, int fontSize) {
		for (int i = 0; i < array.length; i++) {
			Button bi = (Button) this.findViewById(array[i]);
			if(color != -1){
				bi.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);				
			}
			if(fontSize != -1){
				bi.setTextSize(fontSize);				
			}
		}
	}
	
	private void fnVibrate() {
		Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		if (vibrator.hasVibrator() == true) {
			vibrator.vibrate(50);
		}
	}
	
	private void addListenerToButton(int[] array) {
		for (int i = 0; i < array.length; i++) {
			Button bi = (Button) this.findViewById(array[i]);
			bi.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if (flagResetOnNextAction == true) {
						fnAllClear();
						flagResetOnNextAction = false;
					}
					if(flagAllowVibratation==true){
						fnVibrate();
					}

					switch (v.getId()) {
					case R.id.Button00:
						setEquation("0");
						break;
					case R.id.Button01:
						setEquation("1");
						break;
					case R.id.Button02:
						setEquation("2");
						break;
					case R.id.Button03:
						setEquation("3");
						break;
					case R.id.Button04:
						setEquation("4");
						break;
					case R.id.Button05:
						setEquation("5");
						break;
					case R.id.Button06:
						setEquation("6");
						break;
					case R.id.Button07:
						setEquation("7");
						break;
					case R.id.Button08:
						setEquation("8");
						break;
					case R.id.Button09:
						setEquation("9");
						break;
					case R.id.ButtonDecimal:
						setEquation(".");
						break;
					case R.id.ButtonPlus:
						setEquation(" + ");
						break;
					case R.id.ButtonMinus:
						setEquation(" - ");
						break;
					case R.id.ButtonMutiple:
						setEquation(" * ");
						break;
					case R.id.ButtonDivide:
						setEquation(" ÷ ");
						break;
					case R.id.ButtonNegative:
						setEquation(" -");
						break;
					case R.id.ButtonEvalOpen:
						setEquation(" ( ");
						break;
					case R.id.ButtonEvalClose:
						setEquation(" ) ");
						break;
					case R.id.ButtonPercentage:
						setEquation(" % ");
						break;
					case R.id.ButtonBack:
						fnUndo();
						break;
					case R.id.ButtonAC:
						fnAllClear();
						break;
					case R.id.ButtonEqual:
						fnDisplayAnswer(getAnswer());
						flagResetOnNextAction = false;
						break;
					default:
						break;
					}
				}
			});
		}
	}
}