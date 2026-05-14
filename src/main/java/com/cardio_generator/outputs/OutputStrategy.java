package com.cardio_generator.outputs;

/**
 * Defines a common sturcture for outputting simulated patient data.
 * Classes that implement this interface decide whether the generated data is sent. For example, data can
 * be printed to the console, written to a file, sent thorugh a TCP connection or, treamed through a WebSocket connection. 
 */

public interface OutputStrategy {

    /**
     * Outputs one generated patient data value.
     * 
     * @param patientId the unique ID of the patient whose data is being output
     * @param timestamp the time when the data was generated 
     * @param label the type of health data being output, such as ECG or blood saturation
     * @param data the generated helath data value
     */
    void output(int patientId, long timestamp, String label, String data);
}
