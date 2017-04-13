package model;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class Cell {
	private int value;
	private ArrayList<Integer> possibleValues;
	private int l;
	private int c;
	
	public Cell(){
		value = 0;
		possibleValues = new ArrayList<Integer>();
	}
	public Cell(int val){
		value = val;
		possibleValues = new ArrayList<Integer>();
//		for(int i=1; i<=9; i++){
//			possibleValues.add(i);
//		}
	}
	public Cell(int val, int line, int column){
		value = val;
		l = line;
		c = column;
		possibleValues = new ArrayList<Integer>();
		for(int i=1; i<=9; i++){
			possibleValues.add(i);
		}
	}
	public int getValue(){return value;}
	public void setValue(int newVal){
		this.value = newVal;
		this.possibleValues = new ArrayList<Integer>();
	}
	
	public ArrayList<Integer> getPossibleValues(){return possibleValues;}
	public void setPossibleValues(ArrayList<Integer> newVal){possibleValues = newVal;}
	
	public String serialize(){
		return new Gson().toJson(this);
	}
	
	public String toString(){
		return value + " " + possibleValues.toString();
	}
	
	public int getL() {
		return l;
	}
	public void setL(int l) {
		this.l = l;
	}
	public int getC() {
		return c;
	}
	public void setC(int c) {
		this.c = c;
	}
}
