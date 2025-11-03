package algonquin.cst2335.finalandroidgroupproject;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
/**
 * This is our database to save users
 */
@Database(entities = {UserList.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {
    /**
     * we user userDAO to retrieve info of users
     * @return
     */
    public abstract UserListDAO userDAO();

    /**
     * the instance of database
     */
    private static UserDatabase instance;

    /**
     * used to connect our database
     * @param context
     * @return
     */
    public static synchronized UserDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            UserDatabase.class, "database-name")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
