package algonquin.cst2335.finalandroidgroupproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import algonquin.cst2335.finalandroidgroupproject.databinding.ActivityMainBinding;

/**
 * this is our main page
 */
public class MainActivity extends AppCompatActivity {
    /**
     * the variable binding for main page
     */
    private ActivityMainBinding variableBinding;
    /**
     * our tool bar
     */
    protected Toolbar toolbar;

    /**
     * the menu for tool bar
     * @param menu The options menu in which you place your items.
     *
     * @return is the menu
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.quiz_menu, menu);
        return true;
    }

    /**
     * this is the menu we can select item
     * @param item The menu item that was selected.
     *
     * @return is the item
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.item_1) {
            Intent project3Page = new Intent( MainActivity.this, TriviaQuestionDatabaseActivity.class);
            startActivity(project3Page);
        }
        else if(item.getItemId() == R.id.item_bear){
            Intent project4Page = new Intent( MainActivity.this, BearImageGeneratorActivity.class);
            startActivity(project4Page);
        }else if(item.getItemId() == R.id.item_flight){
            Intent project1Page = new Intent( MainActivity.this, FlightTrackerActivity.class);
            startActivity(project1Page);
        }else if(item.getItemId() == R.id.item_currency){
            Intent project2Page = new Intent( MainActivity.this, CurrencyConvertorActivity.class);
            startActivity(project2Page);
        }
        else if (item.getItemId() == R.id.item_2) {
            AlertDialog.Builder builder = new AlertDialog.Builder( MainActivity.this );
            String helpMsg = getResources().getString(R.string.helpMsg);
            builder.setMessage(helpMsg)
                    .show();
        }
        return true;

    }

    /**
     * this is the main funtion, we can start each activity here
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        toolbar = variableBinding.toolbar;
        setSupportActionBar(toolbar);

        variableBinding.project1.setOnClickListener(clk ->{
            Intent project1Page = new Intent( MainActivity.this, FlightTrackerActivity.class);
            startActivity(project1Page);
        });

        variableBinding.project2.setOnClickListener(clk ->{
            Intent project2Page = new Intent( MainActivity.this, CurrencyConvertorActivity.class);
            startActivity(project2Page);
        });

        variableBinding.project4.setOnClickListener(clk ->{
            Intent project4Page = new Intent( MainActivity.this, BearImageGeneratorActivity.class);
            startActivity(project4Page);
        });
        variableBinding.project3.setOnClickListener(clk ->{
            Intent project3Page = new Intent(MainActivity.this, TriviaQuestionDatabaseActivity.class);
            startActivity(project3Page);
        });
    }
}