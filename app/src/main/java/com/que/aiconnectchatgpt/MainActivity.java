package com.que.aiconnectchatgpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView welcomeText;
    EditText messageEditText;
    ImageButton sendButton;

    List<Message> messagesList;
    MessageAdapter messageAdapter;


    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();


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

        //setting up recyclerview
        messageAdapter = new MessageAdapter(messagesList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);


        sendButton.setOnClickListener(v -> {
            String question = messageEditText.getText().toString().trim();
            //Toast.makeText(this, question, Toast.LENGTH_SHORT).show();
            addToChat(question, Message.SEND_BY_ME);
            messageEditText.setText("");
            callAPI(question);
            welcomeText.setVisibility(View.GONE);
        });
    }

    void addToChat(String messege, String sentBy) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messagesList.add(new Message(messege, sentBy));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }

    void addResponse(String response){
        messagesList.remove(messagesList.size()-1);
        addToChat(response,Message.SEND_BY_BOT);
    }

    void callAPI(String question) {
        //okHttps
        messagesList.add(new Message("Typing...",Message.SEND_BY_BOT));
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "text-davinci-003");
            jsonBody.put("prompt", question);
            jsonBody.put("max_tokens", 4000);
            jsonBody.put("temperature", 0);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(jsonBody.toString(),JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization","Bearer rte")
                //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
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Faided to load response due to "+e.getMessage());

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getString("text");
                        addResponse(result.trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }else {
                    addResponse("Faided to load response due to "+response.body().toString());
                }

            }
        });
    }
}