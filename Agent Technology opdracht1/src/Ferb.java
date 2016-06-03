import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.io.ObjectInputStream;


/**
 * Created by Patrick on 19-4-2016.
 */
public class Ferb extends Agent {
    boolean inGame;
    @Override
    protected void setup() {
        super.setup();
        ACLMessage introduction = new ACLMessage(ACLMessage.INFORM);
        introduction.setContent(Messages.INTRODUCTION);
        introduction.addReceiver(new AID("Phineas", false));
        send(introduction);

        System.out.println(this.getAID());

        addBehaviour(new TickerBehaviour(this, 1 * 1000) {
            @Override
            protected void onTick() {
                ACLMessage start_game_message = new ACLMessage(ACLMessage.PROPOSE);
                start_game_message.setContent(Messages.START_GAME);
                start_game_message.addReceiver(new AID("Phineas", false));
                send(start_game_message);
            }
        });

        addBehaviour(new CyclicBehaviour() {
            public void action() {

                ACLMessage msg = receive();
                if (msg != null) {
                    System.out.println(msg.getSender().getName() + " :  " + msg.getContent()+ " " + msg.getPerformative());
                    if(msg.getPerformative() == ACLMessage.INFORM){
                        if(msg.getContent().contains("FifteenStack")){
                            ACLMessage reply = msg.createReply();
                            reply.setPerformative(ACLMessage.INFORM);
                            try {

                                FifteenStack game = (FifteenStack) msg.getContentObject();
                                System.out.println(game.toString());
                                if(game == null) {
                                    return;
                                }
                                game.takeRandom();
                                if (game.gameOver()) {
                                    reply.setContent(Messages.GEFELICIFLAPSTAART);
                                } else {
                                    reply.setContent(Messages.TURN);
                                    reply.setContentObject(game);
                                }
                                send(reply);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        switch (msg.getContent()) {
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
                                inGame = true; 
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
