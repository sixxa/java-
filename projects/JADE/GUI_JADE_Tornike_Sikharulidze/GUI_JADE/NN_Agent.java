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
            System.out.println(getAID().getName() + ":" + state.name());
            msg = blockingReceive();

            if (msg != null) {
                switch (state) {
                    case s0NoConfig:
                        if (msg.getContent().startsWith("Id#") && msg.getPerformative() == ACLMessage.INFORM) {
                            try {
                                if (validateSetupMessage(msg)) {
                                    state = State.s1AwaitingGame;
                                }
                            } catch (NumberFormatException e) {
                                System.out.println(getAID().getName() + ":" + state.name() + " - Bad message");
                            }
                        } else {
                            System.out.println(getAID().getName() + ":" + state.name() + " - Unexpected message");
                        }
                        break;

                    case s1AwaitingGame:
                        if (msg.getPerformative() == ACLMessage.INFORM) {
                            String content = msg.getContent();
                            if (content.startsWith("Id#")) {
                                try {
                                    validateSetupMessage(msg);
                                } catch (NumberFormatException e) {
                                    System.out.println(getAID().getName() + ":" + state.name() + " - Bad message");
                                }
                            } else if (content.startsWith("NewGame#")) {
                                try {
                                    if (validateNewGame(content)) {
                                        state = State.s2Round;
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println(getAID().getName() + ":" + state.name() + " - Bad message");
                                }
                            } else if (content.startsWith("Accounting#")) {
                                System.out.println(content);  // Handle accounting message
                            }
                        }
                        break;

                    case s2Round:
                        if (msg.getPerformative() == ACLMessage.REQUEST) {
                            ACLMessage response = new ACLMessage(ACLMessage.INFORM);
                            response.addReceiver(mainAgent);

                            // Use Neural Network to decide the action
                            double[] inputs = {myId, opponentId}; // Example input (can be adjusted based on game state)
                            String action = moves_list[neuralNetwork.predict(inputs)];
                            response.setContent("Action#" + action);
                            System.out.println(getAID().getName() + " sent " + response.getContent());
                            send(response);
                            state = State.s3AwaitingResult;
                        } else if (msg.getPerformative() == ACLMessage.INFORM && msg.getContent().startsWith("EndGame")) {
                            state = State.s1AwaitingGame;
                        } else {
                            System.out.println(getAID().getName() + ":" + state.name() + " - Unexpected message:" + msg.getContent());
                        }
                        break;

                    case s3AwaitingResult:
                        if (msg.getPerformative() == ACLMessage.INFORM && msg.getContent().startsWith("Results#")) {
                            String[] parts = msg.getContent().split("#");
                            String opponentAction = parts[2].split(",")[1];
                            int reward = Integer.parseInt(parts[3].split(",")[0]);

                            // Train the Neural Network based on the result
                            double[] inputs = {myId, opponentId};
                            neuralNetwork.train(inputs, moves_list, reward);

                            state = State.s2Round;
                        } else {
                            System.out.println(getAID().getName() + ":" + state.name() + " - Unexpected message");
                        }
                        break;

                    default:
                        System.out.println(getAID().getName() + ":" + state.name() + " - Unknown state");
                        break;
                }
            }
        }

        private boolean validateSetupMessage(ACLMessage msg) throws NumberFormatException {
            int tN, tR, tMyId;
            double tF;
            String msgContent = msg.getContent();

            String[] contentSplit = msgContent.split("#");
            if (contentSplit.length != 3) return false;
            if (!contentSplit[0].equals("Id")) return false;
            tMyId = Integer.parseInt(contentSplit[1]);

            String[] parametersSplit = contentSplit[2].split(",");
            if (parametersSplit.length != 3) return false;
            tN = Integer.parseInt(parametersSplit[0]);
            tR = Integer.parseInt(parametersSplit[1]);
            tF = Double.parseDouble(parametersSplit[2]);

            mainAgent = msg.getSender();
            N = tN;
            R = tR;
            F = tF;
            myId = tMyId;
            return true;
        }

        public boolean validateNewGame(String msgContent) {
            int msgId0, msgId1;
            String[] contentSplit = msgContent.split("#");
            if (contentSplit.length != 2) return false;
            if (!contentSplit[0].equals("NewGame")) return false;
            String[] idSplit = contentSplit[1].split(",");
            if (idSplit.length != 2) return false;
            msgId0 = Integer.parseInt(idSplit[0]);
            msgId1 = Integer.parseInt(idSplit[1]);
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