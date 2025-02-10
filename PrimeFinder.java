import java.math.BigInteger;
import java.util.HashSet;
import java.util.Random;

public class PrimeFinder {

    public static void main(String[] args) {
        int bitSize = 1024;
        long totalTime = System.currentTimeMillis();
        Random rand = new Random();

        for (int i = 0; i<100; i++) {
            generatePrime(bitSize, rand);
            System.out.println((i+1) + " primtal har hittats och det har hitills gått " +(System.currentTimeMillis()-totalTime) + "ms");
        }
    }

    /**
     * Finds and returns a big prime using Rabin-Miller Algorithm
     * @param bitSize
     * @param n
     * @param startTime
     * @return BigInteger prime
     */
    public static BigInteger generatePrime(int bitSize, Random rand) {
        boolean isPrime = false;
        int k = 20;

        BigInteger n = BigInteger.ZERO;
        BigInteger five = new BigInteger("5");
        // Algorithm to find out if the generetad number is a prime
        while (!isPrime) {
            // Generate a new random odd integer that is strictly bigger than 3
            do {
                n = new BigInteger(bitSize, rand).setBit(0);
                if (n.mod(five).equals(BigInteger.ZERO)) n.add(BigInteger.TWO);
            } while (n.compareTo(new BigInteger("3")) == -1);

            // Find r and s för the function n = (2^r)s + 1 such that s is odd
            BigInteger s = n.subtract(BigInteger.ONE);
            int r = 0;
            while (s.and(BigInteger.ONE).equals(BigInteger.ZERO)) {
                r++;
                s = s.shiftRight(1);
            }

            // Find the base a such that 0 < a < n and place it in a set
            HashSet<BigInteger> listOfAs = new HashSet<>();
            for (int i = 0; i < k; i++) {
                BigInteger a;
                boolean found = false;
                do {
                    a = new BigInteger(bitSize, rand);
                    if (a.compareTo(BigInteger.TWO) == 1 && a.compareTo(n.subtract(BigInteger.TWO)) == -1) {
                        if (listOfAs.add(a)) {
                            found = true;
                        }
                    }
                } while (!found);
            }

            // If a prime is found, isPrime will be true and the while loop breaks
            boolean isThisPrime = true;
            for (BigInteger a : listOfAs) {
                BigInteger x = a.modPow(s, n);
                if (testPrime(x, n, s, a, r) == Primestates.COMPOSITE) {
                    isThisPrime = false;
                }
            }

            isPrime = isThisPrime;
        }
        return n;
    }

    /**
     * Rabin-Miller Pseudoprime Test
     * 
     * @param x, n, s, a, r
     * @return enum Primestate
     */
    private static Primestates testPrime(BigInteger x, BigInteger n, BigInteger s, BigInteger a, int r) {
        if (x.equals(BigInteger.ONE) || x.equals(n.subtract(BigInteger.ONE))) {
            return Primestates.PROBABLY_PRIME;
        }
        for (int j = 1; j < r - 1; j++) {
            x = a.modPow(s.multiply(new BigInteger(Integer.toString(2^j))), n);
            //x.modPow(BigInteger.TWO, n);
            if (x.equals(BigInteger.ONE)) {
                return Primestates.COMPOSITE;
            }
            if (x.equals(n.subtract(BigInteger.ONE))) {
                return Primestates.PROBABLY_PRIME;
            }
        }

        return Primestates.COMPOSITE;
    }

}
