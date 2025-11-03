package algonquin.cst2335.finalandroidgroupproject;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author Dai Huang
 * @version 1.0 Final
 * This class represents the currency conversion list that will be stored in the Room Database.
 * It contains information such as FromCurrencyUnit, FromAmount, ToCurrencyUnit, ConvertedAmount and ConvertTime.
 *
 * @see Entity
 * @see PrimaryKey
 * @see ColumnInfo
 */
@Entity
public class CurrencyItemList {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    long id;

    @ColumnInfo(name = "FromCurrencyUnit")
    String fromCurrencyUnit;

    @ColumnInfo(name = "FromAmount")
    double fromCurrencyAmt;

    @ColumnInfo(name = "ToCurrencyUnit")
    String convertedCurrencyUnit;

    @ColumnInfo(name = "ConvertedAmount")
    double convertedCurrencyAmt;

    @ColumnInfo(name = "ConvertTime")
    String timeConverted;

    public CurrencyItemList(){
    };

    /**
     * The constructor for the CurrencyItemList.
     *
     * @param fromCurrencyUnit The unit of the from currency.
     * @param fromCurrencyAmt The amount in the from currency unit.
     * @param convertedCurrencyUnit The unit of the converted currency.
     * @param convertedCurrencyAmt The amount in the converted currency unit.
     * @param time The time when the conversion was done.
     */
    public CurrencyItemList(String fromCurrencyUnit, double fromCurrencyAmt, String convertedCurrencyUnit, double convertedCurrencyAmt, String time){
        this.fromCurrencyUnit = fromCurrencyUnit;
        this.fromCurrencyAmt = fromCurrencyAmt;
        this.convertedCurrencyUnit = convertedCurrencyUnit;
        this.convertedCurrencyAmt = convertedCurrencyAmt;
        this.timeConverted = time;
    }

    public String getFromCurrencyUnit(){
        return fromCurrencyUnit;
    }

    public void setFromCurrencyUnit(String fromUnit){
        this.fromCurrencyUnit = fromUnit;
    }

    public double getFromCurrencyAmt(){
        return fromCurrencyAmt;
    }

    public void setFromCurrencyAmt(double amt){
        this.fromCurrencyAmt = amt;
    }

    public String getConvertedCurrencyUnit(){
        return convertedCurrencyUnit;
    }

    public void setConvertedCurrencyUnit(String toUnit){
        this.convertedCurrencyUnit = toUnit;
    }

    public double getConvertedCurrencyAmt(){
        return convertedCurrencyAmt;
    }

    public void setConvertedCurrencyAmt(double newAmt){
        this.convertedCurrencyAmt = newAmt;
    }

    public String getTimeConverted(){
        return timeConverted;
    }

    public void setTimeConverted(String update){
        this.timeConverted = update;
    }
}