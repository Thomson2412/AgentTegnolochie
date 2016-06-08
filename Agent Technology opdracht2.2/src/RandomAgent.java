import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

//confesstival
/**
 * Created by Patrick on 19-4-2016.
 */
public class RandomAgent extends Agent {

    @Override
    protected void setup() {
        super.setup();
        ACLMessage introduction = new ACLMessage(ACLMessage.REQUEST);
        introduction.setContent(Messages.ADD_ME);
        introduction.addReceiver(new AID("SimulationAgent", false));// add myself to the contest
        send(introduction);

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();

                if (msg != null) {
                    if(msg.getPerformative() == ACLMessage.INFORM) {
                        if(msg.getContent().contains(Messages.ADDED)) { //this agent has been added to the contest

                            //yes ik ben toegevoegd
                        }
                        else if(msg.getContent().contains(Messages.YOUR_TURN)){  //it's my turn to play

                            ACLMessage reply = msg.createReply();
                            reply.setPerformative(ACLMessage.INFORM);
                            java.util.Random random = new java.util.Random();
                            reply.setContent(Integer.toString(random.nextInt(10)+1));                 //always confess
                            send(reply);
                        }
                        else if(msg.getContent().contains(Messages.OPPONENT)){
                            int choiceOfOpponent = Integer.parseInt(msg.getContent().split(" ")[1]);
                        }
                    }
                }
            }
        });
    }



}
