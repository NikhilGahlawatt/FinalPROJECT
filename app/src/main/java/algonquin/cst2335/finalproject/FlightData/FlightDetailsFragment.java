package algonquin.cst2335.finalproject.FlightData;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.Adapters.FlightAdapter;
import algonquin.cst2335.finalproject.R;

/**
 * Fragment class for displaying FlightDetails.
 */
public class FlightDetailsFragment extends Fragment {

    // TextViews to display details of the flight.
    private TextView destinationTextView;
    private TextView terminalTextView;
    private TextView gateTextView;
    private TextView delayTextView;

    // Button to perform save/delete action.
    private Button actionButton;

    // Boolean to determine if we are dealing with the saved list or not.
    private boolean isSavedList;

    // Adapter for the RecyclerView of flights.
    private FlightAdapter adapter;

    /**
     * Inflates the layout for the Fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.flight_details, container, false);
    }

    /**
     * Called when the Fragment's view is created. Initializes the UI components, fetches
     * flight details from arguments, and sets up the interaction with the database.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize your text views
        destinationTextView = view.findViewById(R.id.DestinationText);
        terminalTextView = view.findViewById(R.id.TerminalText);
        gateTextView = view.findViewById(R.id.GateText);
        delayTextView = view.findViewById(R.id.DelayText);
        actionButton = view.findViewById(R.id.actionButton);

        // Get arguments from the bundle
        if (getArguments() != null) {
            String destination = getArguments().getString("destination");
            String terminal = getArguments().getString("terminal");
            String gate = getArguments().getString("gate");
            String delay = getArguments().getString("delay");
            String flightNumber = getArguments().getString("flightNumber");
            isSavedList = getArguments().getBoolean("isSavedList");

            // Initialize your FlightAdapter
            FlightDatabase flightDatabase = FlightDatabase.getInstance(requireContext());
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                List<FlightDetails> flights = flightDatabase.flightDao().getAllFlights();
                requireActivity().runOnUiThread(() -> {
                    adapter = new FlightAdapter(flights, flightDatabase, isSavedList);
                });
            });

            // Set the text of your text views
            destinationTextView.setText(destination);
            terminalTextView.setText(terminal);
            gateTextView.setText(gate);
            delayTextView.setText(delay);

            Log.d("FlightDetailsFragment", "isSavedList: " + isSavedList);

            // Set the text on the action button based on the list type
            if (isSavedList) {
                actionButton.setText(R.string.delete);
            } else {
                actionButton.setText(R.string.save);
            }

            // Handle button click based on the list type
            actionButton.setOnClickListener(v -> {
                if (isSavedList) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle(R.string.confirmation)
                            .setMessage(R.string.delete_flight_question)
                            .setPositiveButton(R.string.delete, (dialog, which) -> {
                                // Delete the flight from the database
                                deleteFlight(flightNumber);
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .show();
                } else {
                    // Save the flight to the database
                    saveFlight(flightNumber, destination, terminal, gate, delay);
                }
            });
        }
    }

    /**
     * Saves a flight to the Room database.
     * @param flightNumber The flight number
     * @param destination The destination of the flight
     * @param terminal The terminal of the flight
     * @param gate The gate of the flight
     * @param delay The delay status of the flight
     */
    private void saveFlight(String flightNumber, String destination, String terminal, String gate, String delay) {
        FlightDetails flightDetails = new FlightDetails();
        flightDetails.setFlightNumber(flightNumber);
        flightDetails.setArrivalAirport(destination);
        flightDetails.setTerminal(terminal);
        flightDetails.setGate(gate);
        flightDetails.setDelay(delay);

        FlightDatabase flightDatabase = FlightDatabase.getInstance(requireContext());

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            flightDatabase.flightDao().insertFlight(flightDetails);

            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), R.string.flight_details_saved, Toast.LENGTH_SHORT).show();
            });
        });
    }

    /**
     * Deletes a flight from the Room database.
     * @param flightNumber The flight number
     */
    private void deleteFlight(String flightNumber) {
        FlightDatabase flightDatabase = FlightDatabase.getInstance(requireContext());

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // Retrieve the existing flight from the database
            FlightDetails flightDetails = flightDatabase.flightDao().getFlightByNumber(flightNumber);

            if (flightDetails != null) {
                flightDatabase.flightDao().deleteFlight(flightDetails);

                // Get the updated list of flights
                List<FlightDetails> updatedFlights = flightDatabase.flightDao().getAllFlights();

                requireActivity().runOnUiThread(() -> {
                    // Update the adapter with the new list
                    adapter.deleteFlight(flightDetails);

                    // Display a Snackbar
                    String snackbarText = getString(R.string.flight_deleted, flightNumber);
                    Snackbar snackbar = Snackbar.make(requireView(), snackbarText, Snackbar.LENGTH_LONG);
                    snackbar.show();

                    //Simulate the saved button click
                    Button showSavedButton = (Button) requireActivity().findViewById(R.id.SavedButton);
                    showSavedButton.performClick();

                    requireActivity().onBackPressed();
                });
            } else {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), R.string.flight_not_found, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
