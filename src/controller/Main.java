package controller;

import jade.wrapper.AgentContainer;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class Main {

	public static void main(String[] args) {
		String MAIN_PROPERTIES_FILE = "prop";
		Runtime rt = Runtime.instance();
		Profile p = null;
		try{
			p = new ProfileImpl(MAIN_PROPERTIES_FILE);
			AgentContainer mc = rt.createMainContainer(p);
		} catch(Exception ex){}
		
		try {
			p = new ProfileImpl(MAIN_PROPERTIES_FILE);
			ContainerController cc = rt.createAgentContainer(p);
			AgentController as = cc.createNewAgent("Simulator", "controller.Simulator", null);
			as.start();
			AgentController ae = cc.createNewAgent("Environnement", "view.Environnement", null);
			ae.start();
			
			AgentController aa;
			String name;
			for(int i = 1; i<=27; i++){
				name = "Analysis"+i;
				aa = cc.createNewAgent(name, "controller.Analysis", null);
				aa.start();
			}
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
