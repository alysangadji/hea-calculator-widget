package com.appspot.thinkhea.model;
import java.util.LinkedList;
import java.util.Queue;

import android.util.Log;


public class Calculator {
	private Queue<String> input_history = new LinkedList<String>();
	
	private double currentTotal =0d;
	private StringBuffer currentDisplay =new StringBuffer();

	
	
	/* Structural Functions
	 * */
	public void clearAll() {
		//input_history.clear();
	}
	
	public String getHistory(){
		StringBuffer history = new StringBuffer();
		for(String element : input_history) {
			history.append(element);
	    }
		return history.toString();
	}
	
	public boolean isPreviousKeyNum() {
		String a = input_history.peek();
		if(a.matches("[0-9.]")){
			return false;			
		}else{
			return true;
		}
	}
	
	public boolean isPreviousKeyFunc() {
		String a = input_history.peek();
		if(a.matches("[+-*/]")){
			return false;			
		}else{
			return true;
		}
	}
	
	/* Calculation Functions
	 * */
	public double getResult(){
		return 0d;
	}
	
	public void press(int i) {
		currentDisplay.append(i);
		input_history.add(i+"");
	}
	public void plus() {
		currentDisplay.append("+");
	}
	public void minus() {
		input_history.add("-");
	}
	public void multiple() {
		input_history.add("*");
	}
	public void divide() {
		input_history.add("/");
	}
	public void equals() {
		input_history.add("=");
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
