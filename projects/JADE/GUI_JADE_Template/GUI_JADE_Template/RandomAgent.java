import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.Random;

public class RandomAgent extends Agent {

    private State state;
    private AID mainAgent;
    private int myId, opponentId;
    private int N, R;
    private double F;
    private ACLMessage msg;
    private String[] moves_list = {"C","D"};

    protected void setup() {
        state = State.s0NoConfig;

        //Register in the yellow pages as a player
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
        System.out.println("RandomAgent " + getAID().getName() + " is ready.");

    }

    protected void takeDown() {
        //Deregister from the yellow pages
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        System.out.println("RandomPlayer " + getAID().getName() + " terminating.");
    }

    private enum State {
        s0NoConfig, s1AwaitingGame, s2Round, s3AwaitingResult
    }

    private class Play extends CyclicBehaviour {
        Random random = new Random();


        @Override
        public void action() {
            System.out.println(getAID().getName() + ":" + state.name());
            msg = blockingReceive();

            if (msg != null) {
                // Handle agent logic based on the current state
                switch (state) {
                    case s0NoConfig:
                        // If INFORM Id#_#_,_,_,_ PROCESS SETUP --> go to state 1
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
                        // Handle game start and setup updates
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
                        } else if (msg.getPerformative() == ACLMessage.REQUEST) {
                            roundOverSharesOperations(msg);  // Handle stock operation requests
                        } else {
                            System.out.println(getAID().getName() + ":" + state.name() + " - Unexpected message");
                        }
                        break;

                    case s2Round:
                        // Handle round actions and game results
                        if (msg.getPerformative() == ACLMessage.REQUEST) {
                            ACLMessage response = new ACLMessage(ACLMessage.INFORM);
                            response.addReceiver(mainAgent);
                            String randomMove = moves_list[random.nextInt(moves_list.length)];
                            response.setContent("Action#" + randomMove);
                            System.out.println(getAID().getName() + " sent " + response.getContent());
                            send(response);
                            state = State.s3AwaitingResult;
                        } else if (msg.getPerformative() == ACLMessage.INFORM && msg.getContent().startsWith("EndGame")) {
                            state = State.s1AwaitingGame;  // End of game, return to waiting for game
                        } else {
                            System.out.println(getAID().getName() + ":" + state.name() + " - Unexpected message:" + msg.getContent());
                        }
                        break;

                    case s3AwaitingResult:
                        // If INFORM RESULTS --> go to state 2
                        if (msg.getPerformative() == ACLMessage.INFORM && msg.getContent().startsWith("Results#")) {
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


        private void roundOverSharesOperations(ACLMessage msgReceived) {
            // Check if the message content starts with "RoundOver"
            if (msgReceived.getContent().startsWith("RoundOver")) {

                // Split the message content once to avoid calling split() multiple times
                String[] contentParts = msgReceived.getContent().split("#");

                // Randomly decide between "Buy" or "Sell"
                String decision = Math.random() > 0.5 ? "Buy" : "Sell";

                // Determine the max amount to operate based on the decision
                double maxToOperate = 0.0;
                if (decision.equals("Buy") && contentParts.length > 3) {
                    maxToOperate = Double.parseDouble(contentParts[3]);
                } else if (decision.equals("Sell") && contentParts.length > 5) {
                    maxToOperate = Double.parseDouble(contentParts[5]);
                }

                // If maxToOperate is non-zero, generate a random value between 0 and maxToOperate
                if (maxToOperate > 0.0) {
                    maxToOperate = random.nextDouble();
                }

                // Create the response message and send it to the main agent
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(mainAgent);
                msg.setContent(decision + "#" + maxToOperate);

                // Log the decision
                System.out.println(getAID().getName() + " decided to " + decision + " " + maxToOperate);

                // Send the message
                send(msg);
            }
        }


        /**
         * Validates and extracts the parameters from the setup message
         *
         * @param msg ACLMessage to process
         * @return true on success, false on failure
         */
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


            //At this point everything should be fine, updating class variables
            mainAgent = msg.getSender();
            N = tN;
            R = tR;
            F = tF;
            myId = tMyId;
            return true;
        }

        /**
         * Processes the contents of the New Game message
         * @param msgContent Content of the message
         * @return true if the message is valid
         */
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
}
