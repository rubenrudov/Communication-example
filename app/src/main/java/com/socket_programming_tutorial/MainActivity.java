package com.socket_programming_tutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    EditText dataEditText;
    Button submitButton;
    TextView textViewReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Elements setting:
        dataEditText = findViewById(R.id.dataEditText);
        submitButton = findViewById(R.id.submitButton);
        textViewReceived = findViewById(R.id.textViewReceived);
        // Button listener setting:
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSending();
            }
        });
    }

    private void dataSending() {
        // Here comes the dataSending function

        JSONObject sendingData = new JSONObject();

        try {
            sendingData.put("request", this.dataEditText.getText().toString());
            SocketTask dataTransportTask = new SocketTask(sendingData);
            JSONObject received = dataTransportTask.execute().get();

            textViewReceived.setText(received.get("response").toString());
        } catch (JSONException | InterruptedException | ExecutionException e) {
            Log.e("Exception", e.toString());
        }
    }
}
