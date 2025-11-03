package algonquin.cst2335.finalandroidgroupproject.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.finalandroidgroupproject.Flight;

/**
 * ViewModel class for managing and storing flight-related data for the Flight Tracker app.
 * This class provides LiveData observables that can be observed by UI components to respond to data changes.
 *
 * @author Qiaoqing Wu
 * @version 1.0
 */
public class FlightTrackerViewModel extends ViewModel {

    /**
     * LiveData object holding a list of flights. Any changes to this list will be observed by the
     * subscribed UI components.
     */
    public MutableLiveData<ArrayList<Flight>> flights = new MutableLiveData<>();

    /**
     * LiveData object representing a selected flight. UI components can observe this to respond to
     * any change in the selected flight.
     */
    public MutableLiveData<Flight> selectedFlight = new MutableLiveData<>();

    /**
     * LiveData object representing the type of fragment currently selected (e.g., Details or Delete).
     * UI components can observe this to adjust the display based on the selected fragment type.
     */
    public final MutableLiveData<Integer> selectedFragmentType = new MutableLiveData<>();

    /**
     * LiveData indicating if the fragment's view was destroyed.
     * {@code true} when the view is destroyed, otherwise {@code false}.
     */
    public MutableLiveData<Boolean> isFragmentViewDestroyed = new MutableLiveData<>();
}
