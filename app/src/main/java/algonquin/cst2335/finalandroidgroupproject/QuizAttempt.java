package algonquin.cst2335.finalandroidgroupproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalandroidgroupproject.databinding.ActivityQuizAttemptBinding;
import algonquin.cst2335.finalandroidgroupproject.databinding.QuizListBinding;

/**
 * This class is used to attempt the quiz and calculate the score
 */
public class QuizAttempt extends AppCompatActivity {
    /**
     * This is the variable binding
     */
    private ActivityQuizAttemptBinding variableBinding;
    /**
     * This is our request queue
     */
    RequestQueue queue = null;
    /**
     * this is the quizs list
     */
    ArrayList<QuizList> quizs ;
    /**
     * This is the users list
     */
    ArrayList<UserList> users ;
    /**
     * this is the userDAO to retrieve user value
     */
    UserListDAO userDAO;

    /**
     * This is our adapter for recycle view
     */
    private RecyclerView.Adapter myAdapter;
    /**
     * This is our quiz model to try the quiz
     */
    private QuizAttemptViewModel quizModel ;
    /**
     * This is our recycle view
     */
    RecyclerView recyclerView;
    /**
     * This is our userScore
     */
    private int userScore=0;

    /**
     * This is the main function for us to write the code
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
/**
 * this is the queue for Volley and variables we need
 */
        queue = Volley.newRequestQueue(this);
        UserDatabase db = Room.databaseBuilder(getApplicationContext(), UserDatabase.class, "database-name").build();
        userDAO = db.userDAO();
        users = new ArrayList<>();

        super.onCreate(savedInstanceState);

        variableBinding = ActivityQuizAttemptBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        recyclerView = variableBinding.recyclerView;

        quizModel = new ViewModelProvider(this).get(QuizAttemptViewModel.class);
        quizs = quizModel.quizs.getValue();
        if(quizs == null)
        {
            quizModel.quizs.postValue( quizs = new ArrayList<>());
        }
/**
 * This is the main part for retrieve questions
 */
        String url = getIntent().getStringExtra("quiz_url");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                (response) -> {
                    try {
                        JSONArray quizArray = response.getJSONArray("results");
                        for (int i = 0; i < quizArray.length(); i++) {
                            JSONObject questionObj = quizArray.getJSONObject(i);
                            String questionText = questionObj.getString("question");
                            String answerText = questionObj.getString("correct_answer");
                            JSONArray wrongAnswer = questionObj.getJSONArray("incorrect_answers");
                            ArrayList<String> wrong = new ArrayList<>();
                            for (int j = 0; j < wrongAnswer.length(); j++) {
                                wrong.add(wrongAnswer.getString(j));
                            }
                            quizs.add(new QuizList(questionText, answerText,wrong));
                        }
                        myAdapter.notifyDataSetChanged();
                        /*runOnUiThread(() -> {
                            variableBinding.question.setText( questionText);
                        });*/

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                (error) -> {
                    error.printStackTrace();
                });
        queue.add(request);
/**
 * This is the fragment for quiz, we can check correct answer here
 */
        quizModel.selectedQuiz.observe(this, (newQuizList) -> {
                    FragmentManager fMgr = getSupportFragmentManager();
                    FragmentTransaction tx = fMgr.beginTransaction();
                    QuizInfoFragment quizFragment = new QuizInfoFragment(new QuizList(newQuizList.question, newQuizList.answer));

                    tx.add(R.id.fragmentLocation, quizFragment);
                    tx.commit();// This line actually loads the fragment into the specified FrameLayout
                    tx.addToBackStack("");
                });
/**
 * the quiz will in a recycle view
 */
        recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            /**
             * This funtion is used to choose which page will we load, at here we only have one page, the quiz detail page
             * @param parent   The ViewGroup into which the new View will be added after it is bound to
             *                 an adapter position.
             * @param viewType The view type of the new View.
             * @return is the page
             */
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if(viewType == 0) {
                    QuizListBinding variableBinding = QuizListBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(variableBinding.getRoot());
                } else{
                    QuizListBinding variableBinding = QuizListBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(variableBinding.getRoot());
                }
                }

            /**
             * This function will help us to choose which page to load
             * @param position position to query
             * @return
             */
            @Override
            public int getItemViewType(int position){
                QuizList obj = quizs.get(position);
                //if
                    return 0;
                //else //odd
                  //  return 1;

            }

