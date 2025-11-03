package algonquin.cst2335.finalandroidgroupproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalandroidgroupproject.databinding.ActivityCurrencyConverterBinding;
import algonquin.cst2335.finalandroidgroupproject.databinding.*;

/**
 * @author Dai Huang
 * @version 2.0 final version
 * Activity to handle the conversion of currency.
 * Provides functionalities for currency conversion and manipulating currency list.
 */
public class CurrencyConvertorActivity extends AppCompatActivity {

    // Declare all UI elements and variables used in this activity
    ActivityCurrencyConverterBinding currencyBinding;
    RecyclerView currencyRecyclerView;
    private RecyclerView.Adapter myAdapter;
    ArrayList<CurrencyItemList> currencyItemList = new ArrayList<>();
    CurrencyViewModel currencyViewModel;
    CurrencyItemListDAO myDAO;
    TextView txt_anime;

    EditText txt_anime2;

    private Button currencyConvertButton;
    private EditText currencyAmtTxt;
    private TextView currencyResultAmt;
    private Spinner spinnerFromUnit;
    private Spinner spinnerToUnit;
    private androidx.appcompat.widget.Toolbar currencyToolbar;
    private Button currencyAddlist;

    private String currencyConvertedTime = "";

    private RequestQueue requestQueue;

    private static final String APIkey = "7a4d7ea19909dd410ca14496e90cc290fdf8dd97";

