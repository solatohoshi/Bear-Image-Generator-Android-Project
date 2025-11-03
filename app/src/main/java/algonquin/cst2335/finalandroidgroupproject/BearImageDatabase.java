package algonquin.cst2335.finalandroidgroupproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * The database class that extends RoomDatabase
 */
@Database(entities = {BearImage.class}, version = 1)
public abstract class BearImageDatabase extends RoomDatabase {

    /**
     * Method that returns ImageHistoryDAO
     * @return ImageHistoryDAO
     */
    public abstract BearImageDAO bearDAO();

}
