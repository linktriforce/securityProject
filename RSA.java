import java.math.BigInteger;
import java.util.Random;

/**
 * Authors: Albin Olausson, Ludwig Sinclair
 */
public class RSA {
    public static BigInteger e = new BigInteger("65537"); // a=e=3, 5, 7 or (2^16)+1

    public static void main(String[] args) {
        Random r = new Random();

        // Question C: Uncomment this to test the algoritm
        // System.out.println(testEuclidsAlgorithm() ? "Algoritmen funkar!" :
        // "Algoritmen fungerar inte...");

        // BigInteger q = PrimeFinder.generatePrime(512, r);
        // BigInteger p = PrimeFinder.generatePrime(512, r);
        BigInteger q = new BigInteger(
                "8307769051273421780696919063666844836718940826346093732248802002593429761688688029342383461687584069007551554382663037097856353229202050400087959602693091");
        BigInteger p = new BigInteger(
                "7207597317903766750519784230994376914317599587332274160752033968574212627910105282787635236581386230309938884368320929301279880959561136587018980259548659");

        BigInteger m = (q.subtract(BigInteger.ONE).multiply(p.subtract(BigInteger.ONE)));

        // Compute the modular inverse of e using Euclid's algorithm
        BigInteger v = euclidsAlgorithm(m);

        BigInteger n = q.multiply(p);

        // Generate a number s so that 0 < s < n
        Integer numberToEncrypt = r.nextInt(100000);
        BigInteger s = new BigInteger(numberToEncrypt.toString());
        System.out.println("s: " + s);

        BigInteger c = encrypt(s, n);
        BigInteger z = decrypt(c, v, n);

        System.out.println("v: " + v);
        System.out.println("krypterat c: " + c);
        System.out.println("När vi dekrypterar det får vi z: " + z + " tillbaka");
    }

    /**
     * RSA encryption: ciphertext = s^e mod n
     */
    public static BigInteger encrypt(BigInteger s, BigInteger n) {
        return s.modPow(e, n);
    }

    /**
     * RSA decryption: plaintext = c^v mod n
     */
    public static BigInteger decrypt(BigInteger c, BigInteger v, BigInteger n) {
        return c.modPow(v, n);
    }

    /**
     * Euclid's algorithm to compute the modular inverse of e modulo m
     */
    public static BigInteger euclidsAlgorithm(BigInteger m) {
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
            if (!d1.equals(BigInteger.ONE)) {
                System.out.println(
                        "d: " + d1 + ". Eftersom d != 1 fungerar inte vår algoritm. Vi ökar värdet på e med 2");
                e = e.add(BigInteger.TWO);
            }
        } while (!d1.equals(BigInteger.ONE));
        BigInteger v = v1;

        // add modulus to v if it is negative
        if (v.compareTo(BigInteger.ZERO) == -1) {
            v = v.add(m);
        }
        return v;
    }

    /**
     * Method to test if Euclid's algorithm works as expected
     */
    public static boolean testEuclidsAlgorithm() {
        Random rand = new Random();

        // e = new BigInteger(512, rand).setBit(0); // a=e=3, 5, 7 or (2^16)+1
        // BigInteger m = new BigInteger(512, rand).clearBit(0); //Vårt random modulus

        // Question C2
        BigInteger q1 = new BigInteger(
                "8307769051273421780696919063666844836718940826346093732248802002593429761688688029342383461687584069007551554382663037097856353229202050400087959602693091");
        BigInteger p1 = new BigInteger(
                "7207597317903766750519784230994376914317599587332274160752033968574212627910105282787635236581386230309938884368320929301279880959561136587018980259548659");
        e = new BigInteger("65537");
        BigInteger m = (q1.subtract(BigInteger.ONE).multiply(p1.subtract(BigInteger.ONE)));

        BigInteger v1 = euclidsAlgorithm(m);

        System.out.println(v1.gcd(e).mod(m).equals(BigInteger.ONE));
        System.out.println("v: " + v1);
        System.out.println("e: " + e);
        System.out.println("m: " + m);

        return ((v1.multiply(e)).mod(m).equals(BigInteger.ONE));
    }
}
