import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RL_Agent extends Agent{

    private State state;
    private AID mainAgent;
    private int myId, opponentId;
    private int N, R;
    private double F;
    private ACLMessage msg;
    private double learningRate = 0.1;
    private double discountFactor = 0.9;
    private double epsilon = 0.2; // Exploration rate
    private Random random = new Random();

    private Map<String, Double> qTable = new HashMap<>();
    private String lastAction = null;

    private enum State {
        s0NoConfig, s1AwaitingGame, s2Round, s3AwaitingResult
    }

    protected void setup() {
        state = State.s0NoConfig;
        registerInYellowPages();
        addBehaviour(new Play());
        System.out.println("RLAgent " + getAID().getName() + " is ready.");
    }

    protected void takeDown() {
        deregisterFromYellowPages();
        System.out.println("RLAgent " + getAID().getName() + " terminating.");
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
                            lastAction = action;
                            state = State.s3AwaitingResult;
                        }
                        break;

                    case s3AwaitingResult:
                        if (msg.getPerformative() == ACLMessage.INFORM && msg.getContent().startsWith("Results#")) {
                            updateQTable(msg.getContent());
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
            if (random.nextDouble() < epsilon) {
                return random.nextBoolean() ? "C" : "D";
            }
            return getBestAction();
        }

        private String getBestAction() {
            double cooperateValue = qTable.getOrDefault("C", 0.0);
            double defectValue = qTable.getOrDefault("D", 0.0);
            return cooperateValue >= defectValue ? "C" : "D";
        }

        private void sendAction(String action) {
            ACLMessage response = new ACLMessage(ACLMessage.INFORM);
            response.addReceiver(mainAgent);
            response.setContent("Action#" + action);
            send(response);
            System.out.println(getAID().getName() + " sent " + response.getContent());
        }

        private void updateQTable(String results) {
            String[] parts = results.split("#");
            // parts[0]: "Results"
            // parts[1]: "0,1" (player IDs)
            // parts[2]: "C,D" (actions)
            // parts[3]: "2,3" (rewards)

            // Parse rewards only
            String[] rewards = parts[3].split(",");
            int reward = Integer.parseInt(rewards[0]); // Reward for this agent

            // Update Q-table
            double oldQValue = qTable.getOrDefault(lastAction, 0.0);
            double maxNextQValue = Math.max(qTable.getOrDefault("C", 0.0), qTable.getOrDefault("D", 0.0));
            double newQValue = oldQValue + learningRate * (reward + discountFactor * maxNextQValue - oldQValue);

            qTable.put(lastAction, newQValue);
        }

    }
}
