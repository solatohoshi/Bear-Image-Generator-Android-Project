package algonquin.cst2335.finalandroidgroupproject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * This is used for view quizs and then we can select quiz to see information
 */
public class QuizAttemptViewModel extends ViewModel {
    /**
     * quizs is our array list
     */
    public MutableLiveData<ArrayList<QuizList>> quizs = new MutableLiveData<>();
    /**
     * selected quiz is which one we want to see detaild information
     */
    public MutableLiveData<QuizList> selectedQuiz = new MutableLiveData< >();

}
