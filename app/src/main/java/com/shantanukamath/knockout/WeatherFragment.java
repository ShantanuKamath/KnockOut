package com.shantanukamath.knockout;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeatherFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    TextView country, temp, weather, description, helloUser, psiData;
    ImageView wIcon;
    String[] PSI ;
    String JsonStr = null;

    public WeatherFragment() {
        // Required empty public constructor
    }

    public static WeatherFragment newInstance(String param1, String param2) {
        WeatherFragment fragment = new WeatherFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        helloUser = (TextView) view.findViewById(R.id.helloUser);
        country = (TextView) view.findViewById(R.id.country);
        temp = (TextView) view.findViewById(R.id.temp);
        weather = (TextView) view.findViewById(R.id.weather);
        description = (TextView) view.findViewById(R.id.weatherDesc);
        wIcon = (ImageView) view.findViewById(R.id.weatherIcon);
        TextView day1 = (TextView) view.findViewById(R.id.day1);
        TextView day2 = (TextView) view.findViewById(R.id.day2);
        TextView day3 = (TextView) view.findViewById(R.id.day3);
        TextView day4 = (TextView) view.findViewById(R.id.day4);
        TextView day5 = (TextView) view.findViewById(R.id.day5);
        TextView dateToday = (TextView) view.findViewById(R.id.date);
        psiData = (TextView) view.findViewById(R.id.psiValue);


        helloUser.setText("Hello, " + ParseUser.getCurrentUser().getString("name"));
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMMM, ''yy");
        Date d = calendar.getTime();
        String day = sdf.format(d);

        dateToday.setText(day);
        sdf = new SimpleDateFormat("EEE");


        calendar.add(Calendar.DAY_OF_YEAR, 1);
        d = calendar.getTime();
        day = sdf.format(d);
        day1.setText(day);

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        d = calendar.getTime();
        day = sdf.format(d);
        day2.setText(day);

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        d = calendar.getTime();
        day = sdf.format(d);
        day3.setText(day);

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        d = calendar.getTime();
        day = sdf.format(d);
        day4.setText(day);

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        d = calendar.getTime();
        day = sdf.format(d);
        day5.setText(day);

        new OpenWeatherMapTask("Singapore", country, temp, weather, description, wIcon).execute();
        FetchLocationTask flt = new FetchLocationTask();
        flt.execute();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    private class OpenWeatherMapTask extends AsyncTask<Void, Void, String> {

        String cityName;
        TextView country, temp, weather, description;
        ImageView wIcon;
        String dummyAppid = "802118f9c5ed27951804b4f5fe5b584d";
        String queryWeather = "http://api.openweathermap.org/data/2.5/weather?q=";
        String queryDummyKey = "&appid=" + dummyAppid;

        OpenWeatherMapTask(String cityName, TextView country, TextView temp, TextView weather, TextView description, ImageView wIcon) {
            this.cityName = cityName;
            this.country = country;
            this.temp = temp;
            this.weather = weather;
            this.description = description;
            this.wIcon = wIcon;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            String queryReturn;

            String query = null;
            try {
                //web link
                query = queryWeather + URLEncoder.encode(cityName, "UTF-8") + queryDummyKey;
                //json text
                queryReturn = sendQuery(query);
                result += ParseJSON(queryReturn);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                queryReturn = e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                queryReturn = e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            String weatherIcons[] = {"sky is clear", "few clouds", "scattered clouds", "broken clouds", "shower rain", "Rain", "Thunderstorm", "snow", "mist"};
            int wIcons[] = {R.drawable.weather_1, R.drawable.weather_2, R.drawable.weather_4, R.drawable.weather_3, R.drawable.weather_5, R.drawable.weather_6, R.drawable.weather_7, R.drawable.weather_8, R.drawable.weather_9};
            if (s.length() > 0) {
                int start = 0;
                int end = s.indexOf("\n");
                country.setText(s.substring(start, end));
                start = end + 1;
                end = s.indexOf("\n", start);
                Double toBeTruncated = Double.parseDouble(s.substring(start, end)) - 273;
                Double truncatedDouble = new BigDecimal(toBeTruncated)
                        .setScale(3, BigDecimal.ROUND_HALF_UP)
                        .doubleValue();
                temp.setText("" + truncatedDouble + "°C");
                start = end + 1;
                end = s.indexOf("\n", start);
                weather.setText(s.substring(start, end));
                start = end + 1;
                end = s.indexOf("\n", start);
                description.setText(s.substring(start, end));
                start = end + 1;
                end = s.indexOf("\n", start);
                Log.v("ICON", description.getText().toString());
                for (int i = 0; i < weatherIcons.length; i++) {
                    if (description.getText().toString().equalsIgnoreCase(weatherIcons[i])) {
                        wIcon.setImageResource(wIcons[i]);
                    }
                }

                if(weather.getText().toString().equals("Rain") || weather.getText().toString().equals("Thunderstorm") || weather.getText().toString().equals("Drizzle"))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Looks like it might rain today");

                        builder.setMessage("I think its time to replace outdoor activities with indoor ones!");
                        builder.setPositiveButton("Okay!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                            }
                        });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        }

        private String sendQuery(String query) throws IOException {
            String result = "";

            URL searchURL = new URL(query);

            HttpURLConnection httpURLConnection = (HttpURLConnection) searchURL.openConnection();
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(
                        inputStreamReader,
                        8192);

                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }

                bufferedReader.close();
            }

            return result;
        }

        private String ParseJSON(String json) {
            String jsonResult = "";

            try {
                JSONObject JsonObject = new JSONObject(json);
                String cod = jsonHelperGetString(JsonObject, "cod");

                if (cod != null) {
                    if (cod.equals("200")) {

                        jsonResult += jsonHelperGetString(JsonObject, "name") + ", ";
                        JSONObject sys = jsonHelperGetJSONObject(JsonObject, "sys");
                        if (sys != null) {
                            jsonResult += jsonHelperGetString(sys, "country");
                        }
                        jsonResult += "\n";

                        JSONObject main = jsonHelperGetJSONObject(JsonObject, "main");
                        if (main != null) {
                            jsonResult += jsonHelperGetString(main, "temp") + "\n";
                        }

                        JSONArray weather = jsonHelperGetJSONArray(JsonObject, "weather");
                        if (weather != null) {
                            for (int i = 0; i < weather.length(); i++) {
                                JSONObject thisWeather = weather.getJSONObject(i);
                                jsonResult += jsonHelperGetString(thisWeather, "main") + "\n";
                                jsonResult += jsonHelperGetString(thisWeather, "description") + "\n";
                                jsonResult += jsonHelperGetString(thisWeather, "icon") + "\n";
                            }
                        }
                        //...incompleted

                    } else if (cod.equals("404")) {
                        String message = jsonHelperGetString(JsonObject, "message");
                        jsonResult += "cod 404: " + message;
                    }
                } else {
                    jsonResult += "cod == null\n";
                }

            } catch (JSONException e) {
                e.printStackTrace();
                jsonResult += e.getMessage();
            }

            return jsonResult;
        }

        private String jsonHelperGetString(JSONObject obj, String k) {
            String v = null;
            try {
                v = obj.getString(k);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return v;
        }

        private JSONObject jsonHelperGetJSONObject(JSONObject obj, String k) {
            JSONObject o = null;

            try {
                o = obj.getJSONObject(k);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return o;
        }

        private JSONArray jsonHelperGetJSONArray(JSONObject obj, String k) {
            JSONArray a = null;

            try {
                a = obj.getJSONArray(k);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return a;
        }
    }


//    class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
//        ImageView bmImage;
//
//        public ImageDownloader(ImageView bmImage) {
//            this.bmImage = bmImage;
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String url = urls[0];
//            Bitmap mIcon = null;
//            try {
//                InputStream in = new java.net.URL(url).openStream();
//                mIcon = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//            }
//            return mIcon;
//        }
//
//
//        protected void onPostExecute(Bitmap result) {
//            bmImage.setImageBitmap(result);
//        }
//    }

    public class FetchLocationTask extends AsyncTask<Void, Void, ArrayList<String[]>> {

        @Override
        protected ArrayList<String[]> doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                final String BASE_ADDR = "http://sghaze.herokuapp.com";
                Uri builtUri = Uri.parse(BASE_ADDR).buildUpon().build();
                Log.d("REST", builtUri.toString());
                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                JsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e("FRIENDS", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("FRIENDS", "Error closing stream", e);
                    }
                }
            }
            try {
                return getLocationDataFromJson(JsonStr);
            } catch (JSONException e) {
                Log.e("FRIENDS", e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }
        @Override
        protected void onPostExecute(ArrayList<String[]> result) {
            if (result != null) {
                psiData.setText(PSI[0]);
            }
        }
    }
    private ArrayList<String[]> getLocationDataFromJson(String JsonStr)
            throws JSONException {
        ArrayList<String[]> result = new ArrayList<>();
        JSONObject JsonObj = new JSONObject(JsonStr);
        PSI = new String[1];
        PSI[0]=JsonObj.getString("North");
        result.add(PSI);
        return result;
    }

}