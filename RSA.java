import java.math.BigInteger;
import java.util.Random;

public class RSA {
    public static BigInteger e = new BigInteger("3"); // a=e=3, 5, 7 or (2^16)+1
    public static void main(String[] args) {
        Random r = new Random();
        Integer numberToEncrypt = r.nextInt(100000);
        BigInteger s = new BigInteger(numberToEncrypt.toString()); // My number

        System.out.println("Vårt första valda e är: " + e);
        System.out.println("Vårt valda tal är: " + s);

        BigInteger q = PrimeFinder.generatePrime(512, r);
        BigInteger p = PrimeFinder.generatePrime(512, r);

        // mod with encrypt and decrypt
        BigInteger n = q.multiply(p);

        BigInteger m = (q.subtract(BigInteger.ONE).multiply(p.subtract(BigInteger.ONE)));

        while (!e.gcd(m).equals(BigInteger.ONE)) {
            e = e.add(BigInteger.TWO);
        }

        BigInteger c = encrypt(s, n);
        BigInteger v = euclidsAlgorithm(q, p, m);
        BigInteger z = decrypt(c, v, n); // d=v=53

        System.out.println("Vårt slutgiltiga e blev: " + e);
        System.out.println("Vårt krypterade tal blir: " + c);
        System.out.println("När vi dekrypterar " + c + " får vi " + z + " tillbaka");
    }

    public static BigInteger encrypt(BigInteger s, BigInteger n) {
        return s.modPow(e, n);
    }

    public static BigInteger decrypt(BigInteger c, BigInteger v, BigInteger n) {
        return c.modPow(v, n);
    }

    public static BigInteger euclidsAlgorithm(BigInteger q, BigInteger p, BigInteger m) {
        BigInteger d1;
        BigInteger v1;
        BigInteger v2;
        BigInteger d2;
        do {
            d1 = new BigInteger(m.toString());
            v1 = BigInteger.ZERO; 
            v2 = BigInteger.ONE;
            d2 = new BigInteger(e.toString());

            while (!d2.equals(BigInteger.ZERO)) {
                BigInteger q1 = d1.divide(d2);
                BigInteger t2 = v1.subtract(q1.multiply(v2));
                BigInteger t3 = d1.subtract(q1.multiply(d2));
                v1 = v2;
                d1 = d2;
                v2 = t2;
                d2 = t3;
            }
            if (!d1.equals(BigInteger.ONE)) e.add(BigInteger.TWO);

        } while (!d1.equals(BigInteger.ONE));
        BigInteger v = v1; // This is our d(=v), might be negative

        if (v.compareTo(BigInteger.ZERO) == -1) {
            v = v.add(m);
        }
        return v;
    }
}
