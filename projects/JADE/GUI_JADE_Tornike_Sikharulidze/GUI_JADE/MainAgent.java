import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class MainAgent extends Agent {

    private GUI gui;
    private AID[] playerAgents;
    private PlayerInformation.GameParametersStruct parameters = new PlayerInformation.GameParametersStruct();
    private double sharesPrice = 1.5;


    @Override
    protected void setup() {
        gui = new GUI(this);
        System.setOut(new PrintStream(gui.getLoggingOutputStream()));

        updatePlayers();
        gui.logLine("Agent " + getAID().getName() + " is ready.");
    }

    public int updatePlayers() {
        gui.logLine("Updating player list...");

        // Create a template for the search, specifying we want players
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Player"); // Looking for agents that offer the "Player" service
        template.addServices(sd);

        try {
            // Search for players using the DFService
            DFAgentDescription[] result = DFService.search(this, template);

            // If players were found, log the count
            if (result.length > 0) {
                gui.logLine("Found " + result.length + " players.");
            } else {
                gui.logLine("No players found.");
                return 0; // Exit early if no players are found
            }

            // Initialize the playerAgents array and populate it with the found players' AIDs
            playerAgents = new AID[result.length];
            for (int i = 0; i < result.length; ++i) {
                playerAgents[i] = result[i].getName(); // Store AID of each player

            }

            // Convert the AIDs to player names (just their name for the GUI display)
            String[] playerNames = new String[playerAgents.length];
            for (int i = 0; i < playerAgents.length; i++) {
                playerNames[i] = playerAgents[i].getName(); // Get the name of each player
                gui.addRow(playerAgents[i].getName(), "x", "x", "x", "x", "x");
            }

            // Update the GUI to show the player list
            gui.setPlayersUI(playerNames); // This method in GUI updates the UI with player names

            return result.length; // Return the number of players found

        } catch (FIPAException fe) {
            // If there's an error with DFService, log the error message
            gui.logLine("Error while searching for players: " + fe.getMessage());
            return 0; // Return 0 if there's an error
        }
    }


    public int newGame() {
        // Log the start of a new game
        gui.logLine("Starting a new game...");

        addBehaviour(new GameManager());
        return 0;
    }

    private boolean stopGame = false;  // A flag to stop the game
    private boolean paused = false;    // A flag to pause the game


    // Method to pause the game
    public void pauseGame() throws InterruptedException {
        paused = true;
        gui.logLine("Game is paused.");
        Thread.sleep(1000);
    }

    // Method to continue the game
    public void continueGame() {
        if (paused) {
            paused = false;
            gui.logLine("Game is resuming.");

        } else {
            gui.logLine("Game is already running.");
        }
    }

    /**
     * In this behavior this agent manages the course of a match during all the
     * rounds.
     */
    private class GameManager extends SimpleBehaviour {




        // Modified action method to check for pause/stop/continue condition
        @Override
        public void action() {

            for (int i = 1; i <= 10; i++) {
                synchronized (this) {
                    while (paused) {  // Check if paused is true
                        try {
                            wait();  // Pause the thread
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();  // Handle interruption
                        }
                    }
                }
            }

            // Assign the IDs to players
            ArrayList<PlayerInformation> players = new ArrayList<>();
            int lastId = 0;
            for (AID a : playerAgents) {
                players.add(new PlayerInformation(a, lastId++));
            }

            // Initialize (inform ID)
            for (PlayerInformation player : players) {
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setContent("Id#" + player.id + "#" + parameters.N + "," + parameters.R + "," + parameters.F);
                msg.addReceiver(player.aid);
                send(msg);
            }



            // Organize the matches and process rounds
            for (int currentRound = 1; currentRound <= parameters.R; currentRound++) {
                gui.leftPanelRoundsLabel.setText(currentRound + "/" + parameters.R);
                parameters.inflationRate = getInflationRate(currentRound);
                sharesPrice = getIndexValue(currentRound);




                // Play all matches
                for (int i = 0; i < players.size(); i++) {
                    for (int j = i + 1; j < players.size(); j++) {
                        playGame(players.get(i), players.get(j));
                    }
                }

                // Log the results after the matches
                gui.logLine("Results are: ");
                for (int i = 0; i < players.size(); i++) {
                    Object[] row = gui.getRow(i);
                    gui.logLine(row[0] + " payoff: " + row[1] + " stocks: " + row[5]);
                }

                // Process each player's decision and update the results
                for (int i = 0; i < players.size(); i++) {
                    PlayerInformation player = players.get(i);

                    // Handle round end and get the decision
                    roundOver(player);
                    ACLMessage decision = blockingReceive();
                    processSharesOperation(decision, player);

                    // Update the table with new results
                    gui.updateTable(players);
                }
            }


            // Game over phase
            if (!stopGame) {  // Only proceed with game over if the game was not stopped early
                gameOver(players);
            }
            gui.updateTable(players);

        }



        private void gameOver(List<PlayerInformation> players) {
            players.forEach(player -> {
                player.sellShares(player.Shares, sharesPrice, parameters.F);
                String msgContent = String.format("GameOver#%d#%f", player.id, player.currentReward);

                gui.logLine(msgContent);

                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(player.aid);
                msg.setContent(msgContent);
                send(msg);
            });
        }

        private void processSharesOperation(ACLMessage decisionMessage, PlayerInformation player) {
            gui.logLine(String.format("Main Received decision %s from %s",
                    decisionMessage.getContent(),
                    decisionMessage.getSender().getName()));

            String[] decisionSplit = decisionMessage.getContent().split("#");
            double assetsToOperate = Double.parseDouble(decisionSplit[1]);
            String decision = decisionSplit[0];

            gui.logLine(decisionSplit[1] + " this was decision 222");

            switch (decision) {
                case "Buy":
                    player.buyShares(assetsToOperate, sharesPrice);
                    break;
                case "Sell":
                    player.sellShares(assetsToOperate, sharesPrice, parameters.F);
                    break;
                default:
                    gui.logLine("Invalid decision received: " + decision);
                    break;
            }

            sendAccounting(player);
        }

        public void sendAccounting(PlayerInformation player) {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(player.aid);
            msg.setContent(String.format("Accounting#%d#%.2f#%.2f", player.id, player.currentReward, player.Shares));
            send(msg);
        }

        public double getIndexValue (int round){
            return 7.35;
        }
        public double getInflationRate (int round){return 0.05;}


        public int[] getReward(String player1, String player2) {
            // Define payoffs in a more readable manner using a map of player combinations
            if ("C".equals(player1) && "C".equals(player2)) {
                return new int[]{2, 2}; // C vs C -> 2, 2
            } else if ("C".equals(player1) && "D".equals(player2)) {
                return new int[]{0, 4}; // C vs D -> 0, 4
            } else if ("D".equals(player1) && "C".equals(player2)) {
                return new int[]{4, 0}; // D vs C -> 4, 0
            } else if ("D".equals(player1) && "D".equals(player2)) {
                return new int[]{0, 0}; // D vs D -> 0, 0
            }

            // If no valid combination, throw an exception
            throw new IllegalArgumentException("Invalid input: Only 'C' or 'D' allowed for each player.");
        }


        private void playGame(PlayerInformation player1, PlayerInformation player2) {
            // Send new game message to both players
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(player1.aid);
            msg.addReceiver(player2.aid);
            msg.setContent("NewGame#" + player1.id + "," + player2.id);
            send(msg);

            // Wait for and process actions from both players
            String pos1, pos2;

            // Player 1 action
            msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setContent("Action");
            msg.addReceiver(player1.aid);
            send(msg);
            gui.logLine("Main Waiting for movement");
            ACLMessage move1 = blockingReceive();
            gui.logLine("Main Received " + move1.getContent() + " from " + move1.getSender().getName());
            pos1 = move1.getContent().split("#")[1];

            // Player 2 action
            msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setContent("Action");
            msg.addReceiver(player2.aid);
            send(msg);
            gui.logLine("Main Waiting for movement");
            ACLMessage move2 = blockingReceive();
            gui.logLine("Main Received " + move2.getContent() + " from " + move2.getSender().getName());
            pos2 = move2.getContent().split("#")[1];

            // Calculate payoff based on positions
            int[] payoff = getReward(pos1, pos2);

            // Send results to both players
            msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(player1.aid);
            msg.addReceiver(player2.aid);
            String result = String.format("Results#%d,%d#%s,%s#%d,%d", player1.id, player2.id, pos1, pos2, payoff[0], payoff[1]);
            msg.setContent(result);
            gui.logLine(result);
            send(msg);

            // End the game
            msg.setContent("EndGame");
            send(msg);

            player1.addToRoundReward(payoff[0]);
            player2.addToRoundReward(payoff[1]);

// Update decision counts for player1
            player1.decisionC += pos1.equals("C") ? 1 : 0;
            player1.decisionD += pos1.equals("D") ? 1 : 0;

// Update decision counts for player2
            player2.decisionC += pos2.equals("C") ? 1 : 0;
            player2.decisionD += pos2.equals("D") ? 1 : 0;

        }

        private void roundOver(PlayerInformation player) {
            player.finishRound(parameters.inflationRate);

            String roundOverMessage = String.format("RoundOver#%d#%f#%f#%f#%f#%f",
                    player.id,
                    (double) player.roundReward,        // Cast to double if it's an Integer
                    (double) player.currentReward,      // Cast to double if it's an Integer
                    (double) parameters.inflationRate,  // Cast to double if it's an Integer
                    (double) player.Shares,             // Cast to double if it's an Integer
                    (double) sharesPrice);              // Cast to double if it's an Integer




            gui.logLine(roundOverMessage + " round is over");

            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(player.aid);
            msg.setContent(roundOverMessage);
            send(msg);
        }


        @Override
        public boolean done() {
            return true;
        }

        private int parseMove(String move) {
            if ("C".equalsIgnoreCase(move)) {
                return 1; // Cooperate
            } else if ("D".equalsIgnoreCase(move)) {
                return 0; // Defect
            }
            return -1; // Invalid move (optional: handle this case if needed)
        }

    }

    public static class PlayerInformation {

        AID aid;
        int id;
        int score;
        int roundReward, decisionC, decisionD;
        double Shares, currentReward;

        public PlayerInformation(AID a, int i) {
            aid = a;
            id = i;
            roundReward = 0;
        }

        // Truncates a double value to 2 decimal places
        public double truncate(double value) {
            return Double.parseDouble(String.format("%.2f", value));
        }

        @Override
        public boolean equals(Object o) {
            return aid.equals(o);
        }

        // Adds the payoff amount to the current round's payoff
        public void addToRoundReward(int payoffAmount) {
            roundReward += payoffAmount;
        }

        // Ends the round, applies inflation, and resets the round payoff
        public void finishRound(double inflationRate) {
            currentReward = truncate((currentReward + roundReward) * (1 - inflationRate));
            roundReward = 0;  // Reset the round payoff
        }

        // Buys stocks, ensuring the player has enough funds (payoff)
        public void buyShares(double amountToBuy, double stockPrice) {
            if (amountToBuy <= currentReward) {
                currentReward = truncate(currentReward - amountToBuy);
                Shares = truncate(Shares + (amountToBuy / stockPrice));
            }
        }

        // Sells stocks, ensuring the player has enough stocks
        public void sellShares(double amountToSell, double stockPrice, double feeRate) {
            if (amountToSell <= Shares) {
                Shares = truncate(Shares - amountToSell);
                currentReward = truncate(currentReward + (amountToSell * stockPrice * (1 - feeRate)));
            }
        }

        public static class GameParametersStruct {

            int N;
            static int R;
            static double F;
            double inflationRate;

            public GameParametersStruct() {
                N = 2;
                R = 500;
                F = 0.01;
                inflationRate = 0.1;
            }
        }
    }
}