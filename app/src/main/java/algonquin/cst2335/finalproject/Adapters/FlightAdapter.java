package algonquin.cst2335.finalproject.Adapters;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import algonquin.cst2335.finalproject.FlightData.FlightDatabase;
import algonquin.cst2335.finalproject.FlightData.FlightDetails;
import algonquin.cst2335.finalproject.FlightData.FlightDetailsFragment;
import algonquin.cst2335.finalproject.R;

/**
 * This adapter is used to populate the RecyclerView in AviationActivity with FlightDetails.
 * It also provides methods for managing and interacting with the flights data.
 */
public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.ViewHolder> {

    /**
     * List that stores the details of flights.
     */
    private List<FlightDetails> flights;

    /**
     * Instance of the flight database to perform CRUD operations.
     */
    private FlightDatabase flightDatabase;

    /**
     * Boolean flag to determine if the list shown is the saved list or the search results.
     */
    private boolean isSavedList;

    /**
     * ViewHolder provides a reference to the views for each data item in the RecyclerView.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * TextView that displays the flight number.
         */
        public TextView flightNumberView;

        /**
         * TextView that displays the destination of the flight.
         */
        public TextView destinationView;

        public ViewHolder(View v) {
            super(v);
            flightNumberView = v.findViewById(R.id.flight_number);
            destinationView = v.findViewById(R.id.destination);
        }
    }
    /**
     * Constructor for the FlightAdapter.
     *
     * @param flights A list of FlightDetails.
     * @param database An instance of FlightDatabase.
     * @param isSavedList Boolean flag to determine if the list shown is the saved list or the search results.
     */
    public FlightAdapter(List<FlightDetails> flights, FlightDatabase database, boolean isSavedList) {
        this.flights = flights;
        this.flightDatabase = database;
        this.isSavedList = isSavedList;
        Log.d("FlightAdapter", "FlightAdapter constructor called");
    }
    /**
     * This method inflates the layout of each item of the RecyclerView.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.flight_list, parent, false);
        return new ViewHolder(v);
    }
    /**
     * This method removes the given flight details from the flights list and notifies the adapter about the change.
     *
     * @param flightDetails The flight details to be removed.
     */
    public void deleteFlight(FlightDetails flightDetails) {
        int position = flights.indexOf(flightDetails);
        if (position != -1) {
            flights.remove(position);
            notifyItemRemoved(position);
        }
    }
    /**
     * This method updates the flights list and notifies the adapter about the change.
     *
     * @param updatedFlights The new list of FlightDetails.
     */
    public void updateFlights(List<FlightDetails> updatedFlights) {
        this.flights = updatedFlights;
        notifyDataSetChanged();  // Notify the adapter about the change
    }
    /**
     * This method returns the flights list.
     *
     * @return The flights list.
     */
    public List<FlightDetails> getFlights() {
        return flights;
    }

    /**
     * This method updates the contents of the itemView to reflect the flight details at the given position.
     *
     * @param holder The ViewHolder which should be updated.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FlightDetails flight = flights.get(position);
        holder.flightNumberView.setText(flight.getFlightNumber());
        holder.destinationView.setText(flight.getArrivalAirport());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            FlightDetailsFragment fragment = new FlightDetailsFragment();
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("destination", flight.getArrivalAirport());
                bundle.putString("terminal", flight.getTerminal());
                bundle.putString("gate", flight.getGate());
                bundle.putString("delay", flight.getDelay());
                bundle.putString("flightNumber", flight.getFlightNumber());
                bundle.putBoolean("isSavedList", isSavedList);
                fragment.setArguments(bundle);

                // Show the flight details fragment
                    ((AppCompatActivity) v.getContext()).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentLocation, fragment)
                            .addToBackStack(null)
                            .commit();

            }
        });
    }

    /**
     * This method returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return flights.size();
    }
}
