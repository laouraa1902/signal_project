package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates simulated alert data for patients.
 * This class keeps track of whether each patient currently has an active alert. If a patient
 * already has an active alert, the generator bmay resolve it. If a patient does not have an 
 * active alert, the generator may trigger a new alert.
 */

public class AlertGenerator implements PatientDataGenerator {

    public static final Random randomGenerator = new Random();
    private boolean[] AlertStates; // false = resolved, true = pressed

    /**
     * Creates an alert generator for a given number of patients.
     * The alert state array stores whether each patient currently has an active alert. Patient IDs 
     * are expected to start at 1, so the array size is patientCount +1.
     * 
     * @param patientCount the number of patients for whom alert data will be generated
     */

    public AlertGenerator(int patientCount) {
        AlertStates = new boolean[patientCount + 1];
    }

    /**
     * Generates an alert update for one patient
     * If the patient currently has an active alert, there is a cahnce that the alert will 
     * be resolved. If the patient does not have an active alert, there is a chance that a new 
     * alert will be triggered. Any generated alert update is sent to the provided output strategy.
     * 
     * @param patientId the unique ID of the patient whose alert state is generated
     * @param outputStrategy the output strategy used to send or store the alert update
     */

    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (AlertStates[patientId]) {
                if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
                    AlertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                double Lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                double p = -Math.expm1(-Lambda); // Probability of at least one alert in the period
                boolean alertTriggered = randomGenerator.nextDouble() < p;

                if (alertTriggered) {
                    AlertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
