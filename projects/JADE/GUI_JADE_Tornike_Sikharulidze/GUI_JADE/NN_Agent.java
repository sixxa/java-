import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.Random;

public class NN_Agent extends Agent{

    private State state;
    private AID mainAgent;
    private int myId, opponentId;
    private int N, R;
    private double F;
    private ACLMessage msg;

    private SOM som; // Self-organizing map for decision-making
    private static final int GRID_SIZE = 10; // SOM grid size
    private static final int INPUT_SIZE = 2; // Input size (e.g., payoff + opponent behavior)
    private double[] lastInputVector;

    private Random random = new Random();

    private enum State {
        s0NoConfig, s1AwaitingGame, s2Round, s3AwaitingResult
    }

    protected void setup() {
        state = State.s0NoConfig;
        som = new SOM(GRID_SIZE, INPUT_SIZE); // Initialize SOM
        registerInYellowPages();
        addBehaviour(new Play());
        System.out.println("NN_Agent " + getAID().getName() + " is ready.");
    }

    protected void takeDown() {
        deregisterFromYellowPages();
        System.out.println("NN_Agent " + getAID().getName() + " terminating.");
    }

    private void registerInYellowPages() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Player");
        sd.setName("Game");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    private void deregisterFromYellowPages() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    private class Play extends CyclicBehaviour {
        @Override
        public void action() {
            System.out.println(getAID().getName() + ":" + state.name());
            msg = blockingReceive();

            if (msg != null) {
                switch (state) {
                    case s0NoConfig:
                        if (msg.getPerformative() == ACLMessage.INFORM && msg.getContent().startsWith("Id#")) {
                            parseSetupMessage(msg);
                            state = State.s1AwaitingGame;
                        }
                        break;

                    case s1AwaitingGame:
                        if (msg.getPerformative() == ACLMessage.INFORM && msg.getContent().startsWith("NewGame#")) {
                            parseNewGameMessage(msg.getContent());
                            state = State.s2Round;
                        }
                        break;

                    case s2Round:
                        if (msg.getPerformative() == ACLMessage.REQUEST && msg.getContent().equals("Action")) {
                            String action = chooseAction();
                            sendAction(action);
                            state = State.s3AwaitingResult;
                        }
                        break;

                    case s3AwaitingResult:
                        if (msg.getPerformative() == ACLMessage.INFORM && msg.getContent().startsWith("Results#")) {
                            trainSOM(msg.getContent());
                            state = State.s2Round;
                        }
                        break;

                    default:
                        break;
                }
            }
        }

        private void parseSetupMessage(ACLMessage msg) {
            String[] parts = msg.getContent().split("#");
            myId = Integer.parseInt(parts[1]);
            String[] params = parts[2].split(",");
            N = Integer.parseInt(params[0]);
            R = Integer.parseInt(params[1]);
            F = Double.parseDouble(params[2]);
            mainAgent = msg.getSender();
        }

        private void parseNewGameMessage(String content) {
            String[] parts = content.split("#")[1].split(",");
            opponentId = myId == Integer.parseInt(parts[0]) ? Integer.parseInt(parts[1]) : Integer.parseInt(parts[0]);
        }

        private String chooseAction() {
            // Input vector for SOM (e.g., last reward, opponent behavior)
            double[] inputVector = lastInputVector != null ? lastInputVector : new double[]{0.0, 0.0};

            // Use SOM to determine action
            String bmu = som.sGetBMU(inputVector, false);
            int x = Integer.parseInt(bmu.split(",")[0]);
            int y = Integer.parseInt(bmu.split(",")[1]);

            // Decide action based on SOM output
            return (x + y) % 2 == 0 ? "C" : "D"; // Example: Map even/odd to "C"/"D"
        }

        private void sendAction(String action) {
            ACLMessage response = new ACLMessage(ACLMessage.INFORM);
            response.addReceiver(mainAgent);
            response.setContent("Action#" + action);
            send(response);
            System.out.println(getAID().getName() + " sent " + response.getContent());
        }

        private void trainSOM(String results) {
            String[] parts = results.split("#");
            // parts[0]: "Results"
            // parts[1]: "0,1" (player IDs)
            // parts[2]: "C,D" (actions)
            // parts[3]: "2,3" (rewards)

            // Parse rewards only
            String[] rewards = parts[3].split(",");
            double myReward = Double.parseDouble(rewards[0]);
            double opponentReward = Double.parseDouble(rewards[1]);

            // Input vector for training
            double[] inputVector = {myReward, opponentReward};
            som.sGetBMU(inputVector, true); // Train SOM with current results

            // Update last input for the next round
            lastInputVector = inputVector;
        }

    }
}
