package view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.CellEditorListener;

import com.google.gson.Gson;

import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import model.Cell;
import model.Group;
import model.Sudoku;

public class Environnement extends Agent{
	static Sudoku sudoku;
	Boolean complete;
	
	protected void setup(){
		Object[] args = getArguments();
		String f = (String) args[0];
		addBehaviour(new SimulatorReceiverBehaviour((String) args[0]));
		addBehaviour(new SendDataBehaviour());
		addBehaviour(new AnalysisReceiverBehaviour());
		sudoku = new Sudoku(f);
//		addBehaviour(new test());
		try {
			System.out.println("ORIGINAL GRID");
//			sudoku.printSudoku();
//			sudoku.printAll();
		} catch(Error e) {
			e.printStackTrace();
		}
	}
	
	class test extends Behaviour {
		public void action(){
			System.out.println(sudoku.getLineGroup(0).occurs(5));
			System.out.println(sudoku.getLineGroup(0).occurs(1));
		}
		@Override
		public boolean done() {
			return true;
		}
	}
	
	class SendDataBehaviour extends Behaviour {
		public void action(){
			int i,j, k=1;
			Group g;
			//send lines
			for(i=0; i<9; i++){
				g = sudoku.getLineGroup(i);
				g.setIndex(k);
				sendMessage(g.serialize(), "Analysis"+k);
				k++;
			}
			//send columns
			for(i=0; i<9; i++){
				g = sudoku.getColumnGroup(i);
				g.setIndex(k);
				sendMessage(g.serialize(), "Analysis"+k);
				k++;
			}
			//send blocks
			for(i=0; i<9; i+=3){
				for(j=0; j<9; j+=3){
					g = sudoku.getBlockGroup(i, (i+2), j, j+2);
					g.setIndex(k);
					sendMessage(g.serialize(), "Analysis"+k);
					k++;
				}
			}
		}
		
		public void sendMessage(String content, String receiver){
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.setContent(content);
			msg.addReceiver(getAID(receiver));
			send(msg);
		}
		@Override
		public boolean done() {
			return false;
		}
	}
	
	class SimulatorReceiverBehaviour extends Behaviour {
		String sudokuFile; 
		
		public SimulatorReceiverBehaviour(String f){
			this.sudokuFile = f;
		}
		public void action(){
			ACLMessage msg = receive();
			if(msg != null){
				if(msg.getSender().getLocalName() == "Simulator"){
					System.out.println("receive message from simulator");
					String content = String.valueOf(msg.getContent());
					System.out.println(content);
					launchAnalysis();
				}
			} else {
				block();
			}
		}
		public boolean done(){
			return false;
		}
}
	
