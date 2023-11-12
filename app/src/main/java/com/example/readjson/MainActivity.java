package com.example.readjson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lvUsers;
    ArrayList<String> arrUsers;
    ArrayAdapter<String> adapterUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvUsers = (ListView) findViewById(R.id.lvUsers);
        arrUsers = new ArrayList<>();

        adapterUsers = new ArrayAdapter<>(this, R.layout.layout_listview, arrUsers);
        lvUsers.setAdapter(adapterUsers);

        new ReadJSON().execute("https://jsonplaceholder.typicode.com/users");
    }

    private class ReadJSON extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                while ((line = bufferedReader.readLine()) != null){
                    content.append(line);
                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray arr = new JSONArray(s);
                for(int i = 0; i < arr.length(); i++){
                    JSONObject object = arr.getJSONObject(i);
                    String name = object.getString("name");
                    String email = object.getString("email");
                    String website = object.getString("website");
                    arrUsers.add("Tên: " + name + "\nEmail: " + email + "\nWebsite: " + website);
                }
                adapterUsers.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}