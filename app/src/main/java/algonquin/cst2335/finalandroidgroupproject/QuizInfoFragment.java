package algonquin.cst2335.finalandroidgroupproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalandroidgroupproject.databinding.QuizInfoBinding;

/**
 * This class is used to check the detailed information of quiz
 */
public class QuizInfoFragment extends Fragment {
    /**
     * the selected is used for select quiz
     */
    QuizList selected;

    /**
     * This is our function to set selected quiz
     * @param m
     */
    public QuizInfoFragment(QuizList m)
    {
        selected = m;
    }

    /**
     * This is used to revoke fragment page when we need to see the information of quiz
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return is the page of fragment
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        QuizInfoBinding variableBinding = QuizInfoBinding.inflate(inflater);
        variableBinding.question.setText ( selected.question );
        variableBinding.answer.setText(selected.answer);
        return variableBinding.getRoot();
    }
}
