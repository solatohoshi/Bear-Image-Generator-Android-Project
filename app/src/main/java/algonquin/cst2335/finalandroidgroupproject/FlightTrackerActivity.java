package algonquin.cst2335.finalandroidgroupproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalandroidgroupproject.data.FetchDataCallback;
import algonquin.cst2335.finalandroidgroupproject.data.FlightTrackerViewModel;
import algonquin.cst2335.finalandroidgroupproject.databinding.ActivityFlightTrackerBinding;

/**
 * Represents the main activity for tracking flights. This activity allows users
 * to search for active flights by airport code and view detailed information about them.
 *
 * @author Qiaoqing Wu
 * @version 1.0
 */
public class FlightTrackerActivity extends AppCompatActivity {

    private ActivityFlightTrackerBinding binding;
    private FlightTrackerViewModel flightViewModel;
    private RequestQueue queue;
    private String departAirportCode;
    protected FlightAdapter myAdapter;
    private List<Flight> flights = new ArrayList<>();
    private FlightDAO flightDAO;
    private Boolean isFragmentDestroyed;

    /**
     * Handles options menu item selections.
     *
     * @param item The selected menu item.
     * @return true if the item action was handled, false otherwise.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (Boolean.TRUE.equals(isFragmentDestroyed)) {
            if (item.getItemId() == R.id.flight_help) {
                AlertDialog.Builder helpAlert = new AlertDialog.Builder(FlightTrackerActivity.this);
                helpAlert.setTitle(getString(R.string.flight_guide))
                        .setMessage(getString(R.string.flight_main_page_guide))
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            } else if (item.getItemId() == R.id.main_page) {
                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);
            } else if (item.getItemId() == R.id.flight_bear) {
                Intent bear = new Intent(this, BearImageGeneratorActivity.class);
                startActivity(bear);
            } else if (item.getItemId() == R.id.flight_quiz) {
                Intent quiz = new Intent(this, TriviaQuestionDatabaseActivity.class);
                startActivity(quiz);
            } else if (item.getItemId() == R.id.flight_currency) {
                Intent currency = new Intent(this, CurrencyConvertorActivity.class);
                startActivity(currency);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Initializes the activity, sets up the UI and data bindings.
     *
     * @param savedInstanceState State of the activity saved in a bundle.
     */
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create a Volley object to connect to the server
        queue = Volley.newRequestQueue(this);

        binding = ActivityFlightTrackerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // open the database
        FlightDatabase database = FlightDatabase.getDatabase(this);
        flightDAO = database.flightDAO();

        // set toolbar
        setSupportActionBar(binding.toolbar);

        // save the previous search term
        SharedPreferences sharedPreferences = getSharedPreferences("FlightTrackerPrefs", Context.MODE_PRIVATE);
        departAirportCode = sharedPreferences.getString("searchTerm", "");

        // set flight tracker view model
        flightViewModel = new ViewModelProvider(this).get(FlightTrackerViewModel.class);
        flightViewModel.isFragmentViewDestroyed.setValue(true);

        // initialize the recycler view
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the FlightAdapter with an empty list and set it on the RecyclerView
        myAdapter = new FlightAdapter(new ArrayList<>(), flightViewModel);
        binding.recyclerView.setAdapter(myAdapter);

        flightViewModel.flights.observe(this, flights -> {
            myAdapter.setFlights(flights);
            myAdapter.notifyDataSetChanged();
        });

