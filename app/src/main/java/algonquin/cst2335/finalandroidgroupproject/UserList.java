package algonquin.cst2335.finalandroidgroupproject;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
/**
 * this is our user list in database
 */
public class UserList {
    /**
     * the auto generator key
     */
    @PrimaryKey(autoGenerate = true)
    /**
     * the id for users
     */
    @ColumnInfo(name="id")
    public long id;
    /**
     * the user name
     */
    @ColumnInfo(name="userName")
    String userName;
    /**
     * the user score
     */
    @ColumnInfo(name="userScore")
    Integer userScore;
    /**
     * the full score
     */
    @ColumnInfo(name="fullScore")
    String fullScore;
    /**
     * Function to get userName in shared preference
     * @return is the user name
     */
    public String getName(){
        return userName;
    }
    /**
     * Function to get fullScore in shared preference
     * @return is the full score
     */
    public String getFullScore(){
        return fullScore;
    }
    /**
     * constructor for user list
     * @param name is user name
     * @param score is user score
     * @param fuScore is full score
     */
    public UserList(String name, Integer score, String fuScore)
    {
        userName = name;
        userScore = score;
        fullScore = fuScore;
    }

    /**
     * no arg constructor
     */
    public UserList(){}

    /**
     * set id to users
     * @param id
     */
    public void setId(long id) {
        this.id= id;
    }
}
