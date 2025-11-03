package algonquin.cst2335.finalandroidgroupproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalandroidgroupproject.data.FlightTrackerViewModel;
import algonquin.cst2335.finalandroidgroupproject.databinding.FlightDetailsBinding;
import algonquin.cst2335.finalandroidgroupproject.databinding.FlightDetailsDeleteBinding;

/**
 * A fragment for displaying detailed flight information with options to save or delete the flight.
 *
 * @author Qiaoqing Wu
 * @version 1.0
 */
public class FlightDetailsFragment extends Fragment {

    private final Flight selectedFlight;
    private FlightDatabase flightDatabase;
    private FlightDAO flightDAO;
    private FlightTrackerViewModel flightViewModel;

    /**
     * Constants to indicate the type of fragment display.
     * Type of fragment with save flight button
     */
    public static final int TYPE_DETAILS = 0;

    /**
     * Constants to indicate the type of fragment display.
     * Type of fragment with delete flight button
     */
    public static final int TYPE_DELETE = 1;

    private final int fragmentType;

    /**
     * Constructor to initialize the FlightDetailsFragment.
     *
     * @param flight The selected flight to display.
     * @param type The type of fragment to be displayed (Details or Delete).
     */
    public FlightDetailsFragment(Flight flight, int type) {
        this.selectedFlight = flight;
        this.fragmentType = type;
        Log.d("FragmentCreation", "FlightDetailsFragment created with type: " + type);
    }

