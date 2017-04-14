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
		return "Group " + index + " : " + cells.toString();
	}
	
	public ArrayList<Integer> mapValues(){
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(int i=0; i<9; i++){
			if(cells.get(i).getValue() != 0){
				result.add(cells.get(i).getValue());
			}
		}
		return result;
	}
	
	public ArrayList<Integer> mapPossibleValues(){
		ArrayList<Integer> result = new ArrayList<Integer>();
		return result;
	}

	public ArrayList<Integer> occurrencesIndexes(int i){
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for(int j=0; j<9; j++){
			if(cells.get(j).getPossibleValues().contains(i)){
				indexes.add(j);
			}
		}
		return indexes;
	}
}