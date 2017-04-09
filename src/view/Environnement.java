package view;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.synth.SynthSeparatorUI;

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
import sun.print.PrinterGraphicsDevice;

public class Environnement extends Agent{
	static Sudoku sudoku;
	Boolean complete;
	
	protected void setup(){
//		System.out.println("Environnement started");
		Object[] args = getArguments();
		String f = (String) args[0];
		addBehaviour(new SimulatorReceiverBehaviour((String) args[0]));
		addBehaviour(new SendDataBehaviour());
		addBehaviour(new AnalysisReceiverBehaviour());
		sudoku = new Sudoku();
		sudoku.parse(f);
		try {
			System.out.println("ORIGINAL GRID");
			sudoku.printSudoku();
		} catch(Error e) {
			e.printStackTrace();
		}
	}
	
	class SendDataBehaviour extends Behaviour {
		public void action(){
//			System.out.println("running action");
				int i,j, k=1;
				Group g;
				String messageContent;
				ACLMessage message;
				//send lines
				for(i=0; i<9; i++){
//					System.out.println("creating group " + i);
					g = new Group(k);
					for(j=0; j<9; j++){
						g.addElement(sudoku.getGrid()[i][j]);
					}
					messageContent = g.serialize();
//					System.out.println(messageContent);
					message = new ACLMessage(ACLMessage.REQUEST);
					message.setContent(messageContent);
					message.addReceiver(getAID("Analysis"+k));
			    	send(message);
			    	k++;
				}
				//send columns
				for(i=0; i<9; i++){
					g = new Group(k);
					for(j=0; j<9; j++){
						g.addElement(sudoku.getGrid()[j][i]);
					}
					messageContent = g.serialize();
//					System.out.println(messageContent);
					message = new ACLMessage(ACLMessage.REQUEST);
					message.setContent(messageContent);
					message.addReceiver(getAID("Analysis"+k));
			    	send(message);
			    	k++;
				}
				//send blocks
				Group g1,g2,g3;
				int idebut=0, ifin=3;
				boolean notfinish= true;
				do{
					g1 = new Group(k);
					g2 = new Group(k+1);
					g3 = new Group(k+2);
					
					for(i=idebut; i<ifin; i++){
						for(j=0; j<3; j++){
							g1.addElement(sudoku.getGrid()[i][j]);
						}
						for(j=3; j<6; j++){
							g2.addElement(sudoku.getGrid()[i][j]);
						}	
						for(j=6; j<9; j++){
							g3.addElement(sudoku.getGrid()[i][j]);
						}
					}
					messageContent = g1.serialize();
//					System.out.println(messageContent);
					message = new ACLMessage(ACLMessage.REQUEST);
					message.setContent(messageContent);
					message.addReceiver(getAID("Analysis"+k));
			    	send(message);
					messageContent = g2.serialize();
//					System.out.println(messageContent);
					message = new ACLMessage(ACLMessage.REQUEST);
					message.setContent(messageContent);
					message.addReceiver(getAID("Analysis"+(k+1)));
			    	send(message);
					messageContent = g3.serialize();
//					System.out.println(messageContent);
					message = new ACLMessage(ACLMessage.REQUEST);
					message.setContent(messageContent);
					message.addReceiver(getAID("Analysis"+(k+2)));
			    	send(message);
					if(ifin == 9){
						notfinish = false;  
					} else {
						idebut +=3;
						ifin +=3;
						k+=3;
					}
				}while(notfinish);
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
						System.out.println("receive message from analysis");
						String content = String.valueOf(msg.getContent());
						Gson gson = new Gson();
						Group g = gson.fromJson(content, Group.class);
						
						//merging group with current grid
						Cell newCell, oldCell;
						for(int i=0; i<9; i++){
							newCell = g.getCells().get(i);
							oldCell = sudoku.getCell(newCell.getL(), newCell.getC());
							if(oldCell.getValue() == 0){
								if(newCell.getValue() != 0){//if newCell has a value it means that Analysis succeed so newCell must replace oldCell in sudoku
									sudoku.setCell(newCell);
								} else { //merging the 2 lists of possibleValues
									ArrayList<Integer> tmp = new ArrayList<Integer>();
									for(int j=0; j<newCell.getPossibleValues().size(); j++){
										int localValue = newCell.getPossibleValues().get(j);
										if(oldCell.getPossibleValues().indexOf(localValue) != -1){
											tmp.add(localValue);
										}
									}
									newCell.setPossibleValues(tmp);
									sudoku.setCell(newCell);
								}
							}
						}
						System.out.println("NEW GRID");
						sudoku.printSudoku();
						} else {
							System.out.println("receive message FROM : " + senderName);
						}
					}

				} else {
					block();
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
	
	public void gridIsComplete(){
		//send notification to Simulator to stop simulation
	}
	
	public void launchAnalysis(){
		this.addBehaviour(new SendDataBehaviour());
	}

}
