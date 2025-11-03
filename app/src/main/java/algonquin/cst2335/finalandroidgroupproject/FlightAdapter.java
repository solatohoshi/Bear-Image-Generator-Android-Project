package algonquin.cst2335.finalandroidgroupproject;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import algonquin.cst2335.finalandroidgroupproject.data.FlightTrackerViewModel;
import algonquin.cst2335.finalandroidgroupproject.databinding.FlightResultBinding;

/**
 * An adapter for displaying flight information in a RecyclerView.
 *
 * @author Qiaoqing Wu
 * @version 1.0
 */
public class FlightAdapter
        extends RecyclerView.Adapter<FlightAdapter.MyRowHolder> {

    protected List<Flight> flights;
    private final FlightTrackerViewModel viewModel;

    /**
     * Constructor to initialize the FlightAdapter.
     *
     * @param flights List of flights to display.
     * @param viewModel ViewModel to interact with for updates.
     */
    public FlightAdapter(List<Flight> flights, FlightTrackerViewModel viewModel) {
        this.flights = flights;
        this.viewModel = viewModel;
    }

    /**
     * Called when RecyclerView needs a new {@link MyRowHolder} of the given type to represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added.
     * @param viewType The view type of the new View.
     * @return A new MyRowHolder that holds the FlightResultBinding.
     */
    @NonNull
    @Override
    public MyRowHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        FlightResultBinding binding = FlightResultBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new MyRowHolder(binding);
    }

    /**
     * Binds the data to the ViewHolder. Sets the flight data and event listeners.
     *
     * @param holder The ViewHolder to bind data to.
     * @param position The position of the item within the data set.
     */
    @Override
    public void onBindViewHolder
            (@NonNull MyRowHolder holder, int position) {
        Flight flight = flights.get(position);
        flight.setAdapterPosition(position);

        holder.binding.departTime.setText(flight.getDepartTime());
        holder.binding.departAirport.setText(flight.getDepartIata());
        holder.binding.arriveTime.setText(flight.getArrivalTime());
        holder.binding.arriveAirport.setText(flight.getArrivalIata());

        // set event listener to the details button
        holder.binding.detailBtn.setOnClickListener(v -> {
            viewModel.selectedFlight.setValue(flight);
            Log.d("after clicked detailBtn", "viewModel.selectedFlight: "
                    + viewModel.selectedFlight.getValue());
            Log.d("after clicked detailBtn", "viewModel.selectedFragment type: "
                    + viewModel.selectedFragmentType.getValue());
        });

    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in the data set.
     */
    @Override
    public int getItemCount() {
        return flights.size();
    }

    /**
     * Updates the list of flights and notifies the adapter.
     *
     * @param newFlights The new list of flights.
     */
    public void setFlights(List<Flight> newFlights) {
        this.flights.clear();
        this.flights.addAll(newFlights);
        notifyDataSetChanged();
    }

    /**
     * ViewHolder to represent individual flight rows in the RecyclerView.
     */
    public static class MyRowHolder extends RecyclerView.ViewHolder {

        FlightResultBinding binding;

        /**
         * Constructor to initialize the MyRowHolder.
         *
         * @param binding The FlightResultBinding for the row.
         */
        public MyRowHolder(@NonNull FlightResultBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
