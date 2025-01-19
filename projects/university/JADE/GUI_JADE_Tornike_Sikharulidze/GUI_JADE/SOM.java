import java.util.Locale;

public class SOM {

    private int iGridSide;               // Side of the SOM 2D grid
    private int iInputSize;              // Size of the input vector
    private int iRadio;                  // Neighborhood radius for training
    private double dLearnRate;           // Learning rate
    private double dDecLearnRate;        // Learning rate decay factor
    private double[][][] dGrid;          // SOM grid (weights for each neuron)
    private int[] iBMU_Pos = new int[2]; // Position of the BMU
    private double[] dBMU_Vector;        // BMU weights

    /**
     * Constructor to initialize the SOM grid.
     *
     * @param gridSide  The side length of the SOM grid (2D grid).
     * @param inputSize The size of the input vector.
     */
    public SOM(int gridSide, int inputSize) {
        this.iGridSide = gridSide;
        this.iInputSize = inputSize;
        this.iRadio = iGridSide / 10;
        this.dLearnRate = 1.0;
        this.dDecLearnRate = 0.999;
        this.dBMU_Vector = new double[iInputSize];
        this.dGrid = new double[iGridSide][iGridSide][iInputSize];

        // Initialize the SOM grid with random weights
        initializeGrid();
    }

    /**
     * Initializes the SOM grid with random weights.
     */
    private void initializeGrid() {
        for (int i = 0; i < iGridSide; i++) {
            for (int j = 0; j < iGridSide; j++) {
                for (int k = 0; k < iInputSize; k++) {
                    dGrid[i][j][k] = Math.random();
                }
            }
        }
    }

    /**
     * Finds the Best Matching Unit (BMU) for the given input vector
     * and optionally trains the SOM by updating the neighborhood.
     *
     * @param input The input vector.
     * @param train Whether to train the SOM.
     * @return The coordinates of the BMU as a string ("x,y").
     */
    public String sGetBMU(double[] input, boolean train) {
        int xBMU = 0, yBMU = 0;
        double minDistance = Double.MAX_VALUE;

        // Find the BMU by calculating the Euclidean distance
        for (int i = 0; i < iGridSide; i++) {
            for (int j = 0; j < iGridSide; j++) {
                double distance = calculateDistance(input, dGrid[i][j]);
                if (distance < minDistance) {
                    minDistance = distance;
                    xBMU = i;
                    yBMU = j;
                }
            }
        }

        // Update the BMU position and vector
        iBMU_Pos[0] = xBMU;
        iBMU_Pos[1] = yBMU;
        dBMU_Vector = dGrid[xBMU][yBMU].clone();

        // Train the SOM if requested
        if (train) {
            trainNeighborhood(input, xBMU, yBMU);
            dLearnRate *= dDecLearnRate; // Decay the learning rate
        }

        return xBMU + "," + yBMU;
    }

    /**
     * Trains the SOM by updating the BMU and its neighbors.
     *
     * @param input The input vector.
     * @param xBMU  The x-coordinate of the BMU.
     * @param yBMU  The y-coordinate of the BMU.
     */
    private void trainNeighborhood(double[] input, int xBMU, int yBMU) {
        for (int dx = -iRadio; dx <= iRadio; dx++) {
            for (int dy = -iRadio; dy <= iRadio; dy++) {
                int x = (xBMU + dx + iGridSide) % iGridSide; // Wrap around for toroidal grid
                int y = (yBMU + dy + iGridSide) % iGridSide; // Wrap around for toroidal grid

                // Update the weights of the neuron
                for (int k = 0; k < iInputSize; k++) {
                    double influence = dLearnRate / (1 + dx * dx + dy * dy);
                    dGrid[x][y][k] += influence * (input[k] - dGrid[x][y][k]);
                }
            }
        }
    }

    /**
     * Calculates the Euclidean distance between two vectors.
     *
     * @param vector1 The first vector.
     * @param vector2 The second vector.
     * @return The Euclidean distance.
     */
    private double calculateDistance(double[] vector1, double[] vector2) {
        double sum = 0.0;
        for (int i = 0; i < vector1.length; i++) {
            sum += Math.pow(vector1[i] - vector2[i], 2);
        }
        return sum;
    }
}
