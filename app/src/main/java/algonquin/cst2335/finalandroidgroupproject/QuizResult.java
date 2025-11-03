package algonquin.cst2335.finalandroidgroupproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalandroidgroupproject.databinding.ActivityQuizResultBinding;
import algonquin.cst2335.finalandroidgroupproject.databinding.UserListBinding;

/**
 * this is used for the user list display
 */
public class QuizResult extends AppCompatActivity {
    /**
     * variable binding
     */
    private ActivityQuizResultBinding variableBinding;
    /**
     * the user list
     */
    ArrayList<UserList> users ;
    /**
     * view model to see users
     */
    private UserListViewModel userModel ;
    /**
     * This is our tool bar
     */
    protected Toolbar toolbar;
    /**
     * recycle view to see users, this is the adapter
     */
    private RecyclerView.Adapter myAdapter;
    /**
     * recycle view to see users, this is the view
     */
    RecyclerView recycleView;
    /**
     * to retrieve the user information
     */
    UserListDAO userDAO;
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
            Intent project3Page = new Intent( QuizResult.this, TriviaQuestionDatabaseActivity.class);
            startActivity(project3Page);
        }else if (item.getItemId() == R.id.item_home) {
            Intent project3Page = new Intent( QuizResult.this, MainActivity.class);
            startActivity(project3Page);
        }else if(item.getItemId() == R.id.item_bear){
            Intent project4Page = new Intent( QuizResult.this, BearImageGeneratorActivity.class);
            startActivity(project4Page);
        }else if(item.getItemId() == R.id.item_flight){
            Intent project1Page = new Intent( QuizResult.this, FlightTrackerActivity.class);
            startActivity(project1Page);
        }else if(item.getItemId() == R.id.item_currency){
            Intent project2Page = new Intent( QuizResult.this, CurrencyConvertorActivity.class);
            startActivity(project2Page);
        }else if (item.getItemId() == R.id.item_2) {
            AlertDialog.Builder builder = new AlertDialog.Builder( QuizResult.this );
            String helpMsg2=getResources().getString(R.string.helpMsg3);
            builder.setMessage(helpMsg2)
                    .show();
        }
        return true;

    }

    /**
     * this is the main function to create
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
/**
 * the back button can go back to our main page
 */
        variableBinding = ActivityQuizResultBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        variableBinding.backBtn.setOnClickListener(v->{
            Intent mainPage = new Intent(QuizResult.this, MainActivity.class);
            startActivity(mainPage);
        });
        toolbar = variableBinding.toolbar;
        setSupportActionBar(toolbar);

/**
 * to retrieve the user information
 */
        UserDatabase db = UserDatabase.getInstance(this);
        userDAO = db.userDAO();

        userModel = new ViewModelProvider(this).get(UserListViewModel.class);
        users = userModel.users.getValue();
        if(users == null)
        {
            userModel.users.postValue( users = new ArrayList<>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                users.addAll( userDAO.getAllUsers() ); //Once you get the data from database
                Collections.sort(users, (user1, user2) -> Integer.compare(user2.userScore, user1.userScore));
                runOnUiThread( () ->  variableBinding.recycleView.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }
        /**
         * the refresh button can get all users
         */
        variableBinding.refresh.setOnClickListener(c->{
            userModel.users.postValue( users = new ArrayList<>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                users.addAll( userDAO.getAllUsers() ); //Once you get the data from database
                Collections.sort(users, (user1, user2) -> Integer.compare(user2.userScore, user1.userScore));
                runOnUiThread( () ->  variableBinding.recycleView.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        });

/**
 * we use recycle view to see the users
 */
        recycleView = variableBinding.recycleView;

        recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {

            @NonNull
            @Override
            /**
             * this is the rowHolder function
             */
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    UserListBinding binding = UserListBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(binding.getRoot());
            }

            /**
             * we can use this function to set the user name and score to the icon on user list
             * @param holder   The ViewHolder which should be updated to represent the contents of the
             *                 item at the given position in the data set.
             * @param position The position of the item within the adapter's data set.
             */
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {

                UserList obj = users.get(position);
                holder.nameText.setText(obj.userName);
                holder.scoreText.setText(Integer.toString(obj.userScore)); // Convert int to String
                holder.fullScore.setText(obj.fullScore);
            }

            /**
             * to get the user size
             * @return is the size of users
             */
            @Override
            public int getItemCount() {
                return users.size();
            }

        });

        recycleView.setLayoutManager(new LinearLayoutManager(this));

    }

    /**
     * the row holder function can help us to attach to the page we will load and then write some funtions to each row
     */
    class MyRowHolder extends RecyclerView.ViewHolder {
        /**
         * the name and score for users
         */
        TextView nameText;
        TextView scoreText;
        TextView fullScore;

        /**
         * to delete or redo the delete to users
         * @param itemView is the user in each row
         */
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(clk ->{
                int position = getAbsoluteAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder( QuizResult.this );
                String deleteMsg=getResources().getString(R.string.deleteMsg);
                String deleteMsg2=getResources().getString(R.string.deleteMsg2);
/**
 * the builder to delete user, and then we can redo the delete
 */
                builder.setMessage(deleteMsg + nameText.getText())
                        .setTitle("Question:")
                        .setNegativeButton( "No", (dialog, cl) -> {})
                        .setPositiveButton( "Yes", (dialog, cl) -> {
                            int row = getAbsoluteAdapterPosition();
                            UserList user = users. get (row);
                            //delete from the database
                            Executor thread = Executors.newSingleThreadExecutor();
                            thread.execute(() ->{
                                //background
                                userDAO. deleteUser ( user );
                                users.remove (row);
                                //on UI thread
                                runOnUiThread(()->{myAdapter.notifyItemRemoved (row);});
                                Snackbar.make(nameText,deleteMsg2+position,Snackbar.LENGTH_LONG)
                                        .setAction("Undo", click ->{
                                            Executor thread3 = Executors.newSingleThreadExecutor();
                                            thread3.execute(()->{
                                                userDAO.insertUser(user);
                                                users.add(row,user);
                                                runOnUiThread(()->{myAdapter.notifyItemRemoved (row);});

                                            });
                                        })
                                        .show();
                            });

                        })

                        .create().show();
            });
            nameText=itemView.findViewById(R.id.userName);
            scoreText=itemView.findViewById(R.id.userScore);
            fullScore=itemView.findViewById(R.id.fullScore);
        }
    }

}

