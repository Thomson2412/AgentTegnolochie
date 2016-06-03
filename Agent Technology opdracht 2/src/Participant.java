/**
 * Created by Tijmen on 3-6-2016.
 */
public class Participant {
    private int utility = 0;
    private Participant opponent;
    public String name;
    private String choice = null;
    public Participant(String name){
        this.name = name;
    }
    public void addUtility(int utility){
        this.utility += utility;
    }
    public void setChoice(String choice){
        this.choice = choice;
    }
    public String getChoice(){
        return choice;
    }
    public void setOpponent(Participant p){
        this.opponent = p;
    }
    public Participant getOpponent(){
        return this.opponent;
    }
    public int getUtility(){
        return utility;
    }
}
