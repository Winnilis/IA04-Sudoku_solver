package model;

import java.util.ArrayList;

import com.google.gson.Gson;

public class Group {
	int index;
	ArrayList<Cell> cells;
	
	public Group(){
		index = 0;
		cells = new ArrayList<Cell>();
	}
	public Group(int i){
		index = i;
		cells = new ArrayList<Cell>();
	}
	
	public void addElement(Cell c){
		this.cells.add(c);
	}

	public String serialize() {
		Gson gson = new Gson();
		String result = gson.toJson(this);
		return result;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public ArrayList<Cell> getCells() {
		return cells;
	}
	public void setCells(ArrayList<Cell> cells) {
		this.cells = cells;
	}
	
	public String toString(){
		return cells.toString();
	}
}
