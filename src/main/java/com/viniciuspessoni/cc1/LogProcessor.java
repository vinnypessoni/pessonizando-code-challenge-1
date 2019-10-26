package com.viniciuspessoni.cc1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Vinicius Pessoni (Vinny Pessoni)
 * 26/10/2019
 */
public class LogProcessor {

    String directoryPath;
    String permittedFilesToProcessRegex;
    Map<String, Integer> componentCount;

    public LogProcessor() {
        System.out.println("################ Log Processor Initiated ################");
        this.directoryPath = ".";
        permittedFilesToProcessRegex = ".*(testLog).*";
        parseFile();
    }

    /**
     * Crawls the directory looking for the permitted files to parse
     * @param directoryPath to crawl
     * @return a list of files to parse
     */
    public List<String> crawlDirectory(String directoryPath){

        /* List of files found on the directory that satisfy the permittedFilesToProcessRegex naming restrictions */
        List<String> filesToProcess = null;

        try (Stream<Path> walk = Files.walk(Paths.get(directoryPath))) {
            filesToProcess = walk.map(fileName -> fileName.toString())
                    .filter(fileName -> fileName.matches(permittedFilesToProcessRegex))
                    .collect(Collectors.toList());

            System.out.println("\n\t"+ filesToProcess.size()+ " Files to proccess "+directoryPath+" \n\t"+ filesToProcess+"\n");

        } catch (IOException e) {
            System.err.println("CANNOT CRAWL THE GIVEN PATH "+directoryPath);
            e.printStackTrace();
        }
        return filesToProcess;
    }

    /**
     * Parse the log file and print the most frequent component
     */
    public void parseFile(){
        List<String> filesToProcess = crawlDirectory(directoryPath);

        for(String fileToProcess:filesToProcess) {

            File file = new File(fileToProcess);
            componentCount = new HashMap<>();

            String lineReadFromFile = "";
            String componentName = "";

            try {
                Scanner scFromFile = new Scanner(file);

                while (scFromFile.hasNextLine()) {
                    int tempCount = 1;

                    lineReadFromFile = scFromFile.nextLine();
                    componentName = lineReadFromFile.split("-")[0];

                    if (componentCount.containsKey(componentName)) {
                        tempCount = componentCount.get(componentName) + 1;
                        componentCount.put(componentName, tempCount);
                    } else {
                        componentCount.put(componentName, tempCount);
                    }
                }
                //In case of ambiguity, only the first found element understood as the most frequent will be printed.
                String mostFrequentComponent = Collections.max(
                        componentCount.entrySet(), Comparator.comparingInt(Map.Entry::getValue)
                ).getKey();

                System.out.println("The most frequent component is: " + mostFrequentComponent);
                System.out.println("Processed Map " + componentCount + "\n");

            } catch (FileNotFoundException e) {
                System.err.println("FILE NOT FOUND " + file);
                e.printStackTrace();
            }
        }
    }

}
