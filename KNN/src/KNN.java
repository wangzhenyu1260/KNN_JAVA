/**
 * KNN (K Nearest Neighbor) algorithm for supervised learning implemented in Java
 * @author Zhenyu WANG
 * @date 2015-10-3
 *
 */
import java.util.*;

class KNN {
	//Set K Value
	static int k = 3;// # of neighbours
	// the data
	static String[][] sampleSet = {
			{ "high", "low", "5more", "more", "small", "high" },
			{ "med", "vhigh", "2", "2", "big", "low" },
			{ "high", "high", "3", "4", "med", "high" },
			{ "med", "med", "3", "more", "big", "high" },
			{ "vhigh", "med", "2", "4", "med", "med" },
			{ "high", "high", "3", "more", "small", "med" },
			{ "low", "vhigh", "2", "4", "small", "low" },
			{ "vhigh", "low", "2", "more", "big", "high" },
			{ "low", "low", "2", "4", "big", "high" },
			{ "vhigh", "high", "3", "4", "med", "high" } };
	static String[][] testSet = {
			{ "high", "low", "5more", "more", "small", "high" },
			{ "med", "vhigh", "2", "2", "big", "low" },
			{ "high", "high", "3", "4", "med", "high" },
			{ "med", "med", "3", "more", "big", "high" },
			{ "vhigh", "med", "2", "4", "med", "med" },
			{ "high", "high", "3", "more", "small", "med" },
			{ "low", "vhigh", "2", "4", "small", "low" },
			{ "vhigh", "low", "2", "more", "big", "high" },
			{ "low", "low", "2", "4", "big", "high" },
			{ "vhigh", "high", "3", "4", "med", "high" } };
	/**
	 * Returns the majority value in an array of strings majority value is the
	 * most frequent value (the mode) handles multiple majority values (ties
	 * broken at random)
	 *
	 * @param array
	 *            an array of strings
	 * @return the most frequent string in the array
	 */
	private static String findMajorityClass(String[] array) {
		// add the String array to a HashSet to get unique String values
		Set<String> h = new HashSet<String>(Arrays.asList(array));
		// convert the HashSet back to array
		String[] uniqueValues = h.toArray(new String[0]);
		// counts for unique strings
		int[] counts = new int[uniqueValues.length];
		// loop thru unique strings and count how many times they appear in
		// origianl array
		for (int i = 0; i < uniqueValues.length; i++) {
			for (int j = 0; j < array.length; j++) {
				if (array[j].equals(uniqueValues[i])) {
					counts[i]++;
				}
			}
		}

		int max = counts[0];
		for (int counter = 1; counter < counts.length; counter++) {
			if (counts[counter] > max) {
				max = counts[counter];
			}
		}

		// how many times max appears
		// we know that max will appear at least once in counts
		// so the value of freq will be 1 at minimum after this loop
		int freq = 0;
		for (int counter = 0; counter < counts.length; counter++) {
			if (counts[counter] == max) {
				freq++;
			}
		}

		// index of most freq value if we have only one mode
		int index = -1;
		if (freq == 1) {
			for (int counter = 0; counter < counts.length; counter++) {
				if (counts[counter] == max) {
					index = counter;
					break;
				}
			}
			// System.out.println("one majority class, index is: "+index);
			return uniqueValues[index];
		} else {// we have multiple modes
			int[] ix = new int[freq];// array of indices of modes
			// System.out.println("multiple majority classes: " + freq +
			// " classes");
			int ixi = 0;
			for (int counter = 0; counter < counts.length; counter++) {
				if (counts[counter] == max) {
					ix[ixi] = counter;// save index of each max count value
					ixi++; // increase index of ix array
				}
			}
			// now choose one at random
			Random generator = new Random();
			// get random number 0 <= rIndex < size of ix
			int rIndex = generator.nextInt(ix.length);
			// System.out.println("random index: " + rIndex);
			int nIndex = ix[rIndex];
			// return unique value at that index
			return uniqueValues[nIndex];
		}

	}

