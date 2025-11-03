package algonquin.cst2335.finalandroidgroupproject;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
/**
 * our DAO to retrieve user from database
 */
public interface UserListDAO {
    @Insert
    /**
     * The insert function
     */
     long insertUser(UserList u);

    /**
     * the query to select all users then display in result page
     * @return
     */
    @Query("Select * from UserList")
     List<UserList> getAllUsers();

    /**
     * This is used to delete user record
     * @param u is the user in user list
     */
    @Delete
    void deleteUser(UserList u);


}
