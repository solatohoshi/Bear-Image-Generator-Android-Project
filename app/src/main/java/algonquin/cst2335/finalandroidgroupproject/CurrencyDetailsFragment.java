package algonquin.cst2335.finalandroidgroupproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalandroidgroupproject.databinding.CurrencyItemDetailBinding;

/**
 * @author  Dai Huang
 * @version 1.0
 * This class is responsible for handling the logic related to the CurrencyDetailsFragment.
 * It extends the Fragment class and uses data binding for managing the UI components.
 * This fragment shows the details of a selected currency item.
 *
 * @see Fragment
 * @see CurrencyItemDetailBinding
 * @see CurrencyItemList
 */
public class CurrencyDetailsFragment extends Fragment {

    CurrencyItemDetailBinding fragmentBinding;
    CurrencyItemList selectedItem;
    CurrencyItemListDAO myDAO;

    /**
     * The constructor for the CurrencyDetailsFragment
     * @param selecteditem The item that the details will be shown for.
     */
    public CurrencyDetailsFragment(CurrencyItemList selecteditem){
        selectedItem = selecteditem;
    }

    /**
     * This method is called to ask the fragment to instantiate its user interface view.
     * It initializes the data binding and sets the UI elements' values to the selected item's details.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        fragmentBinding = CurrencyItemDetailBinding.inflate(inflater);

        CurrencyDatabase db = Room.databaseBuilder(getContext(), CurrencyDatabase.class, "CurrencyConvertorDatabase").build();
        myDAO = db.getDAO();

        fragmentBinding.fromUnitInput.setText(selectedItem.getFromCurrencyUnit());
        fragmentBinding.fromAmount.setText("$ " + selectedItem.getFromCurrencyAmt());
        fragmentBinding.toUnitInput.setText(selectedItem.getConvertedCurrencyUnit());
        fragmentBinding.toAmount.setText("$ " + selectedItem.getConvertedCurrencyAmt());
        fragmentBinding.convertTime.setText(selectedItem.getTimeConverted());

        // Set OnClickListener for the "Back" button
        fragmentBinding.buttonCurrencyBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CurrencyConvertorActivity.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for the "Delete" button
        fragmentBinding.buttonCurrencyDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSelectedItem();
            }
        });

        return fragmentBinding.getRoot();
    }

    private void deleteSelectedItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getString(R.string.currency_record_question))
                .setTitle(getString(R.string.currency_question_title))
                .setNegativeButton(getString(R.string.currency_no), ((dialog, clk) -> {}))
                .setPositiveButton(getString(R.string.currency_yes), ((dialog, clk) -> {
                    // Delete selected list item from database
                    Executor thread = Executors.newSingleThreadExecutor();
                    thread.execute(() -> {
                        myDAO.deleteCurrencyList(selectedItem);

                        Snackbar.make(fragmentBinding.getRoot(), getString(R.string.currency_record_deleted), Snackbar.LENGTH_LONG)
                                .setAction(getString(R.string.currency_undo), clk2 -> {
                                    //reinsert the message:
                                    Executor thrd = Executors.newSingleThreadExecutor();
                                    thrd.execute(() -> {
                                        myDAO.insertCurrencyList(selectedItem);
                                    });
                                })
                                .show();
                    });
                }))
                .create().show();
    }
}
