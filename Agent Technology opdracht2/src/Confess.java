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
public class Confess extends Agent {
    private boolean inGame = false;
    private String intoducedAgent;


    @Override
    protected void setup() {
        super.setup();
        ACLMessage introduction = new ACLMessage(ACLMessage.REQUEST);
        introduction.setContent(Messages.ADD_ME);
        introduction.addReceiver(new AID("TournamentMaster", false));
        send(introduction);

        System.out.println(this.getAID());

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();

                if (msg != null) {
                    if(msg.getPerformative() == ACLMessage.INFORM) {
                        if(msg.getContent().contains(Messages.ADDED)) {
                            System.out.println("Yes ik ben toegevoegd");
                            //yes ik ben toegevoegd
                        }
                        else if(msg.getContent().contains(Messages.YOUR_TURN)){
                            ACLMessage reply = msg.createReply();
                            reply.setPerformative(ACLMessage.INFORM);
                            reply.setContent(Messages.CONFESS);
                            send(reply);
                        }
                        if(msg.getContent().contains(Messages.OPPONENT_CONFESS)){
                        //    System.out.println("ik ben " + getName() + " en mijn tegenstander deed confess");
                        }
                        if(msg.getContent().contains(Messages.OPPONENT_DENY)){
                       //     System.out.println("ik ben " + getName() + " en mijn tegenstander deed Deny");
                        }
                    }
                }
            }
        });
    }



}
