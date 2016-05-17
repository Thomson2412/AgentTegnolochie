import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.io.ObjectInputStream;


/**
 * Created by Patrick on 19-4-2016.
 */
public class Phineas extends Agent {
    private boolean inGame = false;
    private String intoducedAgent;


    @Override
    protected void setup() {
        super.setup();
        ACLMessage introduction = new ACLMessage(ACLMessage.INFORM);
        introduction.setContent(Messages.INTRODUCTION);
        if(getName().contains("Ferb")) {
            introduction.addReceiver(new AID("Phineas", false));
            send(introduction);
        }
        System.out.println(this.getAID());
        addBehaviour(new TickerBehaviour(this, 2 * 1000) {
            @Override
            protected void onTick() {
                if(intoducedAgent != null) {
                    if (!inGame) {
                        ACLMessage start_game_message = new ACLMessage(ACLMessage.PROPOSE);
                        start_game_message.setContent(Messages.START_GAME);
                        start_game_message.addReceiver(new AID(intoducedAgent.split("@")[0], false));
                        send(start_game_message);
                    }
                }
            }
        });

        addBehaviour(new CyclicBehaviour() {
            public void action() {

                ACLMessage msg = receive();

                if (msg != null) {
                    System.out.println(msg.getSender().getName() + " :  " + msg.getContent() + " " + msg.getPerformative());
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
                                reply.setContent(Messages.GEFELICIFLAPSTAART);
                                reply.setPerformative(ACLMessage.INFORM);
                                send(reply);
                                break;
                            case Messages.INTRODUCTION: {
                                intoducedAgent = msg.getSender().getName();
                                break;
                            }
                            case Messages.GEFELICIFLAPSTAART: {
                                ACLMessage reply21 = msg.createReply();
                                reply21.setContent(Messages.SIGN_OUT);
                                reply21.setPerformative(ACLMessage.INFORM);
                                send(reply21);
                                inGame = false;
                                break;

                            }
                            case Messages.SIGN_OUT: {
                                inGame = false;
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
                        //het spel gaat beginnen, dit is de eerste beurt;
                        inGame = true;
                        FifteenStack game = FifteenStack.fromString("");
                        System.out.println(game.toString());
                        game.takeRandom();
                        ACLMessage message = msg.createReply();
                        message.setPerformative(ACLMessage.INFORM);
                        message.setContent(Messages.TURN);
                        try {
                            message.setContentObject(game);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        send(message);
                    }
                }
            }
        });
    }



}
