import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

//confesstival

/**
 * Created by Patrick on 19-4-2016.
 */
public class Random extends Agent {

    @Override
    protected void setup() {
        super.setup();
        ACLMessage introduction = new ACLMessage(ACLMessage.REQUEST);
        introduction.setContent(Messages.ADD_ME);
        introduction.addReceiver(new AID("TournamentMaster", false));// add myself to the contest
        send(introduction);

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();

                if (msg != null) {
                    if(msg.getPerformative() == ACLMessage.INFORM) {
                        if(msg.getContent().contains(Messages.ADDED)) { //this agent has been added to the contest
                            System.out.println("Yes ik ben toegevoegd");
                            //yes ik ben toegevoegd
                        }
                        else if(msg.getContent().contains(Messages.YOUR_TURN)){  //it's my turn to play
                            ACLMessage reply = msg.createReply();
                            reply.setPerformative(ACLMessage.INFORM);
                            java.util.Random random = new java.util.Random();
                            if(random.nextInt(2) == 1) {                        //do random deny or confess
                                reply.setContent(Messages.CONFESS);
                            }
                            else{
                                reply.setContent(Messages.DENY);
                            }
                            send(reply);
                        }
                    }
                }
            }
        });
    }



}
