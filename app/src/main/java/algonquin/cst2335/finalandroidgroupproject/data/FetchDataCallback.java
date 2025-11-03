package algonquin.cst2335.finalandroidgroupproject.data;

/**
 * An interface for callback methods to handle data fetching outcomes.
 * This provides a way to define actions to take upon successful data fetching
 * as well as when an error occurs during the fetching process.
 *
 * @author Qiaoqing Wu
 * @version 1.0
 */
public interface FetchDataCallback {

    /**
     * Called when data fetching is successful.
     */
    void onSuccess();

    /**
     * Called when there is an error during data fetching.
     */
    void onError();

}
