package com.que.aiconnectchatgpt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView welcomeText;
    EditText messageEditText;
    ImageButton sendButton;

    List<Message> messagesList;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messagesList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerVIew);
        welcomeText = findViewById(R.id.welcometextView);
        messageEditText = findViewById(R.id.messegeEditText);
        sendButton = findViewById(R.id.sendBt);


        sendButton.setOnClickListener(v ->{
            String question = messageEditText.getText().toString().trim();
            //Toast.makeText(this, question, Toast.LENGTH_SHORT).show();
        });

    }
}