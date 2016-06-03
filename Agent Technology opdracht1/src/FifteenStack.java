import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.util.leap.Serializable;

/**
 * Created by Thomas on 19-4-2016.
 */
public class FifteenStack implements Serializable{


    private static FifteenStack instance = null;

    private static int firstStack;
    private static int secondStack;
    private static int thirdStack;
    private FifteenStack(){

    }
    public static FifteenStack fromString(String s) {
        firstStack = 3;
        secondStack = 5;
        thirdStack = 7;
        if(instance == null) {
            instance = new FifteenStack();
        }
        return instance;
    }


    public int look(int stack) {

        switch(stack) {
            case 1: return firstStack;
            case 2: return secondStack;
            case 3: return thirdStack;
            default: break;
        }
        return -1;
    }

    public void take(int stack, int amount) {
        switch (stack) {
            case 1: firstStack -= amount; break;
            case 2: secondStack -= amount; break;
            case 3: thirdStack -= amount; break;
        }

    }

    public boolean gameOver() {

        if(firstStack == 0 && secondStack == 0 && thirdStack == 0)  {
            return true;
        }
        return false;
    }

    public String toString() {
        return "First stack: " + Integer.toString(firstStack) +
                "\n Second stack: " + Integer.toString(secondStack) +
                "\n Third stack: " + Integer.toString(thirdStack);
    }
    public void takeRandom(){
        int stack = 0;
        while(look(stack) <= 0){
            stack = (int) (Math.random() * 3) + 1;
        }
        int amount =  (int)(Math.random() * look(stack)) + 1;
        take(stack, amount);
    }
}
