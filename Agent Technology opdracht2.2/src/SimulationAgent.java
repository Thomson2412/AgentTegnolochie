import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;

public class SimulationAgent extends Agent {

    //the list with candidates for the contest
    private ArrayList<Participant> agentCandidatateList = new ArrayList<>();
    //integer with current round number;
    private int round = 0;

    /**
     * add a single agent to the candidateList
     * @param name the name of the agent
     */
    public void addCandidateAgent(String name){
        agentCandidatateList.add(new Participant(name));
        if(agentCandidatateList.size() == 2) {
            startRound();
        }
    }

    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    if (msg.getPerformative() == ACLMessage.REQUEST) {
                        if (msg.getContent().contains(Messages.ADD_ME)) {   // An agent wants to be added to the game
                            ACLMessage reply = msg.createReply();
                            reply.setPerformative(ACLMessage.INFORM);
                            addCandidateAgent(msg.getSender().getName());
                            reply.setContent(Messages.ADDED);
                            send(reply);                                    //send a reply: you have been added
                        }
                    }
                    if(msg.getPerformative() == ACLMessage.INFORM){
                        boolean bothSendInteger = true;
                        for(Participant p : agentCandidatateList){
                            if(p.name.equals(msg.getSender().getName())){
                                p.setChoice(Integer.parseInt(msg.getContent()));                    //save choice in participantObject
                            }
                            else {
                                ACLMessage opponentChoice = new ACLMessage(ACLMessage.INFORM);      //notify opponent about participant choice
                                opponentChoice.setContent(Messages.OPPONENT +  msg.getContent());
                                opponentChoice.addReceiver(new AID(p.name.split("@")[0], false));
                                send(opponentChoice);
                                if(p.getChoice() == 0) {                                            //one of the participants hasn't made a choice yet
                                    bothSendInteger = false;
                                }
                            }
                        }
                        if(bothSendInteger){
                            finishRound();
                        }
                    }
                }
            }
        });
    }

    /**
     * start a new round by sending all the agents it's their turn
     */
    private void startRound(){
        for(Participant p : agentCandidatateList){
            ACLMessage yourTurn = new ACLMessage(ACLMessage.INFORM);
            yourTurn.setContent(Messages.YOUR_TURN);
            yourTurn.addReceiver(new AID(p.name.split("@")[0], false));
            send(yourTurn);
        }
    }

    /**
     * finish a round by setting the utility for the agent that won this round
     * start a new round
     */
    private void finishRound(){
        Participant a = agentCandidatateList.get(0);
        Participant b = agentCandidatateList.get(1);
        if(a.getChoice() < b.getChoice()){
            a.addUtility(a.getChoice());
        }
        else if (a.getChoice() > b.getChoice()){
            b.addUtility(b.getChoice());
        }
        else{
            java.util.Random random = new java.util.Random();
            if(random.nextInt(2) == 1) {
                b.addUtility(b.getChoice());
            }
            else{
                a.addUtility(a.getChoice());
            }
        }
        if(round >= 9){
            System.out.println("Agent "+  a.name.split("@")[0] + " heeft " + a.getUtility() + " punten gehaald");
            System.out.println("Agent "+  b.name.split("@")[0] + " heeft " + b.getUtility() + " punten gehaald");
        }
        else {
            round++;
            startRound();
        }
    }
}