import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;


/**
 * Created by Patrick on 19-4-2016.
 */
public class Phineas extends Agent {

    @Override
    protected void setup() {
        super.setup();
        getName();

        //send introductie message
        ACLMessage introduction = new ACLMessage(ACLMessage.INFORM);
        introduction.setContent("INTRODUCTION");
        introduction.addReceiver(new AID("Ferb", false));
        //send(introduction);
        System.out.println(this.getAID());

        addBehaviour(new CyclicBehaviour() {
            public void action() {

                ACLMessage msg = receive();

                if (msg != null) {
                    System.out.println(msg.getSender().getName() + " :  " + msg.getContent() + " " + msg.getPerformative());
                    if(msg.getPerformative() == ACLMessage.INFORM){
                        switch (msg.getContent()) {
                            case "":
                                ACLMessage reply = msg.createReply();
                                reply.setContent(Messages.GEFELICIFLAPSTAART);
                                reply.setPerformative(ACLMessage.INFORM);
                                send(reply);
                                break;
                            case Messages.INTRODUCTION: {
                            // do Nothing
                                break;
                            }
                            case Messages.GEFELICIFLAPSTAART: {
                                break;
                            }
                            case Messages.SIGN_OUT: {
                                break;
                            }
                        }
                    }
                    if(msg.getPerformative() == ACLMessage.PROPOSE){
                        switch (msg.getContent()) {
                            case Messages.START_GAME: {
                                ACLMessage message = msg.createReply();
                                message.setContent("");
                                message.setPerformative(ACLMessage.PROPOSE);
                                send(message);
                                break;
                            }
                        }
                    }
                    if(msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){
                        ACLMessage message = msg.createReply();
                        message.setPerformative(ACLMessage.INFORM);
                        message.setContent(Messages.TURN);
                        send(message);
                    }
                }
            }
        });
    }



}
