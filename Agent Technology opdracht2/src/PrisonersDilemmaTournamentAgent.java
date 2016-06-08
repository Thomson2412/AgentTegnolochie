import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;

public class PrisonersDilemmaTournamentAgent extends Agent {

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
        if(agentCandidatateList.size() == 4) {
            setOpponents();
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
                        if (msg.getContent().contains(Messages.ADD_ME)) {   // An agent wants to be added
                            ACLMessage reply = msg.createReply();
                            reply.setPerformative(ACLMessage.INFORM);
                            addCandidateAgent(msg.getSender().getName());
                            reply.setContent(Messages.ADDED);
                            send(reply);                                    //send a reply: you have been added
                        }
                    }
                    if(msg.getPerformative() == ACLMessage.INFORM){
                        if(msg.getContent().contains(Messages.CONFESS)){    //an agent wants to confess
                            confess(msg.getSender().getName());
                        }
                        else if(msg.getContent().contains(Messages.DENY)){  //an agent wants to deny
                            deny(msg.getSender().getName());
                        }
                    }
                }
            }
        });
    }

    /**
     * Change the opponents at round 0 3 and 6. Every agent plays 3 games against every other agent. A total of 9 games
     */
    public void setOpponents(){
        if(round == 0) {
            agentCandidatateList.get(0).setOpponent(agentCandidatateList.get(1));
            agentCandidatateList.get(1).setOpponent(agentCandidatateList.get(0));
            agentCandidatateList.get(2).setOpponent(agentCandidatateList.get(3));
            agentCandidatateList.get(3).setOpponent(agentCandidatateList.get(2));
        }
        if(round == 3){
            agentCandidatateList.get(0).setOpponent(agentCandidatateList.get(2));
            agentCandidatateList.get(2).setOpponent(agentCandidatateList.get(0));
            agentCandidatateList.get(1).setOpponent(agentCandidatateList.get(3));
            agentCandidatateList.get(3).setOpponent(agentCandidatateList.get(1));
        }
        if(round == 6){
            agentCandidatateList.get(0).setOpponent(agentCandidatateList.get(3));
            agentCandidatateList.get(3).setOpponent(agentCandidatateList.get(0));
            agentCandidatateList.get(1).setOpponent(agentCandidatateList.get(2));
            agentCandidatateList.get(2).setOpponent(agentCandidatateList.get(1));
        }
    }

    /**
     * start a new round by sending all the agents it's their turn
     */
    public void startRound(){
        for(Participant p : agentCandidatateList){
            ACLMessage yourTurn = new ACLMessage(ACLMessage.INFORM);
            yourTurn.setContent(Messages.YOUR_TURN);
            yourTurn.addReceiver(new AID(p.getOpponent().name.split("@")[0], false));
            send(yourTurn);
        }
        //send message to all agents.

    }
    /**
     * set the choice of an agent to deny
     * @param agentName the name of the agent
     */
    public void confess(String agentName){
        for(Participant p : agentCandidatateList ){
            if(p.name.equals(agentName)){
                p.setChoice(Messages.CONFESS);
            }
        }
        if(allMadeChoice()){
            finishRound();
        };
    }

    /**
     * set the choice of an agent to deny
     * @param agentName the name of the agent
     */
    public void deny(String agentName){
        for(Participant p : agentCandidatateList ){
            if(p.name.equals(agentName)){
                p.setChoice(Messages.DENY);
            }
        }
        if(allMadeChoice()){
            finishRound();
        };
    }

    /**
     * check if all agents made a choice yet
     * @return boolean: true if all agents made a choice
     */
    public boolean allMadeChoice(){
        for(Participant p : agentCandidatateList){
            if(p.getChoice() == null){
                return false;
            }
        }
        return true;
    }

    /**
     * finish the current round by calculating the utility of every agent and let the agents know what utility they got this round
     * at the end the next round will be started
     */
    public void finishRound(){
        for(Participant p: agentCandidatateList){
            System.out.println(p.name.split("@")[0] + " heeft " + getUtility(p.getChoice(), p.getOpponent().getChoice()) + " punten erbij met " + p.getChoice() + " tegenstander: " + p.getOpponent().getChoice());
            p.addUtility( getUtility(p.getChoice(), p.getOpponent().getChoice()));
            ACLMessage choiceOfOpponent = new ACLMessage(ACLMessage.INFORM);
            if(p.getOpponent().getChoice() == Messages.CONFESS){
                choiceOfOpponent.setContent(Messages.OPPONENT_CONFESS);
            }
            else{
                choiceOfOpponent.setContent(Messages.OPPONENT_DENY);
            }
            choiceOfOpponent.addReceiver(new AID(p.getOpponent().name.split("@")[0], false));
            send(choiceOfOpponent);
        }
        for(Participant p: agentCandidatateList){
            p.setChoice(null);                  //reset all the choices of the agents.
        }
        if(round % 3 == 0){                     // if round = 3, 6 or 9, new opponents will be chosen.
            setOpponents();
            startRound();
        }
        else if(round >= 9){                    //if round = 9, finish the tournament
            System.out.print("\n\n\n");
            for(Participant p : agentCandidatateList){
                System.out.println(p.name.split("@")[0] + " heeft " + p.getUtility() + " punten gehaald");
            }
        }
        else{
            startRound();
        }
        round++;
    }

    /**
     *
     * @param choiceA the choice of agent A
     * @param choiceB the choice of agent B
     * @return the utility for agentA
     */
    public int getUtility(String choiceA, String choiceB){
        if(choiceA == Messages.CONFESS && choiceB == Messages.CONFESS){
            return 3;
        }
        if(choiceA == Messages.DENY && choiceB == Messages.DENY){
            return 1;
        }
        if(choiceA == Messages.DENY && choiceB == Messages.CONFESS){
            return 5;
        }
        if(choiceA == Messages.CONFESS && choiceB == Messages.DENY){
            return 0;
        }
        return 0;
    }
}