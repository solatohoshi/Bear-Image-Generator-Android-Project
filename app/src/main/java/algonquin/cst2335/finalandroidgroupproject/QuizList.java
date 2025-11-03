package algonquin.cst2335.finalandroidgroupproject;

import java.util.ArrayList;

/**
 * this is our quiz list
 */
public class QuizList {
    /**
     * quiz question
     */
    String question;
    /**
     * quiz answer
     */
    String answer;
   // boolean isSentButton;
    /**
     * array for wrong answers
     */
   public ArrayList<String> wrong;
    /**
     * this is used for make sure there is no selected answer at first
     * cause recycly view will auto-check the button
     */
   private int userSelectedOption = -1;
    /**
     * This is used to make sure the user score will not always plus
     */
    private boolean answeredCorrectly = false;

    /**
     * when the answer is correct, it will plus
     * @return is the true
     */
    public boolean isAnsweredCorrectly() {
        return answeredCorrectly;
    }

    /**
     * this is setter method
     * @param answeredCorrectly is the test result
     */
    public void setAnsweredCorrectly(boolean answeredCorrectly) {
        this.answeredCorrectly = answeredCorrectly;
    }

    /**
     * this is the getter method used to get which one is the selected answer
     * @return
     */
    public int getUserSelectedOption() {
        return userSelectedOption;
    }

    /**
     * this is the setter method
     * @param userSelectedOption
     */
    public void setUserSelectedOption(int userSelectedOption) {
        this.userSelectedOption = userSelectedOption;
    }


    /**
     * The quiz list to store question, answer, and incorrect answers in a list
     * @param q is the question
     * @param a is the answer
     * @param w is the wrong answers
     */
    QuizList(String q, String a, ArrayList<String> w)
    {
        question = q;
        answer = a;
        wrong = w;
    }

    /**
     * we use this to display in the fragment
     * @param q is the question
     * @param a is the answer
     */
    public QuizList(String q, String a)//for database queries
    {
        question = q;
        answer = a;
    }


}
