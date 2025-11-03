package com.example.bearimagegeneratorapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * BearImageDAO interface that defines the the methods to be implemented
 */
@Dao
public interface BearImageDAO {
    /**
     * the method that inserts image into database
     * @param i the image to be inserted
     * @return a long value representing the id
     */
    @Insert
    long insertImage(BearImage i);

    /**
     * this method selects all items from database
     * @return results of getting all image history
     */
    @Query("Select * from BearImage")
    List<BearImage> getAllImages();

    /**
     * method that deletes a record from database
     * @param i the image to be deleted
     */
    @Delete
    void deleteImage(BearImage i);
}