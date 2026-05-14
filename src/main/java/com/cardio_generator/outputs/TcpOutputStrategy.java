package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * Sends simulated patient data through a TCP socket connection.
 * This output strategy starts a TCP sevrer on the given port and waits for a client
 * to connect. Once a client is connected, generated patient data is sent to the client 
 * as text seperated with commas.
 */

public class TcpOutputStrategy implements OutputStrategy {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;

    /**
     * Creates a TCP output strategy and starts a TCP server on the given part.
     * The constructor opens a server socket and waits for one cclient connection in a seperate
     * thread. This prevents the main simulator from being blocked while waiting for a client to connect.
     * 
     * @param port the TCP port number on which the server should listen for clients
     */

    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends one genrated patient data value to the connected TCP client
     * The data is formatted as comma-seperated text containing the patient ID, timestamp, label, and data value.
     * If no client is connected yet , the method does not send anyhting.
     * 
     * @param patientId the unique ID of the patient whose data is being sent
     * @param timestamp the time when the data was generated
     * @param label the type of health data being sent, such as ECG or blood pressure
     * @param data the generated health data value
     */

    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }
}
