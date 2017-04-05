package view;

import java.util.List;

import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import model.Cell;

public class Environnement extends Agent{
	Cell[][] grid;
	Boolean complete;
	
	protected void setup(){
		System.out.println("Environnement started");
	}

	public Environnement(){
		grid = new Cell[9][9];
		complete = false;
		printGrid();
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
	
	public Cell getCell(int x, int y){
		return grid[x][y];
	}
	public void setCell(int x, int y, int value, List<Integer> possibleValues){
		grid[x][y].setValue(value);
		grid[x][y].setPossibleValues(possibleValues);
	}
	
	public void printGrid(){
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				System.out.print(grid[i][j].getValue() +" ");
			}
			System.out.println("\n");
		}
	}

}
