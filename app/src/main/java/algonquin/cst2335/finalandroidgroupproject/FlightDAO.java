package algonquin.cst2335.finalandroidgroupproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * DAO (Data Access Object) for accessing and modifying flights in the database.
 * This interface defines methods for saving, retrieving, and deleting flights.
 *
 * @author Qiaoqing Wu
 * @version 1.0
 */
@Dao
public interface FlightDAO {

    /**
     * Saves a single flight into the database.
     *
     * @param flight The flight to be saved.
     */
    @Insert
    void saveOneFlight(Flight flight);

    /**
     * Retrieves all saved flights from the database.
     *
     * @return A list containing all saved flights.
     */
    @Query("SELECT * FROM Flight")
    List<Flight> getAllSavedFlights();

    /**
     * Deletes a single saved flight from the database.
     *
     * @param flight The flight to be deleted.
     */
    @Delete
    void deleteOneSavedFlight(Flight flight);

}