	public static void main(String args[]) {
		// int k = 3;// # of neighbours
		// list to save car data
		List<Car> carList = new ArrayList<Car>();
		// standard car decide test list
		List<String> testStandardList = new ArrayList<String>();
		// add car data to carList
		carList.add(new Car(sampleSet[0], "unacc"));
		carList.add(new Car(sampleSet[1], "acc"));
		carList.add(new Car(sampleSet[2], "acc"));
		carList.add(new Car(sampleSet[3], "acc"));
		carList.add(new Car(sampleSet[4], "unacc"));
		carList.add(new Car(sampleSet[5], "unacc"));
		carList.add(new Car(sampleSet[6], "unacc"));
		carList.add(new Car(sampleSet[7], "acc"));
		carList.add(new Car(sampleSet[8], "unacc"));
		carList.add(new Car(sampleSet[9], "acc"));
		// load car test list
		testStandardList.add("acc");
		testStandardList.add("unacc");
		testStandardList.add("acc");
		testStandardList.add("acc");
		testStandardList.add("unacc");
		testStandardList.add("unacc");
		testStandardList.add("unacc");
		testStandardList.add("acc");
		testStandardList.add("acc");
		testStandardList.add("unacc");
		// data about unknown car
		// String[] query = { "high", "low", "5more", "more", "small", "high" };
		// test result set
		List<String> testResultList = new ArrayList<String>();
		// find disnaces
		// compute the distance between two vectors
		// Distance function: # of different attributes
		for (int i = 0; i < testSet.length; i++) {
			// list to save distance result
			List<Result> resultList = new ArrayList<Result>();
			for (Car car : carList) {
				double dist = 0.0;
				for (int j = 0; j < car.carAttributes.length; j++) {
					dist += car.carAttributes[j] != testSet[i][j] ? 1 : 0;
					// System.out.print(car.carAttributes[j]+" ");
				}
				resultList.add(new Result(dist, car.decide));
				// System.out.println(distance);
			}
			// System.out.println(resultList);
			Collections.sort(resultList, new DistanceComparator());
			String[] ss = new String[k];
			for (int x = 0; x < k; x++) {
				// System.out.println(resultList.get(x).decide + " .... " +
				// resultList.get(x).distance);
				// get classes of k nearest instances (city names) from the list
				// into an array
				ss[x] = resultList.get(x).decide;
			}
			// String majClass = findMajorityClass(ss);
			testResultList.add(findMajorityClass(ss));
			// System.out.println("Class of new instance is: " + majClass);
		}
		/*
		 * print test result list
		 */
		System.out.println("Test Result:");
		for (int i = 0; i < testResultList.size(); i++) {
			System.out.println(testResultList.get(i));
		}
		/*
		 * compute error rate
		 */
		int correctCount = 0;
		for (int i = 0; i < testStandardList.size(); i++) {
			correctCount += testStandardList.get(i) == testResultList.get(i) ? 1
					: 0;
		}
		System.out.println("K=" + k + ", Correct Rate: " + correctCount * 100
				/ testStandardList.size() + "%, Error Rate: "
				+ (100 - (correctCount * 100 / testStandardList.size())) + "%");

	}// end main

	/*
	 * Car class
	 */
	static class Car {
		String[] carAttributes;
		String decide;

		public Car(String[] instances, String decide) {
			this.decide = decide;
			this.carAttributes = instances;
		}
	}

	/*
	 * Result class
	 */
	static class Result {
		double distance;
		String decide;

		public Result(double distance, String decide) {
			this.decide = decide;
			this.distance = distance;
		}
	}

	// simple comparator class used to compare results via distances
	static class DistanceComparator implements Comparator<Result> {
		public int compare(Result a, Result b) {
			return a.distance < b.distance ? -1 : a.distance == b.distance ? 0
					: 1;
		}
	}

}
