package algonquin.cst2335.finalandroidgroupproject;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * The database entity class, with columns of id, width, heigh, and time.
 */
@Entity
public class BearImage  {
    @PrimaryKey(autoGenerate=true)
    @ColumnInfo(name = "id")
    public long id;
    @ColumnInfo(name = "width")
    Integer width;
    @ColumnInfo(name = "height")
    Integer height;
    @ColumnInfo(name = "time generated")
    String timeGenerated;

    /**
     * BearImage constructor taking width, height, timeGenerated as parameters
     * @param width width of bear image
     * @param height height of bear image
     * @param timeGenerated time genereated
     */
    public BearImage(Integer width, Integer height, String timeGenerated){
        this.width = width;
        this.height = height;
        this.timeGenerated = timeGenerated;
    }

}
