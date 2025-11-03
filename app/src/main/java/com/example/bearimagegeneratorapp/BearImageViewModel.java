package com.example.bearimagegeneratorapp;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * view model class that has the array list of ImageHistory as type
 */
public class BearImageViewModel extends ViewModel {
    /**
     * sets the ImageHistory array list as mutable live data type
     */
    MutableLiveData<ArrayList<BearImage>> bearImages = new MutableLiveData<>();

}