    private void startAnime(){
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.anim);
        txt_anime.startAnimation(animation);
    }

    private void startSwing(){
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.anim2);
        txt_anime2.startAnimation(animation);
    }

    /**
     * This method is called when the activity is starting.
     * It performs initial setup for basic application functionality.
     *
     * @param savedInstanceState If the activity is being re-initialized
     * after previously being shut down then this Bundle contains the
     * data it most recently supplied. Otherwise it is null.
     */
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currencyBinding = ActivityCurrencyConverterBinding.inflate(getLayoutInflater());
        setContentView(currencyBinding.getRoot());

        // Data binding helps to connect the UI elements with the data
        currencyRecyclerView = currencyBinding.CurrencyRecyclerView;
        currencyAmtTxt = currencyBinding.CurrencyAmtTxt;
        currencyConvertButton = currencyBinding.CurrencyConvertButton;
        currencyResultAmt = currencyBinding.CurrencyResultAmt;
        spinnerFromUnit = currencyBinding.spinnerFromUnit;
        spinnerToUnit = currencyBinding.spinnerToUnit;
        currencyToolbar = currencyBinding.CurrencyToolbar;
        currencyAddlist = currencyBinding.CurrencyAddlist;
        txt_anime = findViewById(R.id.CurrencyResultAmt);
        txt_anime2 = findViewById(R.id.CurrencyAmtTxt);


        // Initialize components using data binding
        // Each of these variables are linked with a corresponding UI element
        currencyViewModel = new ViewModelProvider(this).get(CurrencyViewModel.class);
        currencyItemList = currencyViewModel.currencyListItem.getValue();



        setSupportActionBar(currencyToolbar);

        // Populate the spinner dropdown list with currency units
        // This step sets up the selection elements (spinners) in the UI
        ArrayAdapter<CharSequence> currencyAdapter = ArrayAdapter.createFromResource(this, R.array.currencyList, android.R.layout.simple_spinner_item);
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerFromUnit.setAdapter(currencyAdapter);
        spinnerToUnit.setAdapter(currencyAdapter);

        // Access the database
        CurrencyDatabase db = Room.databaseBuilder(getApplicationContext(), CurrencyDatabase.class, "CurrencyConvertorDatabase").build();
        myDAO = db.getDAO();


        // Fetching data from the database and updating the UI
        // If the list is empty, fetches all data from the database
        // This operation is run on a separate thread to avoid blocking the UI
        if (currencyItemList == null) {
            currencyViewModel.currencyListItem.setValue(currencyItemList = new ArrayList<>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                List<CurrencyItemList> fromDatabase = myDAO.getAllCurrencyList();
                currencyItemList.addAll((Collection<? extends CurrencyItemList>) fromDatabase);
                runOnUiThread(() -> {
                    currencyRecyclerView.setAdapter(myAdapter);
                });
            });
        }

        // Initialize Volley RequestQueue to connect to the server
        requestQueue = Volley.newRequestQueue(this);

        // Defining an Adapter for the RecyclerView
        // RecyclerView displays a list of items that can be scrolled vertically
        myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            // Creates a ViewHolder object representing a single row in the list
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                CurrencylistItemBinding listItemBinding = CurrencylistItemBinding.inflate(getLayoutInflater(), parent, false);
                return new MyRowHolder(listItemBinding.getRoot());
            }


            @Override
            // Initializes a ViewHolder object
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                CurrencyItemList obj = currencyItemList.get(position);
                // Set the input currency unit and amount to the list item
                holder.currency_from_unit.setText(obj.getFromCurrencyUnit());
                holder.currency_to_unit.setText(obj.getConvertedCurrencyUnit());
                holder.currency_list_fromAmt.setText("$" + obj.getFromCurrencyAmt());
                holder.currency_list_toAmt.setText("$" + obj.getConvertedCurrencyAmt());
                holder.convertTime.setText(obj.getTimeConverted());
            }

            @Override
            // Returns an int specifying how many items to draw
            public int getItemCount() {
                return currencyItemList.size();
            }
        };

        // Re-load input data from previous time running the application
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        int fromUnitIndex = prefs.getInt("FromCurrencyUnitIndex", 0);
        int toUnitIndex = prefs.getInt("ToCurrencyUnitIndex", 0);
        String preAmt = prefs.getString("AmountToConvert", "");
        spinnerFromUnit.setSelection(fromUnitIndex);
        spinnerToUnit.setSelection(toUnitIndex);
        currencyAmtTxt.setText(preAmt);

        String userFromCURRENCYUnit = spinnerFromUnit.getSelectedItem().toString();
        String userToCURRENCYUnit = spinnerToUnit.getSelectedItem().toString();
        int spinnerFromPos = currencyAdapter.getPosition(userFromCURRENCYUnit);
        int spinnerToPos = currencyAdapter.getPosition(userToCURRENCYUnit);

        // Convert button to convert the currency
        currencyConvertButton.setOnClickListener(click -> {
            String fromUnit = spinnerFromUnit.getSelectedItem().toString().substring(0, 3);
            String toUnit = spinnerToUnit.getSelectedItem().toString().substring(0, 3);
            String fromAmt = currencyAmtTxt.getText().toString();

            startSwing();

            // Save input data for next time run the application
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("FromCurrencyUnitIndex", spinnerFromPos);
            editor.putInt("ToCurrencyUnitIndex", spinnerToPos);
            editor.putString("AmountToConvert", fromAmt);
            editor.apply();

            String url = "https://api.getgeoapi.com/v2/currency/convert?format=json&from=" + fromUnit + "&to=" + toUnit + "&amount=" + fromAmt + "&api_key=" + APIkey + "&format=json";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("rates");
                        JSONObject rateObject = jsonObject.getJSONObject(toUnit);
                        String rateForAmt = rateObject.getString("rate_for_amount");
                        String updateDate = response.getString("updated_date");
                        String convAmt = formatAmount(rateForAmt);
                        currencyResultAmt.setText(convAmt);
                        currencyConvertedTime = updateDate;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            requestQueue.add(request);
        });



        currencyAddlist.setOnClickListener(clk -> {
            String fromUnit = spinnerFromUnit.getSelectedItem().toString().substring(0, 3);
            String toUnit = spinnerToUnit.getSelectedItem().toString().substring(0, 3);
            String fromAmt = currencyAmtTxt.getText().toString();
            String convertAmt = currencyResultAmt.getText().toString();

            startAnime();

            // Check if conversion has been made
            if (convertAmt.equals(getString(R.string.currency_result))) {
                makeToast(getString(R.string.currency_result_toast));
                return;
            }

            double fromAmtNum = Double.parseDouble(fromAmt);
            double convertAmtNum = Double.parseDouble(convertAmt);

            CurrencyItemList newListItem = new CurrencyItemList(fromUnit, fromAmtNum, toUnit, convertAmtNum, currencyConvertedTime);
            currencyItemList.add(newListItem);

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                newListItem.id = myDAO.insertCurrencyList(newListItem);
                // Run on UI thread
                runOnUiThread(() -> myAdapter.notifyItemInserted(currencyItemList.size() - 1));
            });

            makeToast(getString(R.string.currency_fav_toast));
        });