            /**
             * This function is to set the retrieve values to each id in quiz detail page
             * @param holder   The ViewHolder which should be updated to represent the contents of the
             *                 item at the given position in the data set.
             * @param position The position of the item within the adapter's data set.
             */
            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {

                QuizList obj = quizs.get(position);
                holder.questionText.setText(obj.question);
                holder.radioButtons[0].setText(obj.answer);
                holder.number.setText(String.valueOf(position + 1));
                for (int i = 0; i < obj.wrong.size(); i++) {
                    switch (i) {
                        case 0:
                            holder.radioButtons[1].setText(obj.wrong.get(i));
                            break;
                        case 1:
                            holder.radioButtons[2].setText(obj.wrong.get(i));
                            break;
                        case 2:
                            holder.radioButtons[3].setText(obj.wrong.get(i));
                            break;
                    }
                }
                /*for (int i = 1; i < holder.radioButtons.length; i++) {
                    holder.radioButtons[i].setText(obj.wrong.get(i));
                }*/
                int userSelectedOption = obj.getUserSelectedOption();
                for (int i = 0; i < holder.radioButtons.length; i++) {
                    holder.radioButtons[i].setChecked(userSelectedOption == i);
                }
            }

            /**
             * This is the quiz amount
             * @return is the size of our quiz
             */
            @Override
            public int getItemCount() {
                return quizs.size();
            }

        });
        variableBinding.backBtn.setOnClickListener(v->{finish();});

/**
 * this submit button will help us to add the user and userScore into database
 */
        variableBinding.submitBtn.setOnClickListener(clk ->{
            Intent quizResultPage = new Intent( QuizAttempt.this, QuizResult.class);
            startActivity(quizResultPage);

            String toastMsg=getResources().getString(R.string.toastMsg);
            String fullScoreMsg=getResources().getString(R.string.fullScoreMsg);
            Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

            SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
            String userName = prefs.getString("userName", "");
            String fullScore = prefs.getString("number", "");
            UserList newUser = new UserList(userName, userScore, fullScoreMsg+fullScore+")");
            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(() -> {
                newUser.id = userDAO.insertUser(newUser);//add to database;
                users.add(newUser);
                runOnUiThread(()->{myAdapter.notifyDataSetChanged();});
                /*this runs in another thread*/
            });

        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    /**
     * this function can help us to calculate the user score
     */
    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView questionText,number;
        /*RadioButton answerText;
        RadioButton radio1,radio2,radio3;*/
        RadioButton[] radioButtons = new RadioButton[4]; // Assuming you have 4 options

        /**
         * this function is used to get the id on quiz detail page, and then set function
         * @param itemView is the way we can handle the icon
         */
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            questionText=itemView.findViewById(R.id.question);
            radioButtons[0]=itemView.findViewById(R.id.answer);
            radioButtons[1] = itemView.findViewById(R.id.radio1);
            radioButtons[2] = itemView.findViewById(R.id.radio2);
            radioButtons[3] = itemView.findViewById(R.id.radio3);

           /* radioButtons[0].setOnClickListener(click ->{
                if (!quizs.get(getAbsoluteAdapterPosition()).isAnsweredCorrectly()) {
                    userScore++;
                    quizs.get(getAbsoluteAdapterPosition()).setAnsweredCorrectly(true);
                    myAdapter.notifyDataSetChanged(); // Notify the adapter to update the UI
                }
            });*/
/**
 * This click listener is for fragment to check the correct answer
 */
            number.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                QuizList selected = quizs.get(position);
                // Notify the adapter that the data has changed, so the UI can update if needed
                quizModel.selectedQuiz.postValue(selected);
                });
/**
 * this one is used to make sure when click the right answer and the score will add
 */
            for (int i = 0; i < radioButtons.length; i++) {
                final int optionIndex = i;
                radioButtons[i].setOnClickListener(view -> {
                    int position = getAbsoluteAdapterPosition();
                    QuizList selected = quizs.get(position);
                    if (radioButtons[0].isChecked()) {
                        if (!quizs.get(getAbsoluteAdapterPosition()).isAnsweredCorrectly()) {
                            userScore++;
                            quizs.get(getAbsoluteAdapterPosition()).setAnsweredCorrectly(true);
                            myAdapter.notifyDataSetChanged(); // Notify the adapter to update the UI
                        }
                    }
                    selected.setUserSelectedOption(optionIndex);
                    myAdapter.notifyDataSetChanged();
                });
            }

        }

    }

}
