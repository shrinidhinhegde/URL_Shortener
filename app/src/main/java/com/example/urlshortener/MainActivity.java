package com.example.urlshortener;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1000;
    private Button submit,copy;
    private TextView resultText;
    private EditText submitText;
    private Button download;
    private String data;
    private String url;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialised id
        submit = findViewById(R.id.submit_button);
        copy = findViewById(R.id.copy_button);
        submitText = findViewById(R.id.submit_text);
        resultText = findViewById(R.id.result);
        download = findViewById(R.id.download);
        imageView = findViewById(R.id.qr_code);


        //get data from user
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = submitText.getText().toString();
                url = data;
                data = "url="+data;
                System.out.println(data);
                submit.setVisibility(View.GONE);
                submitText.setVisibility(View.GONE);
                try {
                    task(data, url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void task(String d, String u) throws IOException {
        OkHttpClient client = new OkHttpClient();
        OkHttpClient client1 = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, d);
        Request request = new Request.Builder()
                .url("https://url-shortener-service.p.rapidapi.com/shorten")
                .post(body)
                .addHeader("x-rapidapi-host", "url-shortener-service.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "21e213400cmsh5e9e175e32cec4bp13ad1bjsne84f91c9ccf2")
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        Request request1 = new Request.Builder()
                .url("https://pierre2106j-qrcode.p.rapidapi.com/api?backcolor=ffffff&pixel=1%20to%2010&ecl=L%20%257C%20M%257C%20Q%20%257C%20H&forecolor=000000&type=text%20%257C%20url%20%257C%20tel%20%257C%20sms%20%257C%20email&text="+u)
                .get()
                .addHeader("x-rapidapi-host", "pierre2106j-qrcode.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "21e213400cmsh5e9e175e32cec4bp13ad1bjsne84f91c9ccf2")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String m = response.body().string();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultText.setText(m.substring(15,m.length()-2));
                        resultText.setVisibility(View.VISIBLE);
                        copy.setVisibility(View.VISIBLE);
                        copy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("label",resultText.getText().toString());
                                clipboard.setPrimaryClip(clip);
                                Toast.makeText(MainActivity.this, "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        client1.newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String n = response.body().string();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(n);
                    }
                });
            }
        });
    }
}
