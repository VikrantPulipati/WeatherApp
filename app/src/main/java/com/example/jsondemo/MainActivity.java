package com.example.jsondemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ConstraintLayout layout;

    EditText enterLat;
    EditText enterLong;
    Button getWeather;

    Map<String, Integer> iconID;

    TextView name1, name2, name3,
            temp1, temp2, temp3,
            time1, time2, time3,
            date1, date2, date3,
            weather1, weather2, weather3;

    ImageView icon1, icon2, icon3;

    public final String API_KEY = "bf819331189b05586f4e8601943ea89d";

    double lat;
    double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.id_MasterLayout);

        enterLat = findViewById(R.id.id_lat);
        enterLong = findViewById(R.id.id_long);
        getWeather = findViewById(R.id.id_getWeather);
        getWeather.setOnClickListener(this);

        name1 = findViewById(R.id.id_name1);
        name2 = findViewById(R.id.id_name2);
        name3 = findViewById(R.id.id_name3);

        temp1 = findViewById(R.id.id_temp1);
        temp2 = findViewById(R.id.id_temp2);
        temp3 = findViewById(R.id.id_temp3);

        icon1 = findViewById(R.id.id_icon1);
        icon2 = findViewById(R.id.id_icon2);
        icon3 = findViewById(R.id.id_icon3);

        time1 = findViewById(R.id.id_time1);
        time2 = findViewById(R.id.id_time2);
        time3 = findViewById(R.id.id_time3);

        date1 = findViewById(R.id.id_date1);
        date2 = findViewById(R.id.id_date2);
        date3 = findViewById(R.id.id_date3);

        weather1 = findViewById(R.id.id_weather1);
        weather2 = findViewById(R.id.id_weather2);
        weather3 = findViewById(R.id.id_weather3);

        iconID = new HashMap<>();
        iconID.put("01d", R.drawable.ic_01d);
        iconID.put("01n", R.drawable.ic_01n);
        iconID.put("02d", R.drawable.ic_02d);
        iconID.put("02n", R.drawable.ic_02n);
        iconID.put("03d", R.drawable.ic_03d);
        iconID.put("03n", R.drawable.ic_03n);
        iconID.put("04d", R.drawable.ic_04d);
        iconID.put("04n", R.drawable.ic_04n);
        iconID.put("09d", R.drawable.ic_09n);
        iconID.put("09n", R.drawable.ic_09n);
        iconID.put("10d", R.drawable.ic_10d);
        iconID.put("10n", R.drawable.ic_10n);
        iconID.put("11d", R.drawable.ic_11d);
        iconID.put("11n", R.drawable.ic_11n);
        iconID.put("13d", R.drawable.ic_13d);
        iconID.put("13n", R.drawable.ic_13n);
        iconID.put("50d", R.drawable.ic_50d);
        iconID.put("50n", R.drawable.ic_50n);

    }

    @Override
    public void onClick(View v) {
        if (enterLat.getText().toString().length() > 0 && enterLong.getText().toString().length() > 0) {
            lat = Double.parseDouble(enterLat.getText().toString());
            lon = Double.parseDouble(enterLong.getText().toString());
            String url = "http://api.openweathermap.org/data/2.5/find?lat=" + lat + "&lon=" + lon + "&cnt=3&units=imperial&appid=" + API_KEY;
            WeatherTask task = new WeatherTask();
            task.execute(url);

        }

    }

    public static String capitalize(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuilder sb = new StringBuilder();

        for (String s : arr) {
            sb.append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    public class WeatherTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params){
            String stringUrl = params[0];
            String result = "";
            String inputLine;

            try {
                URL url = new URL(stringUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);

                connection.connect();

                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());

                BufferedReader reader = new BufferedReader(streamReader);

                while ((inputLine = reader.readLine()) != null) {
                    Log.d("ERROR1", result);
                    result = result.concat(inputLine);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d("ERROR1", result);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("ERROR2", result);
                result = null;
            }

            //Log.d("ERROR1", result);

            JSONObject output = null;
            try {
                if (result != null) {
                    output = new JSONObject(result);
                } else {
                    output = null;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return output;
        }

        @Override
        protected void onPostExecute(JSONObject s) {
            super.onPostExecute(s);
            doEverything(s);
        }
    }

    public void doEverything(JSONObject s) {
        if (s != null) {
            JSONArray arr;
            JSONObject city1 = new JSONObject();
            JSONObject city2 = new JSONObject();
            JSONObject city3 = new JSONObject();
            try {
                arr = s.getJSONArray("list");
                city1 = arr.getJSONObject(0);
                city2 = arr.getJSONObject(1);
                city3 = arr.getJSONObject(2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                //SET NAME
                name1.setText(city1.getString("name"));
                name2.setText(city2.getString("name"));
                name3.setText(city3.getString("name"));

                //SET TEMPERATURE
                String tmp1 = city1.getJSONObject("main").getString("temp") + "\u00B0F";
                String tmp2 = city2.getJSONObject("main").getString("temp") + "\u00B0F";
                String tmp3 = city3.getJSONObject("main").getString("temp") + "\u00B0F";
                temp1.setText(tmp1);
                temp2.setText(tmp2);
                temp3.setText(tmp3);

                //SET ICONS
                icon1.setImageResource(iconID.get(city1.getJSONArray("weather").getJSONObject(0).getString("icon")));
                icon2.setImageResource(iconID.get(city2.getJSONArray("weather").getJSONObject(0).getString("icon")));
                icon3.setImageResource(iconID.get(city3.getJSONArray("weather").getJSONObject(0).getString("icon")));

                //SET TIME
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
                timeFormat.setTimeZone(TimeZone.getTimeZone("EST"));
                String t1 = timeFormat.format(new Date(city1.getInt("dt") * 1000l));
                if (t1.charAt(0) == '0') {
                    t1 = t1.substring(1);
                }
                String t2 = timeFormat.format(new Date(city2.getInt("dt") * 1000l));
                if (t2.charAt(0) == '0') {
                    t2 = t2.substring(1);
                }
                String t3 = timeFormat.format(new Date(city3.getInt("dt") * 1000l));
                if (t3.charAt(0) == '0') {
                    t3 = t3.substring(1);
                }
                time1.setText(t1 + " EST");
                time2.setText(t2 + " EST");
                time3.setText(t3 + " EST");

                //SET DATE
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                date1.setText(dateFormat.format(new Date(city1.getInt("dt") * 1000l)));
                date2.setText(dateFormat.format(new Date(city2.getInt("dt") * 1000l)));
                date3.setText(dateFormat.format(new Date(city3.getInt("dt") * 1000l)));

                //SET WEATHER
                weather1.setText(capitalize(city1.getJSONArray("weather").getJSONObject(0).getString("description")));
                weather2.setText(capitalize(city2.getJSONArray("weather").getJSONObject(0).getString("description")));
                weather3.setText(capitalize(city3.getJSONArray("weather").getJSONObject(0).getString("description")));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast toast = Toast.makeText(this, "Invalid Coordinates", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


}