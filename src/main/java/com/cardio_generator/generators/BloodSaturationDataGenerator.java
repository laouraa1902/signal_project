package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates simulated blood oxygen saturation data for patients
 * This class creates saturation values as percentages. Each patient starts with a baseline value 
 * betweem 95% and 100%. During each generation step, the value changes slightly to simulate natural flactuations,
 * while staying between 90% and 100%
 */

public class BloodSaturationDataGenerator implements PatientDataGenerator {
    private static final Random random = new Random();
    private int[] lastSaturationValues;

    /**
     * Creates a blood saturation data generator for a given number of patients.
     * The arary stores the most recent saturation value for each patient. Patient IDs arew
     * expected to start at 1, so the array size is patientCount + 1.
     * 
     * @param patientCount the number of patients for whom saturation data will be generated
     */

    public BloodSaturationDataGenerator(int patientCount) {
        lastSaturationValues = new int[patientCount + 1];

        // Initialize with baseline saturation values for each patient
        for (int i = 1; i <= patientCount; i++) {
            lastSaturationValues[i] = 95 + random.nextInt(6); // Initializes with a value between 95 and 100
        }
    }

    /**
     * Generates a new simulated blood saturation value for one patient.
     * The method changees the patient's previous saturation value by -1, 0, or 1 to simulate small natural
     * changes. The value is then limited to the range 90% to 100%. The generated value is sent to the provided output strategy.
     * 
     * @param patiendId the unique ID of the patient whose saturation data is generated
     * @param outputStrategy the output startegy used to send or store the generated value
     */

    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // Simulate blood saturation values
            int variation = random.nextInt(3) - 1; // -1, 0, or 1 to simulate small fluctuations
            int newSaturationValue = lastSaturationValues[patientId] + variation;

            // Ensure the saturation stays within a realistic and healthy range
            newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);
            lastSaturationValues[patientId] = newSaturationValue;
            outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation",
                    Double.toString(newSaturationValue) + "%");
        } catch (Exception e) {
            System.err.println("An error occurred while generating blood saturation data for patient " + patientId);
            e.printStackTrace(); // This will print the stack trace to help identify where the error occurred.
        }
    }
}
