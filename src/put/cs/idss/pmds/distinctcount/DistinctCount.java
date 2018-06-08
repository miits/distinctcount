package put.cs.idss.pmds.distinctcount;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DistinctCount {
	int size;
	int k;
	int range;
	BloomFilter bloomFilter;
	BloomFilter linearCounter;
    HashMap<Integer,Integer> hashCounter;
    int distinctCount;

	public DistinctCount(int size, int k, int range) {
		this.size = size;
		this.k = k;
        this.range = range;
        this.distinctCount = 0;
		this.bloomFilter = new BloomFilter(size, k, range);
		this.linearCounter = new BloomFilter(size, 1, range);
        this.hashCounter = new HashMap<>();
	}

    public void addCounter(int key) {
        if (!hashCounter.containsKey(key)) {
            hashCounter.put(key, 0);
            distinctCount++;
        }
    }

    public void addBloom(int key) {
        if (!bloomFilter.contains(key)) {
            bloomFilter.add(key);
            distinctCount++;
        }
    }

    public void addLinear(int key) {
        if (!linearCounter.contains(key)) {
            linearCounter.add(key);
            distinctCount++;
        }
    }

	int getDistinctCount() {
		return distinctCount;
	}

	double getLinearCount(BloomFilter counter) {
        double inv = 1/counter.zeros();
	    double log = Math.log(inv);
	    return counter.size * log / counter.k;
    }

	public static void main(String[] args) throws Exception {


		String path = "data\\facts.csv";

		long sTime = System.currentTimeMillis();

		int size = 500000;
		int k = 3;
		int range = size * 10;
		String separator = ",";

		DistinctCount dc = new DistinctCount(size, k, range);

        BufferedReader br = new BufferedReader(new FileReader(path));
        String header = br.readLine();
        String line;
        while ((line = br.readLine()) != null) {
            String[] fact = line.split(separator);
//            dc.addBloom(Integer.parseInt(fact[0]));
//            dc.addLinear(Integer.parseInt(fact[0]));
            dc.addCounter(Integer.parseInt(fact[0]));
        }
		
		long runningTime = System.currentTimeMillis() - sTime;

//		System.out.println("Distinct count = " + dc.getLinearCount(dc.linearCounter));
        System.out.println("Distinct count = " + dc.getDistinctCount());
		System.out.println("Running time = " + runningTime);

        //Linear count: 391400.2950502186 time: 34758
        //Bloom count: 392228.8753043585 time: 75870
        //Hash count: 332123 time: 14805
	}
	
}
