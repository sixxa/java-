SmartAgent

The SmartAgent is an enhanced implementation designed to participate in multi-agent games while making smarter decisions based on past interactions. It uses a basic reinforcement learning strategy to dynamically adjust its behavior, promoting cooperation or punishing defection to maximize long-term rewards.

Features

Dynamic Strategy Adjustment:

Learns from the opponent's actions, adjusting its cooperation probability to optimize outcomes.

Starts with a neutral strategy (50% cooperation) and adapts during the game.

State Management:

Operates through defined states:

waitConfig: Processes initial configuration.

waitGame: Prepares for a new game.

waitAction: Decides and sends an action.

waitResults: Processes game results and adjusts its strategy.

Behavior Customization:

Decision-making logic can be easily extended for more advanced strategies or game-specific optimizations.

How It Works

Initialization:

When the agent is started, it registers itself as a player in the system using the JADE directory.

The agent begins in the waitConfig state, waiting to receive configuration details from the main agent (e.g., its ID and game parameters).

Receiving Configuration:

The agent processes a configuration message in the format Id#[ID]#[N],[R],[S].

It extracts and validates parameters such as its own ID, the number of rounds (N), the reward factor (R), and the starting stock value (S).

Starting a New Game:

Upon receiving a NewGame# message, the agent identifies its opponent and transitions to the waitAction state, ready to make its first decision.

Action Decision:

The agent calculates its next move (C for cooperate, D for defect) using its current cooperation probability.

The decideAction method is called, which randomly chooses an action based on the cooperation probability.

The chosen action is sent back to the main agent in the format Action#[C/D].

Results Processing:

After sending an action, the agent waits for a Results# message, which contains the opponent's last action.

The updateStrategy method adjusts the cooperation probability:

If the opponent cooperates (C), the agent becomes slightly more likely to cooperate.

If the opponent defects (D), the agent reduces its tendency to cooperate.

This simple reinforcement learning mechanism allows the agent to adapt to its opponent's behavior.

Termination:

When the game ends, the agent receives a GameOver message and gracefully shuts down.

Code Structure

Main Class: SmartAgent

Handles agent lifecycle, state transitions, and message processing.

States:

Enum State: Represents the agent's operational states.

Behaviors:

Play: Implements the agent's main behavior loop, processing incoming messages and executing actions.

Methods:

decideAction(): Determines the next action based on the current cooperation probability.

updateStrategy(String opponentAction): Adjusts the cooperation probability based on the opponent's actions.

validateAndSetParameters(ACLMessage msg): Parses and validates configuration messages.

processNewGame(String msgContent): Initializes game parameters for a new game.

Example Message Flow

Configuration:

Message from Main Agent: Id#1#10,5,1.0

Action: The agent validates the configuration and enters the waitGame state.

New Game:

Message from Main Agent: NewGame#1#2

Action: The agent identifies its opponent and prepares for action.

Action:

Message from Main Agent: Action

Action: The agent decides between C or D and sends its choice.

Results:

Message from Main Agent: Results#C

Action: The agent updates its strategy based on the opponent's action.

Game Over:

Message from Main Agent: GameOver

Action: The agent terminates.

