import java.util.*;

public class AdaptiveJaccardSimilarity {
    public static double calculate(String text1, String text2) {
        Set<String> ngrams1 = generateUniqueNgrams(text1, 2, 3); // Set limit on n-gram frequency
        Set<String> ngrams2 = generateUniqueNgrams(text2, 2, 3);

        Set<String> union = new HashSet<>(ngrams1);
        union.addAll(ngrams2);

        Set<String> intersection = new HashSet<>(ngrams1);
        intersection.retainAll(ngrams2);

        return union.isEmpty() ? 0 : (double) intersection.size() / union.size();
    }

    private static Set<String> generateUniqueNgrams(String text, int n, int maxFrequency) {
        String[] words = text.split("\\s+");
        Map<String, Integer> ngramCounts = new HashMap<>();

        for (int i = 0; i <= words.length - n; i++) {
            StringBuilder ngram = new StringBuilder();
            for (int j = 0; j < n; j++) {
                ngram.append(words[i + j]).append(" ");
            }
            String ngramString = ngram.toString().trim();
            ngramCounts.put(ngramString, ngramCounts.getOrDefault(ngramString, 0) + 1);
        }

        Set<String> uniqueNgrams = new HashSet<>();
        for (Map.Entry<String, Integer> entry : ngramCounts.entrySet()) {
            if (entry.getValue() <= maxFrequency) {
                uniqueNgrams.add(entry.getKey());
            }
        }
        return uniqueNgrams;
    }
}
