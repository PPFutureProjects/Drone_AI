package neural_net;

import neural_net.data.Table;
import neural_net.data.TableRow;
import neural_net.core.PApplet;

public class FlyFixedHeight {
	
	PApplet pApplet = new PApplet();

	// Drone specific parameters
	private static double REQ_HEIGHT = 50; // The requested height of the drone
	private float height = 4.9f; // The initial height of the drone
	private float pitch = 0; // The initial pitch of the drone
	private float speed = 0; // The initial speed of the drone
	private double updates_left = 2000; // The lifetime of the drone (amount of updates until end)
	private float[] reqRadWings = new float[]{0f, 0f};
	
	// AI specific parameters
	NeuralNet brain; // The neural net of the drone
	float[] inputNodes;  // The input-nodes of the neural network
	float[] outputNodes;  // The output of the neural network
	float points; // The points the drone will receive (minimize!)
	float fitness; // The total fitness of the drone will receive (maximize!)
	
	/**
	 * The constructor
	 */
	public FlyFixedHeight() {
		brain = new NeuralNet(3, 10, 30);  // Create a neural net with three input-nodes, three nodes in the hidden layers, and one output-node
		updates_left = 2000;
	}
	
	/**
	 * Mutate the neural net
	 * @param mr: Mutation ratio
	 */
	public void mutate(float mr) {
		brain.mutate(mr);
	}
	
	/**
	 * Use the neural net to calculate which is the best angle of the front wings.
	 */
	public void setAngleWings() {
		outputNodes = brain.output(inputNodes);
		
		// Calculate the best node
		float max = 0;
		int maxIndex = 0;
		for (int i = 0; i < outputNodes.length; i++) {
			if (max < outputNodes[i]) {
				max = outputNodes[i];
				maxIndex = i;
			}
		}
		
		// Set the wings-rad to the corresponding max-node
		float reqRad = (float) (-Math.PI/6 + Math.PI/90 * maxIndex);
		reqRadWings[0] = reqRad;
		reqRadWings[1] = reqRad;
	}
	
	/**
	 * Return the best fitted angle of the front wings.
	 */
	public float[] getResult() {
		return reqRadWings;
	}
	
	/**
	 * Calculate the fitness, since the main object is to maximize the fitness, we will take the reciprocal of the total score (points).
	 */
	public void calcFitness() {
		fitness = 1/points; 
	}
	
	/**
	 * Crossover function used in the generic algorithm.
	 */
	FlyFixedHeight crossover(FlyFixedHeight partner) {
		FlyFixedHeight child = new FlyFixedHeight();
		
		child.brain = brain.crossover(partner.brain);
		return child;
	}
	
	/**
	 * Clone the drone
	 */
	FlyFixedHeight cloneFlyFixedHeight() {
		FlyFixedHeight clone = new FlyFixedHeight();
		clone.brain = brain.clone_neuralnet();
		return clone;
	}
	
	/**
	 * Store the drone in an excell file.
	 */
	void saveDrone(int droneNo, int score, int popID) {
		// Save the drones top score and its population id
		Table droneStats = new Table();
		droneStats.addColumn("Top Score");
		droneStats.addColumn("PopulationID");
		TableRow tr = droneStats.addRow();
		tr.setFloat(0, score);
		tr.setInt(1, popID);
		
		pApplet.saveTable(droneStats, "data/DroneStats" + droneNo + ".csv");
		
		// Save the drone its brain
		pApplet.saveTable(brain.NetToTable(), "data/Drone" + droneNo + ".csv");
	}
	
	/**
	 * Load drone from excell file
	 */
	FlyFixedHeight loadDrone(int droneNo) {
		FlyFixedHeight load = new FlyFixedHeight();
		Table t = pApplet.loadTable("data/Drone" + droneNo + ".csv");
		load.brain.TableToNet(t);
		return load;
	}
	
	
	
	
	
	/** TODO
	 * Used for testing purposes.
	 */
	public void do_something() {
		height += 1f;
		System.out.println("input: " + inputNodes[0]);
		System.out.println("height: " + height);
	}
	
	public void add_points(float height_drone) {
		points += Math.abs(REQ_HEIGHT - height_drone);
	}

}
