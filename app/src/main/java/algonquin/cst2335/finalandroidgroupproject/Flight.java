package algonquin.cst2335.finalandroidgroupproject;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Represents a flight entity in the Flight Tracker application.
 * This class provides detailed information about a specific flight, including details about
 * departure, arrival, terminal, gate, delay, and more.
 *
 * @author Qiaoqing Wu
 * @version 1.0
 */
@Entity
public class Flight {

    /**
     * Unique identifier for each flight.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "flightId")
    public long id;

    /**
     * Name of the airport from which the flight departs.
     */
    @ColumnInfo(name = "departAirport")
    protected String departAirport;

    /**
     * Departure time of the flight.
     */
    @ColumnInfo(name = "departTime")
    protected String departTime;

    /**
     * Name of the airport at which the flight arrives.
     */
    @ColumnInfo(name = "arrivalAirport")
    protected String arrivalAirport;

    /**
     * Arrival time of the flight.
     */
    @ColumnInfo(name = "arrivalTime")
    protected String arrivalTime;

    /**
     * Terminal from which the flight departs.
     */
    @ColumnInfo(name = "departTerminal")
    protected String departTerminal;

    /**
     * Terminal where the flight lands.
     */
    @ColumnInfo(name = "arrivalTerminal")
    protected String arrivalTerminal;

    /**
     * Gate from which the flight departs.
     */
    @ColumnInfo(name = "departGate")
    protected String departGate;

    /**
     * Gate where the flight lands.
     */
    @ColumnInfo(name = "arrivalGate")
    protected String arrivalGate;

    /**
     * Flight's official number.
     */
    @ColumnInfo(name = "flightNumber")
    protected String flightNumber;

    /**
     * The delay in minutes from the scheduled departure time.
     */
    @ColumnInfo(name = "departDelay")
    protected int departDelay;

    /**
     * The delay in minutes from the scheduled arrival time.
     */
    @ColumnInfo(name = "arrivalDelay")
    protected int arrivalDelay;

    /**
     * The IATA code for the departure airport.
     */
    private String departIata;

    /**
     * The IATA code for the arrival airport.
     */
    private String arrivalIata;

    /**
     * Transient field to store the position of this flight in an adapter.
     */
    private transient Integer adapterPosition = null;

    /**
     * Default constructor for the Flight entity.
     */
    public Flight() {

    }

    /**
     * Constructs a Flight object with detailed flight information.
     *
     * @param departAirport    Departure airport name.
     * @param departTime       Departure time.
     * @param arrivalAirport   Arrival airport name.
     * @param arrivalTime      Arrival time.
     * @param departTerminal   Departure terminal.
     * @param arrivalTerminal  Arrival terminal.
     * @param departGate       Departure gate.
     * @param arrivalGate      Arrival gate.
     * @param departDelay      Departure delay in minutes.
     * @param arrivalDelay     Arrival delay in minutes.
     * @param flightNumber     Official flight number.
     * @param departIata       IATA code for departure airport.
     * @param arrivalIata      IATA code for arrival airport.
     */
    public Flight(String departAirport,
                  String departTime,
                  String arrivalAirport,
                  String arrivalTime,
                  String departTerminal,
                  String arrivalTerminal,
                  String departGate,
                  String arrivalGate,
                  int departDelay,
                  int arrivalDelay,
                  String flightNumber,
                  String departIata,
                  String arrivalIata) {
        this.departAirport = departAirport;
        this.departTime = departTime;
        this.arrivalAirport = arrivalAirport;
        this.arrivalTime = arrivalTime;
        this.departTerminal = departTerminal;
        this.arrivalTerminal = arrivalTerminal;
        this.departGate = departGate;
        this.arrivalGate = arrivalGate;
        this.departDelay = departDelay;
        this.arrivalDelay = arrivalDelay;
        this.flightNumber = flightNumber;
        this.departIata = departIata;
        this.arrivalIata = arrivalIata;
    }

    /**
     * Determines if two Flight objects are equal based on departure and arrival details.
     *
     * @param obj   The object to compare against.
     * @return      true if objects are equal, otherwise false.
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Flight flight = (Flight) obj;
        return departTime.equals(flight.departTime) &&
               departIata.equals(flight.departIata) &&
               arrivalTime.equals(flight.arrivalTime) &&
               arrivalIata.equals(flight.arrivalIata);
    }

    /**
     * Sets the position of this flight in an adapter.
     *
     * @param position   Position to be set.
     */
    public void setAdapterPosition(int position) {
        this.adapterPosition = position;
    }

