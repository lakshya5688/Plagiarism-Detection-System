import java.util.*;

public class SlidingWindowSimilarity {
    public static double calculate(String text1, String text2) {
        int windowSize1 = Math.max(4, text1.split("\\s+").length / 10);
        int windowSize2 = Math.max(4, text2.split("\\s+").length / 10);

        List<String> windows1 = generateWindows(text1, windowSize1);
        List<String> windows2 = generateWindows(text2, windowSize2);

        double totalScore = 0;
        int matches = 0;

        for (String window1 : windows1) {
            for (String window2 : windows2) {
                double similarity = computeJaccardWithDensity(window1, window2);
                if (similarity > 0.3) { // Only consider windows with meaningful similarity
                    totalScore += similarity;
                    matches++;
                }
            }
        }

        return matches == 0 ? 0 : totalScore / matches;
    }

    private static List<String> generateWindows(String text, int windowSize) {
        List<String> windows = new ArrayList<>();
        String[] words = text.split("\\s+");
        for (int i = 0; i <= words.length - windowSize; i++) {
            StringBuilder window = new StringBuilder();
            for (int j = i; j < i + windowSize; j++) {
                window.append(words[j]).append(" ");
            }
            windows.add(window.toString().trim());
        }
        return windows;
    }

    private static double computeJaccardWithDensity(String text1, String text2) {
        Set<String> words1 = new HashSet<>(Arrays.asList(text1.split("\\s+")));
        Set<String> words2 = new HashSet<>(Arrays.asList(text2.split("\\s+")));

        // Apply a unique word density check; reduce similarity if density is low
        double density1 = (double) words1.size() / text1.split("\\s+").length;
        double density2 = (double) words2.size() / text2.split("\\s+").length;

        Set<String> union = new HashSet<>(words1);
        union.addAll(words2);

        Set<String> intersection = new HashSet<>(words1);
        intersection.retainAll(words2);

        double jaccard = union.isEmpty() ? 0 : (double) intersection.size() / union.size();

        // Adjust jaccard similarity based on word density; reduce if overly repetitive
        double densityFactor = (density1 + density2) / 2;
        return jaccard * densityFactor;
    }
}
