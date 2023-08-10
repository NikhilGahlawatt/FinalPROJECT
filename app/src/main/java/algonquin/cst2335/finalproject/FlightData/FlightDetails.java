package algonquin.cst2335.finalproject.FlightData;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * This class represents a FlightDetails entity in the Room Database.
 * It includes columns for the flight's details and getter and setter methods for each.
 */
@Entity(tableName = "FlightDetails")
public class FlightDetails {

    /**
     * Unique ID for the flight, which acts as a primary key and is auto-generated.
     */
    @PrimaryKey(autoGenerate=true)
    @ColumnInfo(name="flightID")
    public int id;

    /**
     * Flight number, represented as a string.
     */
    @ColumnInfo(name="flightNumber")
    private String flightNumber;

    /**
     * Arrival airport, represented as a string.
     */
    @ColumnInfo(name="arrivalAirport")
    private String arrivalAirport;

    /**
     * Terminal, represented as a string.
     */
    @ColumnInfo(name="terminal")
    private String terminal;

    /**
     * Gate, represented as a string.
     */
    @ColumnInfo(name="gate")
    private String gate;

    /**
     * Delay, represented as a string.
     */
    @ColumnInfo(name="delay")
    private String delay;

    // Getters and setters for each field

    /**
     * Retrieves the flight ID.
     *
     * @return The flight ID.
     */
    public int getFlightID() {
        return id;
    }

    /**
     * Sets the flight ID.
     *
     * @param id The flight ID.
     */
    public void setFlightID(int id) {
        this.id = id;
    }

    /**
     * Retrieves the flight number.
     *
     * @return The flight number.
     */
    public String getFlightNumber() {
        return flightNumber;
    }

    /**
     * Sets the flight number.
     *
     * @param flightNumber The flight number.
     */
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    /**
     * Retrieves the arrival airport.
     *
     * @return The arrival airport.
     */
    public String getArrivalAirport() {
        return arrivalAirport;
    }

    /**
     * Sets the arrival airport.
     *
     * @param arrivalAirport The arrival airport.
     */
    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    /**
     * Retrieves the terminal.
     *
     * @return The terminal.
     */
    public String getTerminal() {
        return terminal;
    }

    /**
     * Sets the terminal.
     *
     * @param terminal The terminal.
     */
    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    /**
     * Retrieves the gate.
     *
     * @return The gate.
     */
    public String getGate() {
        return gate;
    }

    /**
     * Sets the gate.
     *
     * @param gate The gate.
     */
    public void setGate(String gate) {
        this.gate = gate;
    }

    /**
     * Retrieves the delay.
     *
     * @return The delay.
     */
    public String getDelay() {
        return delay;
    }

    /**
     * Sets the delay.
     *
     * @param delay The delay.
     */
    public void setDelay(String delay) {
        this.delay = delay;
    }

    /**
     * Default constructor. Ignored by Room's processing but used by the developer.
     */
    @Ignore
    public FlightDetails() {
    }

    /**
     * Full constructor used by Room to create instances of the FlightDetails entity.
     *
     * @param id Unique identifier for the flight
     * @param flightNumber Flight number
     * @param arrivalAirport Arrival airport
     * @param terminal Terminal
     * @param gate Gate
     * @param delay Delay
     */
    public FlightDetails(int id, String flightNumber, String arrivalAirport, String terminal, String gate, String delay) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.arrivalAirport = arrivalAirport;
        this.terminal = terminal;
        this.gate = gate;
        this.delay = delay;
    }
}
