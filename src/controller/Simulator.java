package controller;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class Simulator extends Agent{
	Boolean isOver;

	protected void setup(){
		isOver = false;
		
		addBehaviour(new TickerBehaviour(this, 4000) {
		      protected void onTick() {
		    	  ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		    	  msg.addReceiver(getAID("Environnement"));
		    	  send(msg);
		      }
		    });
		addBehaviour(new FailureHandlerBehaviour());
		addBehaviour(new DoneBehaviour());
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
	
	class DoneBehaviour extends Behaviour {
		public void action(){
			ACLMessage msg = receive();
			if(msg != null){
				String senderName = msg.getSender().getLocalName();
				if(senderName.equals("Environnement") && msg.getContent().equals("over")){
					System.out.println("PROGRAM IS OVER -----------------------------------------------------------------");
					myAgent.doDelete(); //end of program
				}
			} else {
				block();
			}
		}
		public boolean done(){
			return false;
		}
	}
}