        flightViewModel.selectedFlight.observe(this, details -> {
            if (details != null) {
                int type = flightViewModel.selectedFragmentType.getValue() == null ?
                        FlightDetailsFragment.TYPE_DETAILS :
                        flightViewModel.selectedFragmentType.getValue();

                Log.d("VM Observe", "Activity 135: Fragment type is "
                        + flightViewModel.selectedFragmentType.getValue());

                FlightDetailsFragment flightDetailsFragment = new FlightDetailsFragment(details, type);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentLocation, flightDetailsFragment)
                        .addToBackStack(null)
                        .commit();
                flightViewModel.isFragmentViewDestroyed.setValue(false);
                Log.d("Selected Flight",
                        "After click details: " + flightViewModel.selectedFlight);
                Log.d("Fragment Type",
                        "After click details: "
                                + flightViewModel.selectedFragmentType.getValue());
            }
            // flightViewModel.selectedFlight.setValue(null);
        });

        // set the saved search term to the EditText
        binding.airportCode.setText(departAirportCode);

        // initialize a new color and set it when click the buttons
        int newBtnColor = ContextCompat.getColor(this, R.color.flight_tracker_secondary);
        int primaryBtnColor = ContextCompat.getColor(this,R.color.flight_tracker_primary);

        // add event action to the search button
        binding.searchButton.setOnClickListener(clk -> {
            binding.searchButton.setBackgroundColor(newBtnColor);
            binding.savedButton.setBackgroundColor(primaryBtnColor);

            flightViewModel.selectedFragmentType.setValue(FlightDetailsFragment.TYPE_DETAILS);
            departAirportCode = binding.airportCode.getText().toString();

            String searchUrl = "http://api.aviationstack.com/v1/flights?" +
                    "access_key=b5ab97314efc90f3ba84143687af7787&dep_iata=" + departAirportCode +
                    "&flight_status=active";

            if (departAirportCode.isEmpty()) {
                String emptyCode = "Airport code is empty.";
                Toast.makeText(this, emptyCode, Toast.LENGTH_SHORT).show();
                binding.progressBar.setVisibility(View.INVISIBLE);
                binding.recyclerView.setVisibility(View.INVISIBLE);
                binding.searchButton.setBackgroundColor(primaryBtnColor);
            } else {
                Log.d("FlightTrackerActivity", searchUrl);
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.INVISIBLE);

                // save search term to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("searchTerm", departAirportCode);
                editor.apply();

                getFlightData(searchUrl, new FetchDataCallback() {
                    @Override
                    public void onSuccess() {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {
                        String errorText = getString(R.string.flight_load_error);
                        Toast.makeText(FlightTrackerActivity.this, errorText, Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // add event listener to the saved button
        binding.savedButton.setOnClickListener(cl ->{
            binding.savedButton.setBackgroundColor(newBtnColor);
            binding.searchButton.setBackgroundColor(primaryBtnColor);
            flightViewModel.selectedFragmentType.setValue(FlightDetailsFragment.TYPE_DELETE);

            Log.d("CHECK_TYPE", "Set fragment type: " + flightViewModel.selectedFragmentType.getValue());
            refreshFlights();
        });

    }

    /**
     * Refreshes the flight list from the database.
     */
    public void refreshFlights() {
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(()->{
            List<Flight> savedFlights = flightDAO.getAllSavedFlights();
            runOnUiThread(()->{
                myAdapter.setFlights(savedFlights);
                myAdapter.notifyDataSetChanged();
            });
        });
    }

    /**
     * Inflates the menu items into the action bar.
     *
     * @param menu The menu into which elements should be placed.
     * @return true to display the menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("MENU CHECK", "onCreateOptionsMenu: Fragment type: " +
                flightViewModel.selectedFragmentType.getValue());
        isFragmentDestroyed = flightViewModel.isFragmentViewDestroyed.getValue();

        if (Boolean.TRUE.equals(isFragmentDestroyed)) {
            getMenuInflater().inflate(R.menu.flight_toolbar_menu, menu);
            MenuCompat.setGroupDividerEnabled(menu, true);
        }
        return true;
    }

    /**
     * Retrieves flight data from the provided URL and updates the flight view model.
     *
     * @param searchUrl The URL to fetch flight data from.
     * @param callback A callback to handle success or error.
     */
    private void getFlightData(String searchUrl, FetchDataCallback callback) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, searchUrl,
            null,
            response -> {
                Log.d("FlightTrackerActivity", "Response: " + response.toString());
                flights.clear();
                try {
                    // get array data about flights
                    JSONArray data = response.getJSONArray("data");

                    // loop the array
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject flightData = data.getJSONObject(i);

                        // get departure obj
                        JSONObject departure = flightData.getJSONObject("departure");
                        String deptAirport = departure.getString("airport");
                        String deptTerminal = departure.getString("terminal");
                        String deptGate = departure.getString("gate");
                        String deptIata = departure.getString("iata");
                        int deptDelay;
                        if (!departure.isNull("delay")) {
                            deptDelay = departure.getInt("delay");
                        } else {
                            deptDelay = 0;
                        }
                        // get departure time
                        String deptTime = getTimeFromJSON(departure, "scheduled");

                        // get arrive obj
                        JSONObject arrival = flightData.getJSONObject("arrival");
                        String arriAirport = arrival.getString("airport");
                        String arriTerminal = arrival.getString("terminal");
                        String arriGate = arrival.getString("gate");
                        String arriIata = arrival.getString("iata");
                        int arriDelay;
                        if (!arrival.isNull("delay")) {
                            arriDelay = departure.getInt("delay");
                        } else {
                            arriDelay = 0;
                        }
                        // get arrive time
                        String arriTime = getTimeFromJSON(arrival, "scheduled");

                        // get flight obj
                        JSONObject flight = flightData.getJSONObject("flight");
                        String flightNumber = flight.getString("iata");

                        Flight flightObj = new Flight(deptAirport,
                                deptTime,
                                arriAirport,
                                arriTime,
                                deptTerminal,
                                arriTerminal,
                                deptGate,
                                arriGate,
                                deptDelay,
                                arriDelay,
                                flightNumber,
                                deptIata,
                                arriIata);

                        flights.add(flightObj);
                    }
                    Set<Flight> uniqueFlights = new HashSet<>(flights);
                    flights.clear();
                    flights.addAll(uniqueFlights);

                    // flights = new ArrayList<>(flightSet);
                    flightViewModel.flights.setValue(new ArrayList<>(flights));

                    // call the onSuccess callback
                    callback.onSuccess();

                } catch (JSONException ex) {
                    ex.printStackTrace();
                    // call the onError callback
                    callback.onError();
                }
            },
            error -> {
                Log.e("FlightTrackerActivity", "Error: " + error.toString());
                callback.onError();
            });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);
    }

    /**
     * Converts a JSON time string to a formatted time string.
     *
     * @param jsonObject The JSON object containing the time data.
     * @param scheduled The key to extract the time string from the JSON object.
     * @return A formatted time string.
     * @throws JSONException If there's an error parsing the JSON.
     */
    private String getTimeFromJSON(JSONObject jsonObject, String scheduled) throws JSONException {

        try {
            String time = jsonObject.getString(scheduled);

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            parser.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = parser.parse(time);

            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            assert date != null;
            return formatter.format(date);
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * A map to store information about deleted flights and their positions.
     */
    public Map<Flight, Integer> deletedFlightsMap = new HashMap<>();

}