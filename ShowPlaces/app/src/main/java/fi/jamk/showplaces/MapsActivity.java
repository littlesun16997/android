package fi.jamk.showplaces;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private JSONArray courses;
    private static ArrayList<String[]> arr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        FetchDataTask task = new FetchDataTask();
        task.execute("http://ptm.fi/materials/golfcourses/golf_courses.json");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLngBounds finland = new LatLngBounds(new LatLng(60.151615, 21.130235),
                new LatLng(65.693792, 31.940780));
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(finland, 1000, 1000, 0));

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(getApplicationContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getApplicationContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getApplicationContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        for (int i = 0; i < arr.size(); i++) {
            double lat = Double.parseDouble(arr.get(i)[1]);
            double lng = Double.parseDouble(arr.get(i)[2]);

            StringBuilder info = new StringBuilder();
            info.append(arr.get(i)[4] + "\n");
            info.append(arr.get(i)[5] + "\n");
            info.append(arr.get(i)[6] + "\n");
            info.append(arr.get(i)[7] + "\n");

            String type = arr.get(i)[0];
            float color;

            if (type.equals("Kulta"))
                color = BitmapDescriptorFactory.HUE_CYAN;
            else if (type.equals("Etu"))
                color = BitmapDescriptorFactory.HUE_GREEN;
            else if (type.equals("Kulta/Etu"))
                color = BitmapDescriptorFactory.HUE_ORANGE;
            else
                color = BitmapDescriptorFactory.HUE_VIOLET;

            LatLng location = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions().position(location)
                    .title(arr.get(i)[3]).snippet(info.toString())
                    .icon(BitmapDescriptorFactory.defaultMarker(color)));

        }
    }

    class FetchDataTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;
            JSONObject json = null;
            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                json = new JSONObject(stringBuilder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) urlConnection.disconnect();
            }
            return json;
        }

        protected void onPostExecute(JSONObject json) {
            try {
                courses = json.getJSONArray("courses");
                for (int i = 0; i < courses.length(); i++) {
                    JSONObject course = courses.getJSONObject(i);
                    Iterator<String> keyList = course.keys();
                    ArrayList<String> values = new ArrayList<>();

                    while (keyList.hasNext()) {
                        String key = keyList.next();
                        values.add(course.getString(key));
                    }
                    String[] valArr = new String[values.size()];
                    arr.add(values.toArray(valArr));
                }
            } catch (JSONException e) {
                Log.e("JSON", "Error getting data.");
            }

        }
    }

}
