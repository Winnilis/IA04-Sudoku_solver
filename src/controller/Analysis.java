package controller;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.*;

import javax.swing.plaf.synth.SynthSeparatorUI;

import com.google.gson.Gson;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Set;
import model.Cell;
import model.Group;

public class Analysis extends Agent{
	Boolean complete;

	public void setup(){
		addBehaviour(new startAnalysis());
	}
	
	public class startAnalysis extends Behaviour{
		public void action(){
			ACLMessage msg = receive();
			if(msg != null){
					String content = String.valueOf(msg.getContent());
					Gson gson = new Gson();
					Group g = gson.fromJson(content, Group.class);

					//update all PossibleValues lists
					ArrayList<Integer> usedValues = g.mapValues();
					ArrayList<Integer> newPossibleValues;
					for(int i =0; i<9; i++){ //for each cell in group
						newPossibleValues = new ArrayList<Integer>();
						for(int j=0; j<g.getCells().get(i).getPossibleValues().size(); j++){ //parcourt de la list PossibleValues
							if(!usedValues.contains(g.getCells().get(i).getPossibleValues().get(j))){
								newPossibleValues.add(g.getCells().get(i).getPossibleValues().get(j));
							}
						}
						g.getCells().get(i).setPossibleValues(newPossibleValues);
					}
					
					//algo1 Lorsqu'une cellule n'a plus qu'une valeur possible, celle-ci en devient son contenu et la liste des possibles est vidée.
					for(int i=0; i<9; i++){
						if(g.getCells().get(i).getValue() == 0){
							if(g.getCells().get(i).getPossibleValues().size() == 1){
								g.getCells().get(i).setValue(g.getCells().get(i).getPossibleValues().get(0)); //set value empty the PossibleValues list
							}
						}
					}
					
//					//algo 3 : Une valeur ne se trouvant que dans une seule liste de possibles est la valeur de cette cellule
//					int testedValue;
//					boolean flag;
//					ArrayList<Integer> alreadyTestedValues = new ArrayList<Integer>();
//					for(int i=0; i<9; i++){
//						currentCell = g.getCells().get(i);
//						if(currentCell.getValue() == 0){
//							for(int j = 0; j<currentCell.getPossibleValues().size(); j++){
//								testedValue = currentCell.getPossibleValues().get(j);
//								
//								if(alreadyTestedValues.indexOf(testedValue) != -1){
//									alreadyTestedValues.add(testedValue);
//									flag = false;
//									for(int k=i; k<9; k++){
//										if(g.getCells().get(k).getPossibleValues().indexOf(testedValue) != -1){
//											flag = true;
//										}
//									}
//									if(!flag){//if tested value occurs only once in all possibleValues lists
//										currentCell.setValue(testedValue);
//										currentCell.setPossibleValues(null);
//									}
//								}
//							}
//						}
//					}
////		System.out.println(g);
//					
//					//algo4 : Si seulement deux cellules contiennent les deux mêmes valeurs possibles alors les possibles des autres cellules ne peuvent contenir ces valeurs
//					ArrayList<Integer> tmp =  new ArrayList<Integer>();
//					for(int i=0; i<9; i++){
//						if(g.getCells().get(i).getValue()== 0 && g.getCells().get(i).getPossibleValues().size() == 2){
//							tmp.addAll(g.getCells().get(i).getPossibleValues());
//							for(int j=i; j<9; j++){
//								if(g.getCells().get(j).getValue()== 0 && g.getCells().get(j).getPossibleValues().size() == 2){
//									Collections.sort(g.getCells().get(j).getPossibleValues());
//									Collections.sort(tmp);
//									if(tmp.equals(g.getCells().get(j).getPossibleValues())){
//										//remove in all other PossibleValues the values contained in tmp
//										for(int k=0; k<9; k++){
//											if(k!=i && k!=j && g.getCells().get(k).getValue()== 0){
//												g.getCells().get(k).getPossibleValues().remove(tmp.get(0));
//												g.getCells().get(k).getPossibleValues().remove(tmp.get(1));
//											}
//										}
//									}
//								}
//							}
//						}
//					}
					
					//check group consistency before sending the cells back
//					int tmpVal;
//					for(int i=0 ; i<9; i++){
//						if(g.getCells().get(i).getValue()!=0){
//							tmpVal = g.getCells().get(i).getValue();
//							for(int j=0; j<9; j++){
//								if(g.getCells().get(j).getValue() == 0){
//									if(g.getCells().get(j).getPossibleValues().indexOf(tmpVal) != -1){
//										g.getCells().get(j).getPossibleValues().remove(g.getCells().get(j).getPossibleValues().indexOf(tmpVal));
//									}
//								}
//							}
//						}
//					}
					
					//send modified cells
					ACLMessage answer = new ACLMessage(ACLMessage.INFORM);
					String answerContent = g.serialize();
					answer.setContent(answerContent);
					answer.addReceiver(getAID("Environnement"));
			    	send(answer);
				} else {
					block();
				}
			}
		
		@Override
		public boolean done() {
			return false;
		}

	}
}
