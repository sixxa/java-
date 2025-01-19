package src.agents;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class psi_21 extends Agent {

    private State state;
    private AID mainAgent;
    private int myId, opponentId;
    private int N, R;
    private float S;
    private ACLMessage msg;
    private Map<Integer, Float> opponentActions; // Tracks opponent's actions
    private float cooperationProbability; // Probability of cooperating

    protected void setup() {
        state = State.waitConfig;
        cooperationProbability = 0.5f; // Start with a neutral strategy
        opponentActions = new HashMap<>();

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
        System.out.println("SmartAgent " + getAID().getName() + " is ready.");
    }

    protected void takeDown() {
        // Deregister from the yellow pages
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        System.out.println("SmartAgent " + getAID().getName() + " terminating.");
        System.exit(0);
    }

    private enum State {
        waitConfig, waitGame, waitAction, waitResults, waitAccounting
    }

    private String decideAction() {
        // Decide based on cooperation probability
        return new Random().nextFloat() < cooperationProbability ? "C" : "D";
    }

    private void updateStrategy(String opponentAction) {
        if (opponentAction.equals("C")) {
            cooperationProbability = Math.min(1.0f, cooperationProbability + 0.1f); // Reward cooperation
        } else {
            cooperationProbability = Math.max(0.0f, cooperationProbability - 0.1f); // Punish defection
        }
    }

    private void printColored(String text) {
        if (myId % 2 == 0) {
            System.out.println("\u001B[31m" + text + "\u001B[0m"); // Red color for even IDs
        } else {
            System.out.println("\u001B[32m" + text + "\u001B[0m"); // Green color for odd IDs
        }
    }

    private class Play extends CyclicBehaviour {
        @Override
        public void action() {
            msg = blockingReceive();
            if (msg != null) {
                printColored(
                        getAID().getName() + " received " + msg.getContent() + " from " + msg.getSender().getName()
                                + "\n\t State: " + state); // DELETEME

                switch (state) {
                    case waitConfig:
                        if (msg.getContent().startsWith("Id#")) {
                            try {
                                if (validateAndSetParameters(msg)) {
                                    state = State.waitGame;
                                }
                            } catch (NumberFormatException e) {
                                printColored(getAID().getName() + ":" + state.name() + " - Bad message");
                            }
                        } else {
                            printColored(getAID().getName() + ":" + state.name() + " - Unexpected message");
                        }
                        break;
                    case waitGame:
                        if (msg.getPerformative() == ACLMessage.INFORM && msg.getContent().startsWith("NewGame#")) {
                            if (processNewGame(msg.getContent())) {
                                state = State.waitAction;
                            } else {
                                printColored(getAID().getName() + ":" + state.name() + " - Bad message");
                            }
                        } else if (msg.getPerformative() == ACLMessage.INFORM
                                && msg.getContent().startsWith("GameOver")) {
                            System.out.println("Game Over " + getAID().getName());
                            takeDown();
                        } else {
                            printColored(getAID().getName() + ":" + state.name() + " - Unexpected message");
                        }
                        break;

                    case waitAction:
                        if (msg.getPerformative() == ACLMessage.REQUEST && msg.getContent().startsWith("Action")) {
                            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                            msg.addReceiver(mainAgent);
                            String action = decideAction();
                            msg.setContent("Action#" + action);
                            printColored(getAID().getName() + " sent " + msg.getContent());
                            send(msg);
                            state = State.waitResults;
                        } else {
                            printColored(getAID().getName() + ":" + state.name() + " - Unexpected message");
                        }

                        break;
                    case waitResults:
                        if (msg.getPerformative() == ACLMessage.INFORM && msg.getContent().startsWith("Results#")) {
                            String[] parts = msg.getContent().split("#");
                            if (parts.length >= 2) {
                                String opponentAction = parts[1];
                                updateStrategy(opponentAction);
                            }
                            state = State.waitGame;
                        } else {
                            printColored(getAID().getName() + ":" + state.name() + " - Unexpected message");
                        }
                        break;
                }
                printColored(getAID().getName() + " is now in state " + state);
            }
        }

        private boolean validateAndSetParameters(ACLMessage msg) throws NumberFormatException {
            String[] parts = msg.getContent().split("#");
            if (parts.length != 3) {
                return false;
            }
            String[] params = parts[2].split(",");
            if (params.length != 3) {
                return false;
            }
            int tMyId = Integer.parseInt(parts[1]);
            int tN = Integer.parseInt(params[0]);
            int tR = Integer.parseInt(params[1]);
            float tS = Float.parseFloat(params[2]);

            mainAgent = msg.getSender();
            N = tN;
            S = tS;
            R = tR;
            myId = tMyId;
            return true;
        }

        public boolean processNewGame(String msgContent) {
            String[] parts = msgContent.split("#");
            if (parts.length != 3) {
                return false;
            }
            int id1 = Integer.parseInt(parts[1]);
            int id2 = Integer.parseInt(parts[2]);
            if (id1 == myId) {
                opponentId = id2;
                return true;
            } else if (id2 == myId) {
                opponentId = id1;
                return true;
            }
            return false;
        }
    }
}
