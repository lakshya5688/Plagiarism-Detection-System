import java.util.Arrays;
import java.util.BitSet;

public class SemanticHashing {
    public static double calculate(String text1, String text2) {
        BitSet hash1 = generateHash(text1);
        BitSet hash2 = generateHash(text2);

        // Calculate Hamming distance
        hash1.xor(hash2);
        int distance = hash1.cardinality();
        return 1 - (double) distance / hash1.size();
    }

    private static BitSet generateHash(String text) {
        int[] hash = new int[128];
        Arrays.fill(hash, 0);

        for (char c : text.toCharArray()) {
            for (int i = 0; i < hash.length; i++) {
                hash[i] += (c >> i) & 1;
            }
        }

        BitSet bitSet = new BitSet(128);
        for (int i = 0; i < hash.length; i++) {
            if (hash[i] > 0) {
                bitSet.set(i);
            }
        }
        return bitSet;
    }
}