	class AnalysisReceiverBehaviour extends Behaviour {
		public void action(){
			ACLMessage msg = receive();
			if(msg != null){
				String senderName = msg.getSender().getLocalName();
				System.out.println(senderName);

				if(senderName.length() >= 8){
					senderName = senderName.substring(0, 8);
					if(senderName.equals("Analysis")){
//						System.out.println("receive message from analysis");
						String content = String.valueOf(msg.getContent());
						Gson gson = new Gson();
						Group g = gson.fromJson(content, Group.class);
						
						Cell oldCell, newCell;
						ArrayList<Integer> tmp;
						//merge old and new cells
						for(int i=0; i<9; i++){
							tmp = new ArrayList<Integer>();
							oldCell = sudoku.getCell(g.getCells().get(i).getL(), g.getCells().get(i).getC());
							newCell = g.getCells().get(i);
							if(oldCell.getValue() == 0){ //modify only if old Cell has no value to avoir modification over correct cell
								if(newCell.getValue() != 0){
									newCell.setPossibleValues(new ArrayList<Integer>());
									sudoku.setCell(newCell);
								} else {
									//merge possibleValues list
									for(int k=0; k<oldCell.getPossibleValues().size(); k++){
										if(newCell.getPossibleValues().contains(oldCell.getPossibleValues().get(k))){
											tmp.add(oldCell.getPossibleValues().get(k));
										}
									}
									sudoku.getCell(g.getCells().get(i).getL(), g.getCells().get(i).getC()).setPossibleValues(tmp);
								}//end-else
							} else {
								oldCell.setPossibleValues(new ArrayList<Integer>());
							}
						}
						System.out.println("NEW GRID");
						sudoku.printSudoku();
						sudoku.printAll();
						isGridComplete();
						}
					}
//						for(int i=0; i<9; i++){
//							Cell newCell = g.getCells().get(i);
//							Cell oldCell = sudoku.getCell(newCell.getL(), newCell.getC());
//							Cell mergedCell = merge(oldCell, newCell);
//						}
						
						//merging new group with current grid
//						for(int i=0; i<9; i++){ //forEach cell in g
//							Cell newCell = g.getCells().get(i);
//							int l = newCell.getL();
//							int c = newCell.getC();
//							Cell oldCell = sudoku.getCell(c, l); //get the corresponding oldCell in current grid
//							
//							
//							if(oldCell.getValue() == 0){
//								if(newCell.getValue() != 0){//if newCell has a value it means that Analysis succeed so newCell must replace oldCell in sudoku
//									ArrayList<Integer> lineValuesMap = sudoku.getLineGroup(l).mapValues();
//									ArrayList<Integer> columnValuesMap = sudoku.getLineGroup(c).mapValues();
//									int lmin = (l/3)*3;
//									int cmin = (c/3)*3;
//									ArrayList<Integer> blockValuesMap = sudoku.getBlockGroup(lmin, (lmin+2), cmin, (cmin+2)).mapValues();
//									boolean newValueIsNotPossible = lineValuesMap.contains(newCell.getValue())
//											|| columnValuesMap.contains(newCell.getValue())
//											|| blockValuesMap.contains(newCell.getValue());
//									if(!newValueIsNotPossible){
//										sudoku.setCell(newCell);										
//									}
//								} else {
//									//update possibleValues list with only common elements
//									ArrayList<Integer> tmp = new ArrayList<Integer>();
//									for(int j=0; j<newCell.getPossibleValues().size(); j++){
//										int localValue = newCell.getPossibleValues().get(j);
//										if(oldCell.getPossibleValues().indexOf(localValue) != -1){
//											tmp.add(localValue);
//										}
//									}
//									newCell.setPossibleValues(tmp);
//									sudoku.setCell(newCell);
//								}
//							}
//						}
//						System.out.println("NEW GRID");
//						sudoku.printSudoku();
//						sudoku.printAll(); 
//						} else {
//							System.out.println("receive message FROM : " + senderName);
//						}
//					}
//					isGridComplete();
				} else {
					block();
				}
		}
		
		public void isGridComplete() {
			boolean isComplete = true;
			for(int i=0; i<9; i++){
				for (int j = 0; j < 9; j++) {
					if(sudoku.getCell(i, j).getValue() == 0){
						isComplete = false;
					}
				}
			}
			if(isComplete){
				ACLMessage stopSimulator = new ACLMessage(ACLMessage.REQUEST);
				stopSimulator.setContent("over");
				stopSimulator.addReceiver(getAID("Simulator"));
		    	send(stopSimulator);
		    	System.out.println("SUDOKU IS COMPLETE -----------------------------------------------------------------");
		    	myAgent.doDelete();
			}
		}
		
		public boolean done(){
			return false;
		}
		
	}
	
	public static void launchAgents(){
		String MAIN_PROPERTIES_FILE = "prop";
		Runtime rt = Runtime.instance();
		Profile p = null;
		try{
			p = new ProfileImpl(MAIN_PROPERTIES_FILE);
			AgentContainer mc = rt.createMainContainer(p);
//			System.out.println("Main container created");
		} catch(Exception ex){}
		
		try {
			p = new ProfileImpl(MAIN_PROPERTIES_FILE);
			ContainerController cc = rt.createAgentContainer(p);
			AgentController as = cc.createNewAgent("AgentSimulation", "AgentSimulation", null);
			as.start();
			AgentController ae = cc.createNewAgent("AgentEnvironnement", "AgentEnvironnement", null);
			ae.start();
			
			AgentController aa;
			String name;
			for(int i = 0; i<27; i++){
				name = "AgentAnalyse"+i;
				aa = cc.createNewAgent(name, "AgentAnalyse", null);
				aa.start();
			}
//			System.out.println("27 Analysis agents started");
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void updateGrid(){}
	
	public void launchAnalysis(){
		this.addBehaviour(new SendDataBehaviour());
	}

}
