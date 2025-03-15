package invertedIndex;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author ehab
 */
public class Test {

    public static void main(String args[]) throws IOException {
        Index5 index = new Index5();
        // Set the path to the collection directory
        String files = System.getProperty("user.dir") + "\\rl\\collection\\";

        File file = new File(files);
        // Check if the collection directory exists
        if (!file.exists() || !file.isDirectory()) {
            System.out.println("Collection directory not found: " + files);
            return;
        }

        // Get the list of files in the collection directory
        String[] fileList = file.list();
        if (fileList == null || fileList.length == 0) {
            System.out.println("No files found in the collection directory.");
            return;
        }

        // Sort the file list and update the index size
        fileList = index.sort(fileList);
        index.N = fileList.length;

        // Build the full file paths
        for (int i = 0; i < fileList.length; i++) {
            fileList[i] = files + fileList[i];
        }

        // Build the inverted index
        index.buildIndex(fileList);

        // Store the index to a file (for persistence)
        index.store("index");

        // Print the dictionary (for debugging)
        index.printDictionary();

        // Test search with a predefined phrase
        String test3 = "data should plain greatest comif"; // Example search phrase
        System.out.println("Boolean Model result = \n" + index.find_24_01(test3));

        // Interactive search loop
        String phrase = "";
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        do {
            System.out.println("Print search phrase: ");
            phrase = in.readLine();

            // search the non-empty phrase
            if (!phrase.isEmpty()) {
                String searchResults = index.find_24_01(phrase);
                if (searchResults.isEmpty()) {
                    System.out.println("No results found for the phrase: " + phrase);
                } else {
                    System.out.println("Search results for \"" + phrase + "\":");
                    System.out.println(searchResults);
                }
            }
        } while (!phrase.isEmpty()); // Continue until an empty phrase is entered

        System.out.println("Exiting...");
    }
}