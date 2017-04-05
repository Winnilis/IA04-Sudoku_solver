package controller;
import java.util.List;

import jade.core.Agent;
import model.Cell;

public class Analysis extends Agent{
	Boolean complete;

	
	public List<Cell> analyseCells(List<Cell> inputs){
		for(int i = 0; i<inputs.size(); i++){
			if(inputs.get(i).getPossibleValues().size() == 1){
				inputs.get(i).setValue(inputs.get(i).getPossibleValues().get(0));
			}
		}
		return inputs;
	}
}
