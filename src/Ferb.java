import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;


/**
 * Created by Patrick on 19-4-2016.
 */
public class Ferb extends Agent {

    @Override
    protected void setup() {
        super.setup();
        ACLMessage introduction = new ACLMessage(ACLMessage.INFORM);
        introduction.setContent(Messages.INTRODUCTION);
        introduction.addReceiver(new AID("Phineas", false));
        send(introduction);
        ACLMessage start_game_message = new ACLMessage(ACLMessage.PROPOSE);
        start_game_message.setContent(Messages.START_GAME);
        start_game_message.addReceiver(new AID("Phineas", false));
        send(start_game_message);
        System.out.println(this.getAID());
        addBehaviour(new SimpleBehaviour() {
            public void action(){

            }
            public boolean done(){

                return false;
            }
        });
        addBehaviour(new CyclicBehaviour() {
            public void action() {

                ACLMessage msg = receive();
                if (msg != null) {
                    System.out.println(msg.getSender().getName() + " :  " + msg.getContent()+ " " + msg.getPerformative());
                    if(msg.getPerformative() == ACLMessage.INFORM){
                        switch (msg.getContent()) {
                            case Messages.TURN:{
                                //doe tweede behavior
                                ACLMessage reply = msg.createReply();
                                reply.setContent("");
                                reply.setPerformative(ACLMessage.INFORM);
                                send(reply);
                                break;
                            }
                            case "":
                                ACLMessage reply = msg.createReply();
                                reply.setContent("");
                                reply.setPerformative(ACLMessage.INFORM);
                                send(reply);
                                break;
                            case Messages.INTRODUCTION: {

                                break;
                            }
                            case Messages.GEFELICIFLAPSTAART: {
                                ACLMessage reply21 = msg.createReply();
                                reply21.setContent(Messages.SIGN_OUT);
                                reply21.setPerformative(ACLMessage.INFORM);
                                send(reply21);
                                break;
                            }
                            case Messages.SIGN_OUT: {
                                break;
                            }
                        }
                    }
                    if(msg.getPerformative() == ACLMessage.PROPOSE){
                        switch (msg.getContent()) {
                            case "": {
                                ACLMessage message = msg.createReply();
                                message.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                                send(message);
                                break;
                            }
                        }
                    }

                    if(msg.getPerformative() == ACLMessage.INFORM) { //GG



                        /*ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setContent("Hello");
                        send(reply);*/
                    }
                }
            }
        });
    }
}
