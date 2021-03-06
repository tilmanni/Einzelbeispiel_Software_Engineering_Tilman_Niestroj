package com.example.einzelbeispiel_software_engineering_tilman_niestroj;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    Thread Thread1 = null;
    EditText editTextMatriculationNumber;
    TextView serverAnswer, enterText, actualServerAnswer, calculateAnswer, actualCalculateAnswer;
    Button sendButton, calculateButton;
    String matriculationNumber;
    DataOutputStream outToServer;
    BufferedReader inFromServer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextMatriculationNumber = findViewById(R.id.editTextMatriculationNumber);
        serverAnswer = findViewById(R.id.serverAnswer);
        enterText = findViewById(R.id.enterText);
        actualServerAnswer = findViewById(R.id.actualSeverAnswer);
        calculateAnswer = findViewById(R.id.calculateAnswer);
        actualCalculateAnswer = findViewById(R.id.actualCalculateAnswer);
        sendButton = findViewById(R.id.sendButton);
        calculateButton = findViewById(R.id.calculateButton);
        sendButton.setOnClickListener(v -> {
            matriculationNumber = editTextMatriculationNumber.getText().toString().trim();
            Thread1 = new Thread(new Thread1());
            Thread1.start();
        });

        calculateButton.setOnClickListener(v -> {
            matriculationNumber = editTextMatriculationNumber.getText().toString().trim();
            runOnUiThread(() -> actualCalculateAnswer.setText(calculation(matriculationNumber)));
        });
    }

    private static String calculation(String input) {
        StringBuilder output = new StringBuilder();
        char c;
        for (int i = 0; i < input.length(); i++) {
            if (i % 2 == 1) {
                c =  (char) (97 + Short.parseShort(input.charAt(i) + ""));
            } else {
                c = input.charAt(i);
            }
            output.append(c);
        }
        return output.toString();
    }

    class Thread1 implements Runnable {

        @SuppressLint("SetTextI18n")
        @Override
        public void run() {
            Socket socket;
            try {
                socket = new Socket("se2-isys.aau.at", 53212);
                outToServer = new DataOutputStream(socket.getOutputStream());
                inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                outToServer.writeBytes(matriculationNumber +"\n");
                /*String message = "";
                String temp;
                while ((temp= inFromServer.readLine())!=null) { //check null reference
                    if (temp.length() == 0)
                        Thread.sleep(100);
                    else
                        message += temp;
                }*/
                final String fMessage = inFromServer.readLine();
                runOnUiThread(() -> actualServerAnswer.setText(fMessage));
                /*if (message != null) {
                    //runOnUiThread(() -> actualServerAnswer.setText(message));
                } else {
                    runOnUiThread(() -> actualServerAnswer.setText("no reply"));
                }*/

            } catch (IOException e/*| InterruptedException e*/) {
                runOnUiThread(() -> actualServerAnswer.setText("connection failed"));
            }
        }
    }
}