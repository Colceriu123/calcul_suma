package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextView result;
    Button button;
    EditText nr1, nr2;
    String recData=null;
    String url="https://legacy.openapi.ro/api/exchange/eur.json?date=2023-05-09";
    String sum=null;
    String resultValue=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = findViewById(R.id.result);
        button = findViewById(R.id.button);
        nr1 = findViewById(R.id.nr1);
        nr2 = findViewById(R.id.nr2);


    }


    public void sum(View view) {
        sum=String.valueOf(Float.parseFloat(nr1.getText().toString()) + Float.parseFloat(nr2.getText().toString()));
        new GetTask().execute();
    }

    public String getValueInRon(String value) {
        Log.d("CONVERSION", "getValueInRon: " + "IN");
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder().url(url).build();

        try {
            Response response = client.newCall(req).execute();
            recData = response.body().string();


        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject conversion = null;
        String conversionRate = null;
        try {
            conversion = new JSONObject(recData);
            conversionRate = conversion.getString("rate");
            Log.d("CONVERSION", "getValueInRon: " + conversionRate);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return String.valueOf(Float.parseFloat(value) * Float.parseFloat(conversionRate));
    }

    private class GetTask extends AsyncTask<String, Void, List<String>> {

        @Override
        protected List<String> doInBackground(String... strings) {

            try {

                resultValue = getValueInRon(sum);
                return new ArrayList<>();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<String> strings) {

            result.setText(resultValue);
        }
    }
}

