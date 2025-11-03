package algonquin.cst2335.finalandroidgroupproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * @author Dai Huang
 * @version 1.0
 * This class is a Room database class.
 * @see CurrencyItemList
 */
@Database(entities = {CurrencyItemList.class},version = 1)
public abstract class CurrencyDatabase extends RoomDatabase {

    /**
     * This is an abstract method used to access the Data Access Object (DAO).
     * @return a DAO instance for the {@link CurrencyItemList} entity.
     */
    public abstract CurrencyItemListDAO getDAO();
}