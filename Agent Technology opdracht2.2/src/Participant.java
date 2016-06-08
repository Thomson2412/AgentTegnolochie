/**
 * Created by Tijmen on 3-6-2016.
 */
public class Participant {
    private int utility = 0;
    public String name;
    private int choice;
    public Participant(String name){
        this.name = name;
    }
    public void addUtility(int utility){
        this.utility += utility;
        choice = 0;
    }
    public void setChoice(int choice){
        this.choice = choice;
    }
    public int getChoice(){
        return choice;
    }
    public int getUtility(){
        return utility;
    }
}
