package model;
import java.util.Arrays;
import java.util.List;

public class Cell {
	int value = 0;
	List<Integer> possibleValues;
	
	public Cell(){
		value = 0;
		possibleValues.addAll(Arrays.asList(1,2,3,4,5,6,7,8,9));
	}
	public int getValue(){return value;}
	public void setValue(int newVal){value = newVal;}
	
	public List<Integer> getPossibleValues(){return possibleValues;}
	public void setPossibleValues(List<Integer> newVal){possibleValues = newVal;}
}
