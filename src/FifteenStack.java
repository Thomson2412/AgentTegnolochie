import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;

/**
 * Created by Thomas on 19-4-2016.
 */
public class FifteenStack extends Agent{

    @Override
    protected void setup() {
        super.setup();

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                System.out.println("I'm still alive!");
            }
        });
    }

    @Override
    protected void takeDown() {

    }

    @Override
    public void addBehaviour(Behaviour b) {
        super.addBehaviour(b);
    }

    @Override
    public void removeBehaviour(Behaviour b) {
        super.removeBehaviour(b);
    }

    private static FifteenStack instance = null;

    private static int firstStack = 3;
    private static int secondStack = 5;
    private static int thirdStack = 7;

    public static FifteenStack fromString(String s) {
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
}
