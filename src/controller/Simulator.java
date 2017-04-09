package controller;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class Simulator extends Agent{
	Boolean isOver;

	protected void setup(){
		isOver = false;
		
//		System.out.println("Simulator started");
		
		addBehaviour(new TickerBehaviour(this, 4000) {
		      protected void onTick() {
//		    	  System.out.println("Sending INFORM message to Environnement");
		    	  ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		    	  msg.addReceiver(getAID("Environnement"));
		    	  send(msg);
		      } 
		    });
		addBehaviour(new FailureHandlerBehaviour());
	}
	
	class FailureHandlerBehaviour extends Behaviour {
		public FailureHandlerBehaviour(){}
		
		public void action(){
//			System.out.println("Simulator : Receiving message ...");
			ACLMessage msg = receive();
			if(msg != null){
				String content = String.valueOf(msg.getContent());
//				System.out.println(content);
			} else {
				block();
			}
		}
		public boolean done(){
			return false;
		}
	}	
	//wen receive Environnement notification that grid is complete, enf the program and display the final grid
}
