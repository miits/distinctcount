package put.cs.idss.pmds.distinctcount;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

public class HashAlgorithm {

    private int k;
    private int p;
    private int m;
    private Set<Integer> aParams;
    private Set<Integer> bParams;

    public HashAlgorithm(int k, int p, int m) {
        this.k = k;
        this.p = p;
        this.m = m;
        aParams = getRandomParams(k, 1, p - 1);
        bParams = getRandomParams(k, 0, p - 1);
    }

    private Set<Integer> getRandomParams(int n, int lowerBound, int upperBound) {
        Random rnd = new Random();
        Set<Integer> params = new LinkedHashSet<Integer>();
        while(params.size() < n) {
            int p = rnd.nextInt(upperBound);
            if (p < lowerBound) {
                p = lowerBound;
            }
            params.add(p);
        }
        return params;
    }

    public int[] hash(int x) {
        int[] out = new int [k];
        Iterator<Integer> aIter = aParams.iterator();
        Iterator<Integer> bIter = bParams.iterator();
        BigInteger a;
        BigInteger b;
        BigInteger bigX;
        for (int i = 0; i < k; i++) {
            a = new BigInteger(Integer.toString(aIter.next()));
            b = new BigInteger(Integer.toString(bIter.next()));
            bigX = new BigInteger(Integer.toString(x));
            out[i] = (a.multiply(bigX).add(b))
                        .mod(new BigInteger(Integer.toString(p)))
                        .mod(new BigInteger(Integer.toString(m)))
                        .intValue();
        }
        return out;
    }

}