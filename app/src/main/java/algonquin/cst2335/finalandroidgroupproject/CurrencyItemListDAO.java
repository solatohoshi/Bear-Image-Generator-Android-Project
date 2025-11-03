package algonquin.cst2335.finalandroidgroupproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * @author Dai Huang
 * @version 1.0
 * It's the DAO
 */
@Dao
public interface CurrencyItemListDAO {

    @Insert
    long insertCurrencyList(CurrencyItemList listItem);

    @Query
            ("Select * From CurrencyItemList")
    List<CurrencyItemList> getAllCurrencyList();

    @Delete
    int deleteCurrencyList(CurrencyItemList listItem);
}