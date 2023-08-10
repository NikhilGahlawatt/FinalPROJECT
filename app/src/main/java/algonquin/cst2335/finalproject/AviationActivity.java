package algonquin.cst2335.finalproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.Adapters.FlightAdapter;
import algonquin.cst2335.finalproject.FlightData.FlightDatabase;
import algonquin.cst2335.finalproject.FlightData.FlightDetails;

/**
 * This is the main activity for the Aviation module of the application.
 * It provides features like searching for flights by airport code and saving favorite flights.
 */
public class AviationActivity extends AppCompatActivity {

    /**
     * RecyclerView instance that displays the flight details.
     */
    private RecyclerView recyclerView;

    /**
     * List that stores the details of flights fetched from API or saved in the database.
     */
    private List<FlightDetails> flights = new ArrayList<>();

    /**
     * Boolean flag to determine if the list shown is the saved list or the search results.
     */
    private boolean isSavedList;

    /**
     * Instance of the flight database to perform CRUD operations.
     */
    private FlightDatabase flightDatabase;

    /**
     * Adapter to display the search results of the flights.
     */
    private FlightAdapter searchAdapter;

    /**
     * Adapter to display the saved flights.
     */
    private FlightAdapter savedAdapter;

    /**
     * Shared Preferences file name.
     */
    private static final String PREFS_NAME = "AviationActivityPrefs";

    /**
     * Key to store the airport code in Shared Preferences.
     */
    private static final String KEY_AIRPORT_CODE = "AirportCode";

    /**
     * EditText field to input the airport code.
     */
    private EditText airportCodetext;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_aviation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // If the help menu item is clicked
        if (id == R.id.help_item) {
            showHelpDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * This method shows a help dialog when called.
     */
    private void showHelpDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.help_dialog_title)
                .setMessage(R.string.help_dialog_message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    /**
     * Method invoked when the activity is created.
     * It initializes the UI components, adapters, and button click listeners.
     * @param savedInstanceState State information saved from a previous execution.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviation);




        flightDatabase = FlightDatabase.getInstance(this);

        recyclerView = findViewById(R.id.aviationRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchAdapter = new FlightAdapter(flights, flightDatabase,false); // Pass the FlightDatabase instance
        recyclerView.setAdapter(searchAdapter);

        Button fetchFlightsButton = findViewById(R.id.SearchButton);

        airportCodetext = findViewById(R.id.textInput);
        String airportCode = airportCodetext.getText().toString();

        // Load the saved airport code from SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedAirportCode = prefs.getString(KEY_AIRPORT_CODE, "YOW");
        airportCodetext.setText(savedAirportCode);

        // Add a TextWatcher to the EditText
        airportCodetext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Save the airport code to SharedPreferences whenever it changes
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(KEY_AIRPORT_CODE, s.toString());
                editor.apply();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });


        fetchFlightsButton.setOnClickListener(v -> {
            // Get the airport code from the EditText

            Log.d("request-airportCode", airportCode);
            if (!airportCode.isEmpty()) {
                getFlightsFromApi(airportCode);

            } else {
                getFlightsFromApi("YOW");
            }
        });

        Button ShowSavedButton = findViewById(R.id.SavedButton);
        ShowSavedButton.setOnClickListener(v -> loadSavedFlights());
    }
    /**
     * This method loads the saved flights from the database and updates the UI.
     */
    private void loadSavedFlights() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // Get the saved flights from the database
                List<FlightDetails> savedFlights = flightDatabase.flightDao().getAllFlights();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // Create a new instance of FlightAdapter with the saved flights
                        savedAdapter = new FlightAdapter(savedFlights, flightDatabase,true);

                        // Set the new adapter to the RecyclerView
                        recyclerView.setAdapter(savedAdapter);


                    }
                });
            }
        });
    }

    /**
     * Method to make a request to the aviation API, parse the response,
     * and update the UI with the fetched flight details.
     * @param airportCode Code of the airport to fetch flights from.
     */
    public void getFlightsFromApi(String airportCode) {
        String url = "http://api.aviationstack.com/v1/flights?access_key=0b0afbc49e547546441d1809e82ea0a6&dep_iata=" + airportCode;

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String req = "Sent request";
        Log.d("API", req);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("API Response", response);
                        // Parse the response
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray flightsArray = jsonObject.getJSONArray("data");
                            flights.clear();

                            for (int i = 0; i < flightsArray.length(); i++) {
                                JSONObject flightObject = flightsArray.getJSONObject(i);

                                // Create a new FlightDetails object and fill it with data
                                FlightDetails flight = new FlightDetails();

                                // Extract flight details and fill into FlightDetails object
                                if (flightObject.has("flight")) {
                                    JSONObject flightDetailObject = flightObject.getJSONObject("flight");
                                    flight.setFlightNumber(flightDetailObject.getString("iata"));
                                }

                                if (flightObject.has("arrival")) {
                                    JSONObject arrivalObject = flightObject.getJSONObject("arrival");
                                    flight.setArrivalAirport(arrivalObject.getString("airport"));
                                    flight.setTerminal(arrivalObject.getString("terminal"));
                                    flight.setGate(arrivalObject.getString("gate"));
                                    flight.setDelay(arrivalObject.getString("delay"));
                                }

                                // Add the flight to your list
                                flights.add(flight);
                            }

                            // After parsing the response and updating the flights list:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Update the search adapter and set it to the RecyclerView
                                    searchAdapter.updateFlights(flights);
                                    recyclerView.setAdapter(searchAdapter);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String responseBody;
                Log.e("API Error", error.toString());
                try {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        responseBody = new String(error.networkResponse.data, "utf-8");
                        JSONObject jsonObject = new JSONObject(responseBody);
                        Log.e("API Error", jsonObject.toString());
                    }
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        // Set a longer timeout duration (in milliseconds)
        int timeoutDuration = 10000; // 10 seconds
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                timeoutDuration,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(stringRequest);
    }
}
