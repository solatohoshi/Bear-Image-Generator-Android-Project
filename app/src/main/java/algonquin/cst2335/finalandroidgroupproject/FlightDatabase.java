package algonquin.cst2335.finalandroidgroupproject;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Represents the Room database for flights, encapsulating the data and providing methods to access
 * and modify it. This class follows the Singleton pattern to ensure that only a single instance
 * of the database can be created at any one time.
 *
 * @author Qiaoqing Wu
 * @version 1.0
 */
@Database(entities = {Flight.class}, version = 1)
public abstract class FlightDatabase extends RoomDatabase {

    /**
     * Provides the DAO (Data Access Object) for accessing and modifying flights in this database.
     *
     * @return An instance of {@link FlightDAO}.
     */
    public abstract FlightDAO flightDAO();

    // Singleton instance of the FlightDatabase
    private static volatile FlightDatabase INSTANCE;

    /**
     * Returns the singleton instance of the FlightDatabase. If no instance exists, one is created.
     *
     * @param context The context, used to create the database instance.
     * @return The singleton instance of the FlightDatabase.
     */
    public static FlightDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FlightDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            FlightDatabase.class, "FlightDatabase").build();
                }
            }
        }
        return INSTANCE;
    }

}
