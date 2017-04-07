package view;

import java.io.FileNotFoundException;
import java.util.List;

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
import model.Sudoku;

public class Environnement extends Agent{
	Sudoku sudoku;
	Boolean complete;
	
	protected void setup(){
		System.out.println("Environnement started");
		Object[] args = getArguments();
		String f = (String) args[0];
		addBehaviour(new SimulatorReceiverBehavior((String) args[0]));
		sudoku = new Sudoku();
		sudoku.parse(f);
		try {
			sudoku.printSudoku();
		} catch(Error e) {
			e.printStackTrace();
		}
	}
	
	class SimulatorReceiverBehavior extends Behaviour {
		String sudokuFile; 
		
		public SimulatorReceiverBehavior(String f){
			this.sudokuFile = f;
		}
		public void action(){
			ACLMessage msg = receive();
			if(msg != null){
				String content = String.valueOf(msg.getContent());
				System.out.println(content);
				launchAnalysis();
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
			System.out.println("Main container created");
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
			System.out.println("27 Analysis agents started");
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void updateGrid(){}
	
	public void gridIsComplete(){
		//send notification to Simulator to stop simulation
	}
	
	public void launchAnalysis(){
		//
	}

}
