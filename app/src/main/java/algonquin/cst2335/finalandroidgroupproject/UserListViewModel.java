package algonquin.cst2335.finalandroidgroupproject;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * This class is used to view users
 */
public class UserListViewModel extends ViewModel {
    /**
     * This is used for when we rotate the screen, the info will not change
     */
    public MutableLiveData<ArrayList<UserList>> users = new MutableLiveData<>();


}
