package com.socket_programming_tutorial;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketTask extends AsyncTask<JSONObject, Void, JSONObject> {

    // Constants
    private final static String IP_ADDRESS = "XXX.XXX.XXX.XXX";    // Host's IP address in the LAN
    private static final int PORT = 6000;       // HTTP port
    private static final int PACKET_SIZE = 1024;    // standard 1kb packet size

    // Properties
    private InputStreamReader inputStreamReader;
    private Socket socket;
    private JSONObject sendingJSON, receivingJSON;

    /**
     * Constructor for Socket async task
     * @param jsonObject
     * @return
     */
    public SocketTask(JSONObject jsonObject) {
        this.sendingJSON = jsonObject;
        this.receivingJSON = new JSONObject();
    }


    /**
     * Async task in background
     * @param jsonObjects
     * @return: JSON Object that we've received from the server
     */
    @Override
    protected JSONObject doInBackground(JSONObject... jsonObjects) {
        try {
            this.socket = new Socket(IP_ADDRESS, PORT);
            send(this.sendingJSON);
            receive();
            this.socket.close();
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return this.receivingJSON;
    }

    private void send(JSONObject sendObject) {
        String data = sendObject.toString();

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.socket.getOutputStream(), StandardCharsets.UTF_8); // outputStreamWriter creating
            outputStreamWriter.write(data); // Writes the data into  the outputStreamWriter
            outputStreamWriter.flush(); // Sends the data to the server "flushes the water"
            // outputStreamWriter.close();

            Log.d("Result", "Successfully sent");
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

    }

    private void receive() {
        try {
            this.inputStreamReader = new InputStreamReader(this.socket.getInputStream(), StandardCharsets.UTF_8); // inputStreamReader creating
            char[] buffer = new char[PACKET_SIZE];  // Char array in size of PACKET_SIZE (1024 chars = 1KB)

            StringBuilder stringBuilder = new StringBuilder();

            while (this.inputStreamReader.read(buffer) != -1) {
                stringBuilder.append(buffer);
            }

            this.inputStreamReader.close(); // Close the inputStreamReader
            this.receivingJSON = new JSONObject(stringBuilder.toString());

        } catch (JSONException | IOException e) {
            Log.e("Exception", e.toString());
        }
    }
}