    /**
     * Retrieves the position of this flight in an adapter.
     *
     * @return  The position of the flight.
     */
    public Integer getAdapterPosition() {
        return this.adapterPosition;
    }

    /**
     * Generates a hash code for this Flight object based on departure and arrival details.
     *
     * @return  Generated hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(departTime, departIata, arrivalTime, arrivalIata);
    }

    /**
     * Retrieves the name of the departure airport.
     *
     * @return The name of the departure airport.
     */
    public String getDepartAirport() {
        return departAirport;
    }

    /**
     * Sets the name of the departure airport.
     *
     * @param departAirport Name of the departure airport.
     */
    public void setDepartAirport(String departAirport) {
        this.departAirport = departAirport;
    }

    /**
     * Retrieves the name of the arrival airport.
     *
     * @return The name of the arrival airport.
     */
    public String getArrivalAirport() {
        return arrivalAirport;
    }

    /**
     * Sets the name of the arrival airport.
     *
     * @param arrivalAirport Name of the arrival airport.
     */
    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    /**
     * Retrieves the name of the departure terminal.
     *
     * @return The name of the departure terminal.
     */
    public String getDepartTerminal() {
        return departTerminal;
    }

    /**
     * Sets the name of the departure terminal.
     *
     * @param departTerminal Name of the departure terminal.
     */
    public void setDepartTerminal(String departTerminal) {
        this.departTerminal = departTerminal;
    }

    /**
     * Retrieves the name of the arrival terminal.
     *
     * @return The name of the arrival terminal.
     */
    public String getArrivalTerminal() {
        return arrivalTerminal;
    }

    /**
     * Sets the name of the arrival terminal.
     *
     * @param arrivalTerminal Name of the arrival terminal.
     */
    public void setArrivalTerminal(String arrivalTerminal) {
        this.arrivalTerminal = arrivalTerminal;
    }

    /**
     * Retrieves the name of the departure gate.
     *
     * @return The name of the departure gate.
     */
    public String getDepartGate() {
        return departGate;
    }

    /**
     * Sets the name of the departure gate.
     *
     * @param departGate Name of the departure gate.
     * */
    public void setDepartGate(String departGate) {
        this.departGate = departGate;
    }
    /**
     * Retrieves the gate number for the flight's arrival.
     *
     * @return The gate number where the flight will arrive.
     */
    public String getArrivalGate() {
        return arrivalGate;
    }

    /**
     * Sets the gate number for the flight's arrival.
     *
     * @param arrivalGate Gate number where the flight will arrive.
     */
    public void setArrivalGate(String arrivalGate) {
        this.arrivalGate = arrivalGate;
    }

    /**
     * Retrieves the departure time for the flight.
     *
     * @return The departure time of the flight.
     */
    public String getDepartTime() {
        return departTime;
    }

    /**
     * Sets the departure time for the flight.
     *
     * @param departTime Departure time of the flight.
     */
    public void setDepartTime(String departTime) {
        this.departTime = departTime;
    }

    /**
     * Retrieves the arrival time for the flight.
     *
     * @return The time when the flight will arrive.
     */
    public String getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Sets the arrival time for the flight.
     *
     * @param arrivalTime Time when the flight will arrive.
     */
    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
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
     * Retrieves the IATA code for the departure airport.
     *
     * @return The IATA code for the departure airport.
     */
    public String getDepartIata() {
        return departIata;
    }

    /**
     * Sets the IATA code for the departure airport.
     *
     * @param departIata IATA code for the departure airport.
     */
    public void setDepartIata(String departIata) {
        this.departIata = departIata;
    }

    /**
     * Retrieves the IATA code for the arrival airport.
     *
     * @return The IATA code for the arrival airport.
     */
    public String getArrivalIata() {
        return arrivalIata;
    }

    /**
     * Sets the IATA code for the arrival airport.
     *
     * @param arrivalIata IATA code for the arrival airport.
     */
    public void setArrivalIata(String arrivalIata) {
        this.arrivalIata = arrivalIata;
    }
}
