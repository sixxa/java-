import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
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
        private Random random = new Random(); // Add this to resolve the random variable issue

        @Override
        public void action() {
            ACLMessage msg = blockingReceive(5000); // Use timeout to prevent indefinite blocking
            if (msg == null) {
                System.out.println(getAID().getName() + " No message received, retrying...");
                return;
            }

            synchronized (this) {
                try {
                    switch (state) {
                        case s0NoConfig:
                            if (msg.getContent().startsWith("Id#") && msg.getPerformative() == ACLMessage.INFORM) {
                                if (validateSetupMessage(msg)) {
                                    state = State.s1AwaitingGame;
                                }
                            } else {
                                System.out.println(getAID().getName() + " Unexpected message in NoConfig state: " + msg.getContent());
                            }
                            break;

                        case s1AwaitingGame:
                            if (msg.getContent().startsWith("NewGame#") && msg.getPerformative() == ACLMessage.INFORM) {
                                if (validateNewGame(msg.getContent())) {
                                    state = State.s2Round;
                                }
                            } else {
                                System.out.println(getAID().getName() + " Unexpected message in AwaitingGame state: " + msg.getContent());
                            }
                            break;

                        case s2Round:
                            if (msg.getPerformative() == ACLMessage.REQUEST) {
                                ACLMessage response = new ACLMessage(ACLMessage.INFORM);
                                response.addReceiver(mainAgent);

                                // Random action
                                String action = moves_list[random.nextInt(moves_list.length)];
                                response.setContent("Action#" + action);
                                System.out.println(getAID().getName() + " Sending action: " + response.getContent());
                                send(response);

                                state = State.s3AwaitingResult;
                            } else {
                                System.out.println(getAID().getName() + " Unexpected message in Round state: " + msg.getContent());
                            }
                            break;

                        case s3AwaitingResult:
                            if (msg.getContent().startsWith("Results#") && msg.getPerformative() == ACLMessage.INFORM) {
                                state = State.s2Round;
                            } else {
                                System.out.println(getAID().getName() + " Unexpected message in AwaitingResult state: " + msg.getContent());
                            }
                            break;

                        default:
                            System.out.println(getAID().getName() + " Unknown state: " + state);
                            break;
                    }
                } catch (Exception e) {
                    System.err.println(getAID().getName() + " Error in Play action: " + e.getMessage());
                    e.printStackTrace();
                }
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

        private boolean validateNewGame(String msgContent) {
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

}