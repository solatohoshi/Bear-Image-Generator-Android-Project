package algonquin.cst2335.finalandroidgroupproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import java.util.ArrayList;

import algonquin.cst2335.finalandroidgroupproject.databinding.ActivityTriviaQuestionDatabaseBinding;


public class TriviaQuestionDatabaseActivity extends AppCompatActivity {
    /**
     * This is variableBinding
     */
    private ActivityTriviaQuestionDatabaseBinding variableBinding;
    /**
     * These are the editText
     */
    protected EditText textInput, numOfQue;
    /**
     * This is the startBtn
     */
    protected Button startBtn, resultBtn;

    /**
     * This is the users we can save into the database
     */
    ArrayList<UserList> users ;
    /**
     * This is the userModel for recycle view
     */
    private UserListViewModel userModel ;
    /**
     * This is our UserDAO
     */
    UserListDAO userDAO;
    /**
     * This is the number for quiz category
     */
    int category;
    /**
     * This is our tool bar
     */
    protected Toolbar toolbar;

    /**
     * This is our menu
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
     * This is our menu selected
     * @param item The menu item that was selected.
     *
     * @return is the option
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.item_1) {
            Intent project3Page = new Intent( TriviaQuestionDatabaseActivity.this, TriviaQuestionDatabaseActivity.class);
            startActivity(project3Page);
        }else if (item.getItemId() == R.id.item_home) {
            Intent project3Page = new Intent( TriviaQuestionDatabaseActivity.this, MainActivity.class);
            startActivity(project3Page);
        }else if(item.getItemId() == R.id.item_bear){
            Intent project4Page = new Intent( TriviaQuestionDatabaseActivity.this, BearImageGeneratorActivity.class);
            startActivity(project4Page);
        }else if(item.getItemId() == R.id.item_flight){
            Intent project1Page = new Intent( TriviaQuestionDatabaseActivity.this, FlightTrackerActivity.class);
            startActivity(project1Page);
        }else if(item.getItemId() == R.id.item_currency){
            Intent project2Page = new Intent( TriviaQuestionDatabaseActivity.this, CurrencyConvertorActivity.class);
            startActivity(project2Page);
        } else if (item.getItemId() == R.id.item_2) {
            AlertDialog.Builder builder = new AlertDialog.Builder( TriviaQuestionDatabaseActivity.this );
            String helpMsg2=getResources().getString(R.string.helpMsg2);
            builder.setMessage(helpMsg2)
                   .show();
        }
        return true;

    }

    /**
     * This is our main function
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        UserDatabase db = Room.databaseBuilder(getApplicationContext(), UserDatabase.class, "database-name").build();
        userDAO = db.userDAO();

        variableBinding = ActivityTriviaQuestionDatabaseBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        variableBinding.backBtn.setOnClickListener(v->{finish();});

        textInput = variableBinding.textInput;
        startBtn = variableBinding.startBtn;
        resultBtn = variableBinding.resultBtn;
        numOfQue = variableBinding.numOfQue;

        toolbar = variableBinding.toolbar;
        setSupportActionBar(toolbar);

        resultBtn.setOnClickListener(v->{
            Intent quizResultPage = new Intent( TriviaQuestionDatabaseActivity.this, QuizResult.class);
            startActivity(quizResultPage);
        });
        /**
         * at here we can choose the quiz category
         */

        variableBinding.check1.setOnClickListener(click -> {
            /**
             * different number will for different categories
             */
            category = 21;
        });
        variableBinding.check2.setOnClickListener(click -> {
            category = 22;
        });
        variableBinding.check3.setOnClickListener(click -> {
            category = 23;
        });
        variableBinding.check4.setOnClickListener(click -> {
            category = 17;
        });
/**
 * at here we saved our user and number of questions
 */

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String userName = prefs.getString("userName", "");
        String number = prefs.getString("number", "");
        textInput.setText(userName);
        numOfQue.setText(number);

        userModel = new ViewModelProvider(this).get(UserListViewModel.class);
        users = userModel.users.getValue();
        if(users == null)
        {
            userModel.users.postValue( users = new ArrayList<>());
        }
/**
 * this is the function for start button, we will use it to retrieve quizs from openDB website
 */
        startBtn.setOnClickListener(click ->{

            try {
                String numberInput = numOfQue.getText().toString();
                int quizNum = Integer.parseInt(numberInput);
                Intent quizAttemptPage = new Intent(TriviaQuestionDatabaseActivity.this, QuizAttempt.class);
                String url = "https://opentdb.com/api.php?amount=" + quizNum + "&category=" + category + "&difficulty=medium&type=multiple";
                quizAttemptPage.putExtra("quiz_url", url);
                startActivity(quizAttemptPage);
                String input = textInput.getText().toString();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("userName", input);
                editor.putString("number", numberInput);
                editor.apply();
            } catch (NumberFormatException e) {
                // Handle the exception by displaying an AlertDialog
                String errorMsg = getResources().getString(R.string.erroMsg);
                AlertDialog.Builder builder = new AlertDialog.Builder(TriviaQuestionDatabaseActivity.this);
                builder.setTitle("Error")
                        .setMessage(errorMsg)
                        .setPositiveButton("OK", null)
                        .show();
            }



        });

    }
}