package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Writes simulated patient data
 * This output strategy stores generated health data inside a chosen directory. Each type of health data
 * is written to a seperate text file based on its label. 
 */

public class FileOutputStrategy implements OutputStrategy {

    private String baseDirectory; //renamed BaseDirectory to baseDirectory to follow lowerCamelCase naming and changed this everywhere where this variable is used

    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>(); //renamed file_map to fileMap to follow lowerCamelCase naming and changed this everywhere where this variable is used

    /**
     * Creates a file output strategy using the given base directory.
     * The base directory is the folder where the generated ouput files will be created. If the
     * directory does not already exist, it is created when data is written.
     * 
     * @param baseDirectory the folder where the ouput files dhouold be saved
     */

    public FileOutputStrategy(String baseDirectory) {

        this.baseDirectory = baseDirectory;
    }

    /**
     * Writes one generated patuent data value to a text file.
     * The file name is based on the given label. If this is the first time a label is used, the method
     * creates a file path for that label and stores it in the file map. The method appends the new data to 
     * the file instead of replacing previous data.
     * 
     * @param patientId the unique ID of the patient whose data is being writtem
     * @param timestamp the time when the data was generated
     * @param label the type of health data beoing written, used as the file name
     * @param data he generated health data value
     */

    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the filePath variable
        //renamed FilePath to filePath to follow lowerCamelCase naming and changed this everywhere where this variable is used
        String filePath = fileMap.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}