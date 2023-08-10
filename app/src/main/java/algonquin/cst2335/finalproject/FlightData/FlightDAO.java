package algonquin.cst2335.finalproject.FlightData;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * FlightDAO (Data Access Object) provides methods for performing database operations on FlightDetails table.
 * This interface uses Room Persistence Library annotations to link these methods with database operations.
 */
@Dao
public interface FlightDAO {

    /**
     * Inserts a single flight into the FlightDetails table.
     *
     * @param flight A FlightDetails object that contains information about the flight.
     */
    @Insert
    void insertFlight(FlightDetails flight);

    /**
     * Deletes a single flight from the FlightDetails table.
     *
     * @param flight A FlightDetails object that contains information about the flight.
     */
    @Delete
    void deleteFlight(FlightDetails flight);

    /**
     * Retrieves all the flights from the FlightDetails table.
     *
     * @return A list of FlightDetails objects that contains information about all the flights.
     */
    @Query("SELECT * FROM FlightDetails")
    List<FlightDetails> getAllFlights();

    /**
     * Retrieves a single flight from the FlightDetails table by its flight number.
     *
     * @param flightNumber A String that represents the flight number of the desired flight.
     * @return A FlightDetails object that contains information about the desired flight.
     */
    @Query("SELECT * FROM FlightDetails WHERE flightNumber = :flightNumber LIMIT 1")
    FlightDetails getFlightByNumber(String flightNumber);
}