// Set up the RecyclerView with the adapter and layout manager
        currencyRecyclerView.setAdapter(myAdapter);
        currencyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

// Observe for changes in the selected item in the ViewModel
        currencyViewModel.selectedCurrencyItem.observe(this, (newValue) -> {
            CurrencyDetailsFragment currencyItemFragment = new CurrencyDetailsFragment(newValue);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.currency_fragmentSection, currencyItemFragment)
                    .addToBackStack(getString(R.string.currency_back_sign))
                    .commit();
        });

    }

    /**
     * Sets the toolbar for the activity.
     *
     * @param currencyToolbar The toolbar to be set.
     */
    private void setSupportActionBar(Toolbar currencyToolbar) {
    }

    CurrencyItemList selectedItem;
    int rowClicked;

    /**
     * ViewHolder for RecyclerView items. Holds references to the UI components in each row and sets up click listener.
     */
    public class MyRowHolder extends RecyclerView.ViewHolder {
        TextView currency_from_unit;
        TextView currency_to_unit;
        TextView currency_list_fromAmt;
        TextView currency_list_toAmt;
        TextView convertTime;


        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            currency_from_unit = itemView.findViewById(R.id.currency_list_from);
            currency_to_unit = itemView.findViewById(R.id.currency_list_to);
            currency_list_fromAmt = itemView.findViewById(R.id.currency_list_fromAmt);
            currency_list_toAmt = itemView.findViewById(R.id.currency_list_toAmt);
            convertTime = itemView.findViewById(R.id.time);

            // Show detail in fragment when click the row
            itemView.setOnClickListener(click -> {
                //rowClicked = getAbsoluteAdapterPosition();
                rowClicked = getAbsoluteAdapterPosition();
                selectedItem = currencyItemList.get(rowClicked);
                currencyViewModel.selectedCurrencyItem.postValue(selectedItem);
            });

        }
    }

    /**
     * Display a Toast message.
     *
     * @param str The string message to display.
     */
    public void makeToast(String str) {
        Toast toast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.currency_menu, menu);
        return true;
    }

    /**
     * This method is called whenever an item in your options menu is selected.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String aboutMsg = getString(R.string.currency_creator);

        if (item.getItemId() == R.id.currency_main) {
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);

        } else if (item.getItemId() == R.id.menu_about) {
            makeToast(aboutMsg);
        }
        else if (item.getItemId() == R.id.currency_search) {
            Intent main = new Intent(this, FlightTrackerActivity.class);
            startActivity(main);
        } else if (item.getItemId() == R.id.currency_bear) {
            Intent bear = new Intent(this, BearImageGeneratorActivity.class);
            startActivity(bear);
        } else if (item.getItemId() == R.id.currency_quiz) {
            Intent quiz = new Intent(this, TriviaQuestionDatabaseActivity.class);
            startActivity(quiz);}

        else if (item.getItemId() == R.id.menu_help) {
            AlertDialog.Builder helpBuilder = new AlertDialog.Builder(CurrencyConvertorActivity.this);
            List<String> helpMessages = new ArrayList<>();
            helpMessages.add(getString(R.string.currency_help));
            // Add more strings to helpMessages if needed

            CharSequence[] helpArray = helpMessages.toArray(new CharSequence[0]);

            helpBuilder.setItems(helpArray, (dialog, which) -> {
                        // Handle list item click if needed
                    })
                    .setNeutralButton(getString(R.string.currency_menu_back), ((dialog, which) -> {
                        // Handle the back button if needed
                    }))
                    .show();
        }
        return true;
    }

    /**
     * This method formats a string representing a monetary amount to a standard format.
     *
     * @param value The string to be formatted.
     * @return The formatted string.
     */
    private String formatAmount(String value) {
        double amt = Double.parseDouble(value);
        DecimalFormat format = new DecimalFormat("#.##");
        return format.format(amt);
    }
}

