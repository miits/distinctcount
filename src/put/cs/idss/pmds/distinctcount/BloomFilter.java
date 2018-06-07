package put.cs.idss.pmds.distinctcount;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Random;

public class BloomFilter {

	int size = 0;
	int k = 0;
	int p = 2;
	HashAlgorithm algorithm;
	BitSet array;
	
	public BloomFilter(int size, int k, int range) {
		this.size = size;
		this.k = k;
		this.p = getPrimeGreaterThan(range);
		this.algorithm = new HashAlgorithm(k, p, size);
		this.array = new BitSet();
	}
	
	public void add(int key) {
		int[] out = algorithm.hash(key);
		for (int i : out) {
			array.set(i);
		}
	}
	
	public Boolean contains(int key) {
		int[] out = algorithm.hash(key);
		for (int i : out) {
			if (!array.get(i)) {
				return false;
			}
		}
		return true;
	}

	private boolean isPrime(int x) {
		for(int i = 2; i < x; i++) {
			if (x % i == 0) return false;
		}
		return true;
	}

	private int getPrimeGreaterThan(int x) {
		while (!isPrime(++x));
		return x;
	}
	
	public static void main(String[] args) {
		
		int n = 10000; 
		int range = 1000000;
		double factor = 10;
		int size = (int) Math.round(factor * n);

		int k = 6;
		
		Random random = new Random(0);
		
		BloomFilter bf = new BloomFilter(size, k, range);
		
		HashSet<Integer> set = new HashSet<Integer>(n);
		
		while(set.size() < n) {
			set.add(random.nextInt(range));
		}
		
		for(int item : set) {
			bf.add(item);
		}
		
		int TP = 0, FP = 0, TN = 0, FN = 0;
		
		for(int i = 0; i < range; i++) {
			int key = i; //random.nextInt(range);
			Boolean containsBF = bf.contains(key);
			Boolean containsHS = set.contains(key);
			
			//System.out.println(key + " " + containsBF + " " + containsHS);
			
			if(containsBF && containsHS) {
				TP++;
			} else if(!containsBF && !containsHS) {
				TN++;
			} else if(!containsBF && containsHS) {
				FN++;
			}  else if(containsBF && !containsHS) {
				FP++;
			}   
		}
		
		System.out.println("TP = " + String.format("%6d", TP) + "\tTPR = " + String.format("%1.4f", (double) TP/ (double) n));
		System.out.println("TN = " + String.format("%6d", TN) + "\tTNR = " + String.format("%1.4f", (double) TN/ (double) (range-n)));
		System.out.println("FN = " + String.format("%6d", FN) + "\tFNR = " + String.format("%1.4f", (double) FN/ (double) (n)));
		System.out.println("FP = " + String.format("%6d", FP) + "\tFPR = " + String.format("%1.4f", (double) FP/ (double) (range-n)));
		
	}

}
