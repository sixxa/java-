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

public class RL_Agent extends Agent {

    private State state;
    private AID mainAgent;
    private int myId, opponentId;
    private int N, R;
    private double F;
    private ACLMessage msg;
    private String[] moves_list = {"C", "D"};
    private QLearning qLearning;

    protected void setup() {
        state = State.s0NoConfig;
        qLearning = new QLearning(moves_list);

        // Register in the yellow pages as a player
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
        addBehaviour(new Play());
        System.out.println("RL_Agent " + getAID().getName() + " is ready.");

    }

    protected void takeDown() {
        // Deregister from the yellow pages
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        System.out.println("RL_Agent " + getAID().getName() + " terminating.");
    }

    private enum State {
        s0NoConfig, s1AwaitingGame, s2Round, s3AwaitingResult
    }

    private class Play extends CyclicBehaviour {
        @Override
        public void action() {
            System.out.println(getAID().getName() + " Current state: " + state.name());

            // Use blockingReceive with a timeout to avoid indefinite blocking
            msg = blockingReceive(5000); // Wait up to 5000ms for a message
            if (msg == null) {
                System.out.println(getAID().getName() + " No message received, retrying...");
                return;
            }

            System.out.println(getAID().getName() + " Received message: " + msg.getContent() + " (Performative: " + ACLMessage.getPerformative(msg.getPerformative()) + ")");

            synchronized (this) {
                try {
                    switch (state) {
                        case s0NoConfig:
                            handleNoConfigState(msg);
                            break;

                        case s1AwaitingGame:
                            handleAwaitingGameState(msg);
                            break;

                        case s2Round:
                            handleRoundState(msg);
                            break;

                        case s3AwaitingResult:
                            handleAwaitingResultState(msg);
                            break;

                        default:
                            System.out.println(getAID().getName() + " Unknown state: " + state.name());
                            break;
                    }
                } catch (Exception e) {
                    System.err.println(getAID().getName() + " Error occurred: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        private void handleNoConfigState(ACLMessage msg) {
            if (msg.getContent().startsWith("Id#") && msg.getPerformative() == ACLMessage.INFORM) {
                try {
                    if (validateSetupMessage(msg)) {
                        state = State.s1AwaitingGame;
                    }
                } catch (NumberFormatException e) {
                    System.out.println(getAID().getName() + " " + state.name() + " - Invalid setup message format.");
                }
            } else {
                System.out.println(getAID().getName() + " " + state.name() + " - Unexpected message: " + msg.getContent());
            }
        }

        private void handleAwaitingGameState(ACLMessage msg) {
            if (msg.getPerformative() == ACLMessage.INFORM) {
                String content = msg.getContent();
                if (content.startsWith("Id#")) {
                    try {
                        validateSetupMessage(msg);
                    } catch (NumberFormatException e) {
                        System.out.println(getAID().getName() + " " + state.name() + " - Invalid setup message format.");
                    }
                } else if (content.startsWith("NewGame#")) {
                    try {
                        if (validateNewGame(content)) {
                            state = State.s2Round;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println(getAID().getName() + " " + state.name() + " - Invalid new game message format.");
                    }
                } else if (content.startsWith("Accounting#")) {
                    System.out.println(getAID().getName() + " Accounting information received: " + content);
                } else {
                    System.out.println(getAID().getName() + " Unexpected message in AwaitingGame state: " + content);
                }
            }
        }

        private void handleRoundState(ACLMessage msg) {
            if (msg.getPerformative() == ACLMessage.REQUEST) {
                ACLMessage response = new ACLMessage(ACLMessage.INFORM);
                response.addReceiver(mainAgent);

                // Generate action using Q-Learning
                String currentState = myId + "," + opponentId;
                String action = qLearning.chooseAction(currentState);
                response.setContent("Action#" + action);

                System.out.println(getAID().getName() + " Sending action: " + response.getContent());
                send(response);

                state = State.s3AwaitingResult;
            } else if (msg.getPerformative() == ACLMessage.INFORM && msg.getContent().startsWith("EndGame")) {
                state = State.s1AwaitingGame;
            } else {
                System.out.println(getAID().getName() + " Unexpected message in Round state: " + msg.getContent());
            }
        }

        private void handleAwaitingResultState(ACLMessage msg) {
            if (msg.getPerformative() == ACLMessage.INFORM && msg.getContent().startsWith("Results#")) {
                String[] parts = msg.getContent().split("#");
                String currentState = myId + "," + opponentId;
                String opponentAction = parts[2].split(",")[1];
                int reward = Integer.parseInt(parts[3].split(",")[0]);

                // Update Q-Learning table
                qLearning.updateQTable(currentState, opponentAction, reward);
                System.out.println(getAID().getName() + " Q-Table updated for state: " + currentState + " with reward: " + reward);

                state = State.s2Round;
            } else {
                System.out.println(getAID().getName() + " Unexpected message in AwaitingResult state: " + msg.getContent());
            }
        }

        private boolean validateSetupMessage(ACLMessage msg) {
            System.out.println(getAID().getName() + " Validating setup message: " + msg.getContent());

            String[] contentSplit = msg.getContent().split("#");
            if (contentSplit.length != 3 || !contentSplit[0].equals("Id")) {
                System.out.println(getAID().getName() + " Setup message validation failed.");
                return false;
            }

            myId = Integer.parseInt(contentSplit[1]);
            String[] parametersSplit = contentSplit[2].split(",");
            if (parametersSplit.length != 3) {
                System.out.println(getAID().getName() + " Setup message parameter parsing failed.");
                return false;
            }

            N = Integer.parseInt(parametersSplit[0]);
            R = Integer.parseInt(parametersSplit[1]);
            F = Double.parseDouble(parametersSplit[2]);

            mainAgent = msg.getSender();
            System.out.println(getAID().getName() + " Setup message validation succeeded.");
            return true;
        }

        public boolean validateNewGame(String msgContent) {
            System.out.println(getAID().getName() + " Validating new game message: " + msgContent);

            String[] contentSplit = msgContent.split("#");
            if (contentSplit.length != 2 || !contentSplit[0].equals("NewGame")) {
                System.out.println(getAID().getName() + " New game message validation failed.");
                return false;
            }

            String[] idSplit = contentSplit[1].split(",");
            if (idSplit.length != 2) {
                System.out.println(getAID().getName() + " New game ID parsing failed.");
                return false;
            }

            int msgId0 = Integer.parseInt(idSplit[0]);
            int msgId1 = Integer.parseInt(idSplit[1]);

            if (myId == msgId0) {
                opponentId = msgId1;
                System.out.println(getAID().getName() + " New game validated with opponent ID: " + opponentId);
                return true;
            } else if (myId == msgId1) {
                opponentId = msgId0;
                System.out.println(getAID().getName() + " New game validated with opponent ID: " + opponentId);
                return true;
            }

            System.out.println(getAID().getName() + " New game validation failed.");
            return false;
        }

    }

    private class QLearning {

        private Map<String, Map<String, Double>> qTable;
        private String[] actions;
        private double learningRate = 0.1;
        private double discountFactor = 0.9;
        private double explorationRate = 0.2;

        public QLearning(String[] actions) {
            this.actions = actions;
            qTable = new HashMap<>();
        }

        public String chooseAction(String state) {
            if (!qTable.containsKey(state)) {
                qTable.put(state, initializeActionValues());
            }

            if (Math.random() < explorationRate) {
                return actions[new Random().nextInt(actions.length)];
            } else {
                return qTable.get(state).entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .get()
                        .getKey();
            }
        }

        public void updateQTable(String state, String action, int reward) {
            if (!qTable.containsKey(state)) {
                qTable.put(state, initializeActionValues());
            }
            double oldQValue = qTable.get(state).get(action);
            double maxFutureReward = qTable.get(state).values().stream().max(Double::compare).orElse(0.0);

            double newQValue = oldQValue + learningRate * (reward + discountFactor * maxFutureReward - oldQValue);
            qTable.get(state).put(action, newQValue);
        }

        private Map<String, Double> initializeActionValues() {
            Map<String, Double> actionValues = new HashMap<>();
            for (String action : actions) {
                actionValues.put(action, 0.0);
            }
            return actionValues;
        }
    }
}
