import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class PlagiarismDetector {
    private static final List<String> SEARCH_URLS = Arrays.asList(
            "https://www.google.com/search?q=",
            "https://www.bing.com/search?q="
    );

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter text to check for plagiarism:");
        String inputText = scanner.nextLine();

        // Filter out input with low content density
        if (ContentDensityChecker.hasLowDensity(inputText)) {
            System.out.println("Input has low content density. Skipping detailed analysis.");
            return;
        }

        List<String> webResults = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(SEARCH_URLS.size());

        // Fetch content from each search URL in parallel
        for (String searchUrl : SEARCH_URLS) {
            executor.submit(() -> {
                try {
                    String result = fetchWebContent(searchUrl + inputText.replace(" ", "+"));
                    synchronized (webResults) {
                        webResults.add(result);
                    }
                } catch (IOException e) {
                    System.err.println("Error fetching content from " + searchUrl + ": " + e.getMessage());
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            System.err.println("Error waiting for threads to finish: " + e.getMessage());
        }

        // Calculate and display similarity results for each web content fetched
        for (String webContent : webResults) {
            System.out.println("=== Similarity for one fetched content ===");

            // Perform semantic hashing comparison
            double semanticHashSimilarity = SemanticHashing.calculate(inputText, webContent);
            System.out.printf("Semantic Hash Similarity: %.2f%%\n", semanticHashSimilarity * 100);

            // Perform adaptive Jaccard similarity
            double adaptiveJaccardSimilarity = AdaptiveJaccardSimilarity.calculate(inputText, webContent);
            System.out.printf("Adaptive Jaccard Similarity: %.2f%%\n", adaptiveJaccardSimilarity * 100);

            // Sliding Window Similarity
            double slidingWindowSimilarity = SlidingWindowSimilarity.calculate(inputText, webContent);
            System.out.printf("Sliding Window Similarity: %.2f%%\n", slidingWindowSimilarity * 100);
        }
    }

    private static String fetchWebContent(String url) throws IOException {
        return Jsoup.connect(url).get().body().text();
    }
}
