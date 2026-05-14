package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Defines a common structure for generating simulated patient health data.
 * Classes the implement this interface generate a specific type of patient data, such as
 * ECG data, blood pressure data, blood saturation data, or blood level data. The generated data is 
 * sent to the provided output strategy.
 */

public interface PatientDataGenerator {

    /**
     * Genrates simulated health data for one patient and sends it to the choosen output.
     * 
     * 
     * @param patientId the unique ID of the patient whose data should be generated
     * @param outputStrategy the output strategy used to send or store the generated data
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
