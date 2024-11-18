
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
import java.util.Objects;

public class MainAgent extends Agent {

    private ArrayList<PlayerInformation> players;
    private GUI gui;
    private AID[] playerAgents;
    private GameParametersStruct parameters = new GameParametersStruct();
    private String result;
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

    /**
     * In this behavior this agent manages the course of a match during all the
     * rounds.
     */
    private class GameManager extends SimpleBehaviour {


        @Override
        public void action() {


            gui.logLine("GameManager behavior running...");
            //Assign the IDs
             players = new ArrayList<>();
            int lastId = 0;
            for (AID a : playerAgents) {
                players.add(new PlayerInformation(a, lastId++));
            }

            //Initialize (inform ID)
            for (PlayerInformation player : players) {
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setContent("Id#" + player.id + "#" + parameters.N + "," + parameters.R + "," + parameters.F );
                msg.addReceiver(player.aid);
                send(msg);
            }
            //Organize the matches
            for (int round = 0; round < parameters.R; round++){
                for (int i = 0; i < players.size(); i++) {
                    for (int j = i + 1; j < players.size(); j++) { //too lazy to think, let's see if it works or it breaks
                        playGame(players.get(i), players.get(j));
                    }
                }
            }

        }

        private void playGame(PlayerInformation player1, PlayerInformation player2) {
            // Assuming player1.id < player2.id
            gui.logLine("Starting new game between Player " + player1.id + " and Player " + player2.id);

            // Inform players about the new game
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(player1.aid);
            msg.addReceiver(player2.aid);
            msg.setContent("NewGame#" + player1.id + "," + player2.id);
            send(msg);

            String pos1, pos2;

            // Request move from Player 1
            msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setContent("Action");
            msg.addReceiver(player1.aid);
            send(msg);

            gui.logLine("Main Waiting for movement");
            ACLMessage move1 = blockingReceive();
            gui.logLine("Main Received " + move1.getContent() + " from " + move1.getSender().getName());
            pos1 = move1.getContent().split("#")[1];

            // Request move from Player 2
            msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setContent("Position");
            msg.addReceiver(player2.aid);
            send(msg);

            gui.logLine("Main Waiting for movement");
            ACLMessage move2 = blockingReceive();
            gui.logLine("Main Received " + move2.getContent() + " from " + move2.getSender().getName());
            pos2 = move2.getContent().split("#")[1];


            // Calculate results
            int payoff1, payoff2;
            if (Objects.equals(pos1, "C") && Objects.equals(pos2, "C")) { // Both cooperate
                payoff1 = 2; payoff2 = 2;
            } else if (Objects.equals(pos1, "C") && Objects.equals(pos2, "D")) { // Player 1 cooperates, Player 2 defects
                payoff1 = 0; payoff2 = 4;
            } else if (Objects.equals(pos1, "D") && Objects.equals(pos2, "C")) { // Player 1 defects, Player 2 cooperates
                payoff1 = 4; payoff2 = 0;
            } else { // Both defect
                payoff1 = 0; payoff2 = 0;
            }

            gui.logLine("Game Results: Player " + player1.id + " = " + payoff1 + ", Player " + player2.id + " = " + payoff2);

            // Update players' scores
            player1.addScore(payoff1);
            player2.addScore(payoff2);

            // Send results to players
            msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(player1.aid);
            msg.addReceiver(player2.aid);
            result = "Results#"+player1.id+","+player2.id+"#"+pos1+","+pos2+"#"+payoff1+","+payoff2;
            msg.setContent(result);
            gui.logLine(result);
            send(msg);

            // End the game
            msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(player1.aid);
            msg.addReceiver(player2.aid);
            msg.setContent("EndGame");
            send(msg);

            gui.logLine("Game ended between Player " + player1.id + " and Player " + player2.id);

            // Optionally, log the cumulative scores after each game
            gui.logLine("Cumulative Scores: Player " + player1.id + " = " + player1.score + ", Player " + player2.id + " = " + player2.score);
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

    public void resetPlayers() {
        // Logic to reset player statistics here (example)
        for (PlayerInformation player : players) {
            player.resetStats();  // Assuming you have a resetStats() method for players
        }
    }

    public class PlayerInformation {

        AID aid;
        int id;
        int score;

        public PlayerInformation(AID a, int i) {
            aid = a;
            id = i;
            this.score = 0;
        }
        public void addScore(int points) {
            this.score += points;  // Update score by adding points
        }

        @Override
        public boolean equals(Object o) {
            return aid.equals(o);
        }

        public void resetStats() {
            this.score = 0;
        }
    }

    public class GameParametersStruct {

        int N;
        int R;
        double F;

        public GameParametersStruct() {
            N = 2;
            R = 50;
            F = 0.01;

        }
    }

}