    /**
     * Lifecycle method called when the fragment is first created. Sets up the database and DAO.
     *
     * @param savedInstanceState Bundle containing saved state information.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            // Get a reference to the FlightDatabase
            flightDatabase = Room.databaseBuilder(
                    requireContext(), FlightDatabase.class, "FlightDatabase"
            ).build();
            flightDAO = flightDatabase.flightDAO();
        });
    }

    /**
     * Lifecycle method called to create the view for the fragment.
     *
     * @param inflater LayoutInflater to inflate the layout.
     * @param container Parent container view.
     * @param savedInstanceState Bundle containing saved state information.
     * @return The root view for the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        FlightDetailsBinding detailsBinding = FlightDetailsBinding.inflate(getLayoutInflater());
        FlightDetailsDeleteBinding detailsDeleteBinding = FlightDetailsDeleteBinding.inflate(getLayoutInflater());

        if (selectedFlight != null) {
            if (fragmentType == TYPE_DETAILS) {
                // set tool bar
                if (isAdded() && getActivity() != null) {
                    ((AppCompatActivity)getActivity())
                            .setSupportActionBar(detailsBinding.detailsFragToolbar);
                }

                // set departure details
                detailsBinding.flightNumber.setText(selectedFlight.flightNumber);
                detailsBinding.deptAirport.setText(selectedFlight.departAirport);
                detailsBinding.deptTime.setText(selectedFlight.departTime);
                detailsBinding.deptGate.setText(selectedFlight.departGate);
                detailsBinding.deptTerminal.setText(selectedFlight.departTerminal);
                detailsBinding.deptDelay.setText(String.valueOf(selectedFlight.departDelay));

                // set arrival details
                detailsBinding.destAirport.setText(selectedFlight.arrivalAirport);
                detailsBinding.destTime.setText(selectedFlight.arrivalTime);
                detailsBinding.destGate.setText(selectedFlight.arrivalGate);
                detailsBinding.destTerminal.setText(selectedFlight.arrivalTerminal);
                detailsBinding.destDelay.setText(String.valueOf(selectedFlight.arrivalDelay));

                String text = getString(R.string.flight_save_success);

                // set event listener to the save button
                detailsBinding.saveBtn.setOnClickListener(save -> {
                    Executor thread = Executors.newSingleThreadExecutor();
                    thread.execute(() -> {
                        flightDAO.saveOneFlight(selectedFlight);

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                });

                return detailsBinding.getRoot();

            } else if (fragmentType == TYPE_DELETE) {
                // set tool bar
                if (isAdded() && getActivity() != null) {
                    ((AppCompatActivity)getActivity())
                            .setSupportActionBar(detailsDeleteBinding.fragToolbar);
                }

                // set departure details
                detailsDeleteBinding.flightNumber.setText(selectedFlight.flightNumber);
                detailsDeleteBinding.deptAirport.setText(selectedFlight.departAirport);
                detailsDeleteBinding.deptTime.setText(selectedFlight.departTime);
                detailsDeleteBinding.deptGate.setText(selectedFlight.departGate);
                detailsDeleteBinding.deptTerminal.setText(selectedFlight.departTerminal);
                detailsDeleteBinding.deptDelay.setText(String.valueOf(selectedFlight.departDelay));

                // set arrival details
                detailsDeleteBinding.destAirport.setText(selectedFlight.arrivalAirport);
                detailsDeleteBinding.destTime.setText(selectedFlight.arrivalTime);
                detailsDeleteBinding.destGate.setText(selectedFlight.arrivalGate);
                detailsDeleteBinding.destTerminal.setText(selectedFlight.arrivalTerminal);
                detailsDeleteBinding.destDelay.setText(String.valueOf(selectedFlight.arrivalDelay));

                detailsDeleteBinding.deleteBtn.setOnClickListener(del->{
                    String deletedFlightNumber = selectedFlight.flightNumber;
                    int position = selectedFlight.getAdapterPosition();

                    AlertDialog.Builder deleteAlert = new AlertDialog.Builder(requireActivity());
                    deleteAlert.setMessage(getString(R.string.ask_if_delete) + deletedFlightNumber)
                            .setTitle(getString(R.string.flight_attention))
                            .setNegativeButton("No", null)
                            .setPositiveButton("Yes", ((dialog, which) -> {
                                Executor thread = Executors.newSingleThreadExecutor();
                                thread.execute(()->{
                                    flightDAO.deleteOneSavedFlight(selectedFlight);
                                    if (getActivity() != null) {
                                        getActivity().runOnUiThread(()->{
                                            if (getActivity() instanceof FlightTrackerActivity) {
                                                FlightTrackerActivity activity = (FlightTrackerActivity) getActivity();
                                                activity.deletedFlightsMap.put(selectedFlight, position);
                                                activity.refreshFlights();
                                                getParentFragmentManager().popBackStack();
                                                showUndoSnackbar(activity, selectedFlight);
                                            }
                                        });
                                    }
                                });
                            }))
                            .create().show();
                });
                return detailsDeleteBinding.getRoot();
            }
        } else {
            Log.d("FlightDetailsFragment", "Selected flight is null");
        }
        return null;
    }

    /**
     * Displays an undo snackbar after a flight is deleted.
     *
     * @param activity The parent activity.
     * @param selectedFlight The flight that was deleted.
     */
    private void showUndoSnackbar(FlightTrackerActivity activity, Flight selectedFlight) {
        Snackbar.make(activity.findViewById(R.id.recyclerView),
                        getString(R.string.flight_delete_success),
                        Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.flight_undo), undoClick -> {
                    Executor thread = Executors.newSingleThreadExecutor();
                    thread.execute(()->{
                        flightDAO.saveOneFlight(selectedFlight);
                        Log.d("Reinsert_check", "The flight is reinsert");
                        Log.d("check activity", "The result is: " + activity);
                            activity.runOnUiThread(()->{
                                Integer position = activity.deletedFlightsMap.get(selectedFlight);
                                Log.d("delete_position", "Initialize the position " + position);
                                if (position != null) {
                                    Log.d("position_notnull", "The delete position is: " + position);
                                    activity.myAdapter.flights.add(position, selectedFlight);
                                    activity.myAdapter.notifyItemInserted(position);
                                    Log.d("UNDO_TEST", "Adapter flights size: " + activity.myAdapter.flights.size());
                                    // remove from the map
                                    activity.deletedFlightsMap.remove(selectedFlight);
                                }
                            });

                    });
                })
                .show();
    }

    /**
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} has returned, but before
     * any saved state has been restored in to the view. This method initializes the {@code FlightTrackerViewModel}
     * and sets up an observer for changes to the {@code selectedFragmentType}.
     *
     * @param view               The view returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state
     *                           as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        flightViewModel = new ViewModelProvider(requireActivity()).get(FlightTrackerViewModel.class);
        flightViewModel.selectedFragmentType.observe(getViewLifecycleOwner(), type->{
            Log.d("Fragment Type", "Fragment 109: Type is "
                    + flightViewModel.selectedFragmentType.getValue());
        });
    }

    /**
     * Inflates the options menu.
     *
     * @param menu The options menu.
     * @param inflater The menu inflater.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // inflater.inflate(R.menu.flight_toolbar_menu, menu);
        if (flightViewModel.selectedFragmentType.getValue() != null &&
                flightViewModel.selectedFragmentType.getValue() != 2) {
            inflater.inflate(R.menu.flight_toolbar_menu, menu);
        } else {
            Log.d("Fragment Type", "Fragment type is " +
                    flightViewModel.selectedFragmentType.getValue());
        }
        // super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Handles item selections from the options menu.
     *
     * @param item The selected menu item.
     * @return boolean indicating if the item selection was handled.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Log.d("Fragment Menu", "Fragment type is " + fragmentType);

        if (item.getItemId() == R.id.flight_help) {
            if (fragmentType == TYPE_DELETE) {
                AlertDialog.Builder helpAlert = new AlertDialog.Builder(requireActivity());
                helpAlert.setTitle(getString(R.string.flight_guide))
                        .setMessage(getString(R.string.flight_delete_page_guide))
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            } else if (fragmentType == TYPE_DETAILS) {
                AlertDialog.Builder helpAlert2 = new AlertDialog.Builder(requireActivity());
                helpAlert2.setTitle(getString(R.string.flight_guide))
                        .setMessage(getString(R.string.flight_save_page_guide))
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            }
        } else if (item.getItemId() == R.id.main_page) {
            Intent main = new Intent(requireActivity(), MainActivity.class);
            startActivity(main);
        } else if (item.getItemId() == R.id.flight_bear) {
            Intent bear = new Intent(requireActivity(), BearImageGeneratorActivity.class);
            startActivity(bear);
        } else if (item.getItemId() == R.id.flight_quiz) {
            Intent quiz = new Intent(requireActivity(), TriviaQuestionDatabaseActivity.class);
            startActivity(quiz);
        } else if (item.getItemId() == R.id.flight_currency) {
            Intent currency = new Intent(requireActivity(), CurrencyConvertorActivity.class);
            startActivity(currency);
        }
       // return super.onOptionsItemSelected(item);
        return true;
    }

    /**
     * Lifecycle method called when the fragment's view is being destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Check onDestroyView", "The onDestroyView is called.");

        if (flightViewModel != null) {
            flightViewModel.selectedFlight.setValue(null);
            flightViewModel.isFragmentViewDestroyed.setValue(true);
            Log.d("Type after destroy", "Fragment Type after called onDestroy: "
                    + fragmentType);
        }
    }

}
