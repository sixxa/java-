import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.util.Random;

public class NN_Agent extends Agent {

    private State state;
    private AID mainAgent;
    private int myId, opponentId;
    private int N, R;
    private double F;
    private ACLMessage msg;
    private NeuralNetwork neuralNetwork;

    private String[] moves_list = {"C", "D"};

    protected void setup() {
        state = State.s0NoConfig;
        neuralNetwork = new NeuralNetwork(moves_list.length); // Initialize NN with two outputs for "C" and "D"

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
        System.out.println("NN_Agent " + getAID().getName() + " is ready.");
    }

    protected void takeDown() {
        // Deregister from the yellow pages
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        System.out.println("NN_Agent " + getAID().getName() + " terminating.");
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
                return; // Exit if no message is received
            }

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
                            System.out.println(getAID().getName() + ": Unknown state encountered: " + state.name());
                            break;
                    }
                } catch (Exception e) {
                    System.err.println(getAID().getName() + " encountered an error: " + e.getMessage());
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
                    System.out.println(getAID().getName() + ": Invalid setup message format.");
                }
            } else {
                logUnexpectedMessage(msg);
            }
        }

        private void handleAwaitingGameState(ACLMessage msg) {
            if (msg.getPerformative() == ACLMessage.INFORM) {
                String content = msg.getContent();
                try {
                    if (content.startsWith("Id#")) {
                        validateSetupMessage(msg);
                    } else if (content.startsWith("NewGame#") && validateNewGame(content)) {
                        state = State.s2Round;
                    } else if (content.startsWith("Accounting#")) {
                        System.out.println(getAID().getName() + " Accounting message received: " + content);
                    } else {
                        logUnexpectedMessage(msg);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(getAID().getName() + ": Invalid message format in AwaitingGame state.");
                }
            } else {
                logUnexpectedMessage(msg);
            }
        }

        private void handleRoundState(ACLMessage msg) {
            if (msg.getPerformative() == ACLMessage.REQUEST && msg.getContent().equals("Action")) {
                ACLMessage response = new ACLMessage(ACLMessage.INFORM);
                response.addReceiver(mainAgent);

                // Use Neural Network to decide the action
                double[] inputs = {myId, opponentId}; // Adjust based on the game state if needed
                String action = moves_list[neuralNetwork.predict(inputs)];
                response.setContent("Action#" + action);

                System.out.println(getAID().getName() + " sent action: " + action);
                send(response);
                state = State.s3AwaitingResult;
            } else if (msg.getPerformative() == ACLMessage.INFORM && msg.getContent().startsWith("EndGame")) {
                state = State.s1AwaitingGame;
            } else {
                logUnexpectedMessage(msg);
            }
        }

        private void handleAwaitingResultState(ACLMessage msg) {
            if (msg.getPerformative() == ACLMessage.INFORM && msg.getContent().startsWith("Results#")) {
                try {
                    String[] parts = msg.getContent().split("#");
                    String opponentAction = parts[2].split(",")[1];
                    int reward = Integer.parseInt(parts[3].split(",")[0]);

                    // Train the Neural Network based on the result
                    double[] inputs = {myId, opponentId};
                    neuralNetwork.train(inputs, moves_list, reward);

                    System.out.println(getAID().getName() + " trained NN with reward: " + reward);
                    state = State.s2Round;
                } catch (NumberFormatException e) {
                    System.out.println(getAID().getName() + ": Invalid result message format.");
                }
            } else {
                logUnexpectedMessage(msg);
            }
        }

        private void logUnexpectedMessage(ACLMessage msg) {
            System.out.println(getAID().getName() + ": Unexpected message received in state " + state.name() + ": " + msg.getContent());
        }

        private boolean validateSetupMessage(ACLMessage msg) throws NumberFormatException {
            String msgContent = msg.getContent();
            String[] contentSplit = msgContent.split("#");

            if (contentSplit.length != 3 || !contentSplit[0].equals("Id")) {
                return false;
            }

            myId = Integer.parseInt(contentSplit[1]);
            String[] parametersSplit = contentSplit[2].split(",");

            if (parametersSplit.length != 3) {
                return false;
            }

            N = Integer.parseInt(parametersSplit[0]);
            R = Integer.parseInt(parametersSplit[1]);
            F = Double.parseDouble(parametersSplit[2]);

            mainAgent = msg.getSender();
            return true;
        }

        public boolean validateNewGame(String msgContent) {
            String[] contentSplit = msgContent.split("#");

            if (contentSplit.length != 2 || !contentSplit[0].equals("NewGame")) {
                return false;
            }

            String[] idSplit = contentSplit[1].split(",");
            if (idSplit.length != 2) {
                return false;
            }

            int msgId0 = Integer.parseInt(idSplit[0]);
            int msgId1 = Integer.parseInt(idSplit[1]);

            if (myId == msgId0) {
                opponentId = msgId1;
                return true;
            } else if (myId == msgId1) {
                opponentId = msgId0;
                return true;
            }

            return false;
        }
    }


    private class NeuralNetwork {

        private int numOutputs;
        private Random random;

        public NeuralNetwork(int numOutputs) {
            this.numOutputs = numOutputs;
            this.random = new Random();
        }

        public int predict(double[] inputs) {
            // Simulate prediction by returning a random action for now
            return random.nextInt(numOutputs);
        }

        public void train(double[] inputs, String[] actions, int reward) {
            // Simulate training (implement proper backpropagation logic here)
            System.out.println("Training NN with reward: " + reward);
        }
    }
}
