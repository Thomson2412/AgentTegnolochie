import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.leap.HashMap;
import jade.util.leap.Serializable;
import java.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Enumeration;
import java.util.List;
public class PrisonersDilemmaTournamentAgent extends Agent {

    private ArrayList<Participant> agentCandidatateList = new ArrayList<>();
    private int round = 0;
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
        System.out.println(this.getAID());

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                  //  System.out.println(msg.getSender().getName() + " :  " + msg.getContent() + " " + msg.getPerformative());
                    if (msg.getPerformative() == ACLMessage.REQUEST) {
                        if (msg.getContent().contains(Messages.ADD_ME)) {
                            ACLMessage reply = msg.createReply();
                            reply.setPerformative(ACLMessage.INFORM);
                            addCandidateAgent(msg.getSender().getName());
                            reply.setContent(Messages.ADDED);
                            send(reply);
                        }
                    }
                    if(msg.getPerformative() == ACLMessage.INFORM){
                        if(msg.getContent().contains(Messages.CONFESS)){
                            confess(msg.getSender().getName());
                        }
                        else if(msg.getContent().contains(Messages.DENY)){
                            deny(msg.getSender().getName());
                        }
                    }
                }
            }
        });
    }
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
    public void startRound(){
        System.out.println("start ronde " + round);
        for(Participant p : agentCandidatateList){
            ACLMessage yourTurn = new ACLMessage(ACLMessage.INFORM);
            yourTurn.setContent(Messages.YOUR_TURN);
            yourTurn.addReceiver(new AID(p.getOpponent().name.split("@")[0], false));
            send(yourTurn);
        }
        //send message to all agents.

    }
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
    public boolean allMadeChoice(){
        for(Participant p : agentCandidatateList){
            if(p.getChoice() == null){
                return false;
            }
        }
        return true;
    }
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
    public void finishRound(){
        System.out.println("finish ronde");
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
            p.setChoice(null);
        }
        if(round % 3 == 0){
            setOpponents();
            startRound();
        }
        else if(round >= 9){
            System.out.println("FINISHED");
            for(Participant p : agentCandidatateList){
                System.out.println(p.name.split("@")[0] + " heeft " + p.getUtility() + " punten gehaald");
            }
        }
        else{
            startRound();
        }
        System.out.println("round++");
        round++;
    }

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