import java.util.Arrays;

public class ContentDensityChecker {
    public static boolean hasLowDensity(String text) {
        String[] words = text.split("\\s+");
        long uniqueWords = Arrays.stream(words).distinct().count();
        double densityRatio = (double) uniqueWords / words.length;

        return densityRatio < 0.2;  // Low density threshold for repetitive text
    }
}
