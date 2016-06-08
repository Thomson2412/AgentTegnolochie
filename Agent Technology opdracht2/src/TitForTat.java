import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

//confesstival

/**
 * Created by Patrick on 19-4-2016.
 */
public class TitForTat extends Agent {
    private String lastChoice = null;

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
                            if(lastChoice == null){                                 //the first time is a random choice
                                java.util.Random random = new java.util.Random();
                                if(random.nextInt(2) == 1) {
                                    reply.setContent(Messages.CONFESS);
                                }
                                else{
                                    reply.setContent(Messages.DENY);
                                }
                            }
                            else{                                                   //after the first round, the choice will be of the agent played against last round
                                reply.setContent(lastChoice);
                            }
                            reply.setContent(Messages.DENY);
                            send(reply);
                        }
                        if(msg.getContent().contains(Messages.OPPONENT_CONFESS)){
                                lastChoice = Messages.CONFESS;                      //my opponent did confess, save his choice
                        }
                        if(msg.getContent().contains(Messages.OPPONENT_DENY)){
                            lastChoice = Messages.DENY;                             //my opponent did deny, save his choice
                        }
                    }
                }
            }
        });
    }



}
