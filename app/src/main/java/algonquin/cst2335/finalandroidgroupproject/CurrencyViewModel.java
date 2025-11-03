package algonquin.cst2335.finalandroidgroupproject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * The ViewModel class is designed to store and manage UI-related data in a lifecycle conscious way.
 * The ViewModel class allows data to survive configuration changes such as screen rotations.
 * This ViewModel class manages the MutableLiveData instances for CurrencyItemList objects.
 *
 * @see ViewModel
 * @see MutableLiveData
 */
public class CurrencyViewModel extends ViewModel {

    /**
     * MutableLiveData instance holding an ArrayList of CurrencyItemList objects.
     * MutableLiveData is a data holder class that can be observed within a given lifecycle.
     * This allows updating the data held by the MutableLiveData instance and notifying its active observers about the change.
     */
    public MutableLiveData<ArrayList<CurrencyItemList>> currencyListItem = new MutableLiveData<>();

    /**
     * MutableLiveData instance holding a selected CurrencyItemList object.
     * MutableLiveData is a data holder class that can be observed within a given lifecycle.
     * This allows updating the data held by the MutableLiveData instance and notifying its active observers about the change.
     */
    public MutableLiveData<CurrencyItemList> selectedCurrencyItem = new MutableLiveData<>();


}