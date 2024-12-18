package failure.success;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> globalPatents = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z");
        int numberOfAgents = 5;

        Environment env = new Environment(numberOfAgents, globalPatents);
        env.simulate();
        env.printResults();
    }
}