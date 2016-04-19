import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;


/**
 * Created by Patrick on 19-4-2016.
 */
public class MyAgent extends Agent {

    @Override
    protected void setup() {
        super.setup();

        System.out.println(this.getAID());

        addBehaviour(new CyclicBehaviour() {
            public void action() {

                ACLMessage msg = receive();
                if (msg != null) {

                        System.out.println("yoe:  " + msg.getContent());

                    ACLMessage reply = msg.createReply();
                    reply.setPerformative( ACLMessage.INFORM );
                    reply.setContent("Hello" );
                    send(reply);

                }
            }
        });
    }



}
