package classprojects;



public class bigMain {

	public static void main(String[] args) {
		double[] target = new double[4];
		target[0] = 0.01;
		target[1] = 0.02;
		target[2] = 0.05;
		target[3] = 0.1;
		double[] loss = new double[4];
		loss[0] = 0.01;
		loss[1] = 0.02;
		loss[2] = 0.05;
		loss[3] = 0.1;
		String path = "/home/jeewan/eclipse-workspace/MAC286Projects/src/Data/"; // your path here

		for (int i = 0; i < 4; i++) {// test all combinations of loss and target
			for (int j = 0; j < 4; j++) {
				Simulator sim = new Simulator(path, "Stocks.txt", loss[i], target[j]);
				sim.run();
				// display the stats for these parameters loss[i], target[j]
				tradeArray Tr = sim.getTrades();
				// print the stats
				System.out.println(loss[i] + ", " + target[j] + Tr.getStats().toString());
				Simulator simETF = new Simulator(path, "ETFs.txt", loss[i], target[j]);
				simETF.run();
				// display the stats for these parameters loss[i], target[j]
				tradeArray Tr1 = simETF.getTrades();
				// print the stats
				System.out.println(loss[i] + ", " + target[j] + Tr1.getStats().toString());
				// Print stats for the combination add the trades together
				Tr.addArray(Tr1);
				System.out.println(loss[i] + ", " + target[j] + Tr.getStats().toString());
			}
		}
		// we need to test what if we exit the trade the same day as we enter it at the
		// close
		// we call that loss=0, target = 0
		Simulator sim = new Simulator(path, "Stocks.txt", 0.0, 0.0);
		sim.run();
		// display the stats for these parameters loss[i], target[j]
		tradeArray Tr = sim.getTrades();
		// print the stats
		System.out.println(0.0 + ", " + 0.0 + Tr.getStats().toString());
		Simulator simETF = new Simulator(path, "ETFs.txt", 0.0, 0.0);
		simETF.run();
		// display the stats for these parameters loss[i], target[j]
		tradeArray Tr1 = simETF.getTrades();
		// print the stats
		System.out.println(0.0 + ", " + 0.0 + Tr1.getStats().toString());
		// Print stats for the combination add the trades together
		Tr.addArray(Tr1);
		System.out.println(0.0 + ", " + 0.0 + Tr.getStats().toString());
	}

}
