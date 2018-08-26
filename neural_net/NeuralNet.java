package neural_net;

import neural_net.data.Table;
import neural_net.data.TableRow;

/**
 * ---> AI_1: FLY ON A CONSTANT HEIGHT (!= start-height) <---
 * 
 * --> INPUTS <-- y-coordinate: The most vital input is the height of the drone
 * itself pitch: The angle the drone is heading in the vertical plane (expressed
 * in degrees)
 * 
 * --> OUTPUTS <-- The angle of the front wings (left and right stay the same
 * for simplicity)
 * 
 * --> TEST_SUITE <-- The height to which the drone must fly is 30 meters. The
 * drone will fly for 60 seconds. Each 0.1 seconds the distance till the heigth
 * is measured and added to the 'total_score', the object is to minimize the
 * 'total_score'.
 */
public class NeuralNet {
	private int iNodes; // The amount of input nodes
	private int hNodes; // The amount of hidden (middle) nodes
	private int oNodes; // The amount of output nodes

	private Matrix whi; // The matrix that contains the weights between the input and hidden nodes
	private Matrix whh; // The matrix that contains the weights between the hidden nodes and the second layer of hidden nodes
	private Matrix woh; // The matrix that contains the weights between the second layer of hidden nodes and the output node

	/**
	 * The constructor
	 */
	public NeuralNet(int nbInputs, int nbHidden, int nbOutputs) {
		// Set the dimension for the parameters
		iNodes = nbInputs;
		hNodes = nbHidden;
		oNodes = nbOutputs;
		
		whi = new Matrix(hNodes, iNodes + 1); // Create the first layer weights, include bias weight
		whh = new Matrix(hNodes, hNodes + 1); // Create second layer weights include bias weight
		woh = new Matrix(oNodes, hNodes + 1); // Create second layer weights include bias weight

		// set the matrices to random values
		whi.randomize();
		whh.randomize();
		woh.randomize();
	}
	
	/**
	 * Mutation function for the generic algorithm
	 * 
	 * @param mr: The mutationrate
	 */
	void mutate(float mr) {
		whi.mutate(mr);
		whh.mutate(mr);
		woh.mutate(mr);
	}
	
	/**
	 * Calculate the output values by feeding forward through the neural network
	 * 
	 * @param inputsArray
	 * @return
	 */
	float[] output(float[] inputsArray) {
		// Convert the array to a matrix, note that 'woh' has nothing to do with it 
		// (it's just a function in the matrix class)
		Matrix inputs = woh.singleColumnMatrixFromArray(inputsArray);
		
		// Add bias
		Matrix inputsBias = inputs.addBias();
		
		// Next: Calculate the guessed output
		
		// Apply layer one weights to the inputs
		Matrix hiddenInputs = whi.dot(inputsBias);
		
		// Pass through activation function(sigmoid)
		Matrix hiddenOutputs = hiddenInputs.activate();
		
		// Add bias
		Matrix hiddenOutputsBias = hiddenOutputs.addBias();
		
		// Apply the layer two weights
		Matrix hiddenInputs2 = whh.dot(hiddenOutputsBias);
		Matrix hiddenOutputs2 = hiddenInputs2.activate();
		Matrix hiddenOutputsBias2 = hiddenOutputs2.addBias();
		
		// Apply level three weights
		Matrix outputInputs = woh.dot(hiddenOutputsBias2);
		
		// Pass through activation function(sigmoid)
		Matrix outputs = outputInputs.activate();
		
		// Convert to an array and return
		return outputs.toArray();
	}
	
	/**
	 * Crossover function for genetic algorithm
	 * 
	 * @param partner
	 * @return
	 */
	NeuralNet crossover(NeuralNet partner) {
		// Creates a new child with layer matrices from both parents
		NeuralNet child = new NeuralNet(iNodes, hNodes, oNodes);
		child.whi = whi.crossover(partner.whi);
		child.whh = whh.crossover(partner.whh);
		child.woh = woh.crossover(partner.woh);
		return child;
	}
	
	/**
	 * Return a neural net which is a clone of this Neural net
	 */
	NeuralNet clone_neuralnet() {
		NeuralNet clone = new NeuralNet(iNodes, hNodes, oNodes);
		clone.whi = whi.clone_matrix();
		clone.whh = whh.clone_matrix();
		clone.woh = woh.clone_matrix();
		return clone;
	}
	
	Table NetToTable() {
		
		// Create table
		Table t = new Table();
		
		// Convert the matricies to an array
		float[] whiArr = whi.toArray();
		float[] whhArr = whh.toArray();
		float[] wohArr = woh.toArray();
		
		// Set the amount of columns in the table
		for (int i = 0; i < Math.max(Math.max(whiArr.length, whhArr.length), wohArr.length); i++) {
			t.addColumn();
		}
		
		// Set the FIRST row as whi
		TableRow tr = t.addRow();
		for (int i = 0; i < whiArr.length; i++) {
			tr.setFloat(i, whiArr[i]);
		}
		
		// Set the SECONS row as whh
		tr = t.addRow();
		for (int i = 0; i < whhArr.length; i++) {
			tr.setFloat(i, whhArr[i]);
		}
		
		// Set the THIRD
		tr = t.addRow();
		for (int i = 0; i < wohArr.length; i++) {
			tr.setFloat(i, wohArr[i]);
		}
		
		// Return table
		return t;
	}
	
	// Takes in table as parameter and overwrites the matrices data for this neural network
	void TableToNet(Table t) {
		// Create arrays to temporarily store the data for each matrix
		float[] whiArr = new float[whi.rows * whi.cols];
		float[] whhArr = new float[whh.rows * whh.cols];
		float[] wohArr = new float[woh.rows * woh.cols];
		
		// Set the whi array as the FIRST row of the table
		TableRow tr = t.getRow(0);
		for (int i = 0; i < whiArr.length; i++) {
			whiArr[i] = tr.getFloat(i);
		}
		
		// Set the whh array as the SECOND row of the table
		tr = t.getRow(1);
		for (int i = 1; i < whhArr.length; i++) {
			whhArr[i] = tr.getFloat(i);
		}
		
		// Set the woh array as the THIRD row of the table
		tr = t.getRow(2);
		for (int i = 0; i < wohArr.length; i++) {
			wohArr[i] = tr.getFloat(i);
		}
		
		whi.fromArray(whiArr);
		whh.fromArray(whhArr);
		woh.fromArray(wohArr);
	}
}
