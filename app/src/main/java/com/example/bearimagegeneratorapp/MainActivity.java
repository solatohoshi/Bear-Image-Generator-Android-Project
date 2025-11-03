package com.example.bearimagegeneratorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bearimagegeneratorapp.databinding.BearImageBinding;
import com.google.android.material.snackbar.Snackbar;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.zip.Inflater;

import com.example.bearimagegeneratorapp.databinding.ActivityMainBinding;

/**
 * This class is the application class for BearImageGenerator which loads the application layout,
 * which consists of two edit texts of width and height of bear image, a generation button, a recycler
 * view with history of generated images.
 */
public class MainActivity extends AppCompatActivity{

    /**
     * This is used to bind the elements in activity_bear_image_generator.xml.
     */
    private ActivityMainBinding variableBinding;
    /**
     * This is the adapter for recyclerview
     */
    static RecyclerView.Adapter<MyRowHolder> myAdapter;
    /**
     * This is an arraylist of generated images
     */
    ArrayList<BearImage> images = new ArrayList<>();

    static BearImageDAO bearDAO;
    /**
     * the BearImageViewModel object
     */
    BearImageViewModel imageModel;
    /**
     * the ImageDatabase object
     */
    BearImageDatabase db;


    /**
     * This shows error message depends on different situations.
     * @param message error message to be shown
     */
    private void showError(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.err))
                .setMessage(message)
                .show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        //load the data stored in SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String widthText = prefs.getString("width", "");
        variableBinding.width.setText(widthText);
        String heightText = prefs.getString("height", "");
        variableBinding.height.setText(heightText);

        //initialize database
        db = Room.databaseBuilder(getApplicationContext(), BearImageDatabase.class, "Bear Images Database").build();
        //connects database with DAO
        bearDAO = db.bearDAO();

        //Set view model
        imageModel = new ViewModelProvider(this).get(BearImageViewModel.class);
        //get value stored in view model
        images = imageModel.bearImages.getValue();
        //if the arraylist is null, set value in image view model
        if(images == null)
        {
            imageModel.bearImages.setValue(images = new ArrayList<>());

            //execute the sql query in another thread
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                images.addAll( bearDAO.getAllImages() ); //get the data from database
            });
        }

        //when users click the generate button, the width and height values will be retrieved and
        //examined, if it's empty, show an error message, if not, parse it to be integer and create
        //an BearImage object based on the information
        variableBinding.generatebtn.setOnClickListener(view->{
            // Close the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            String w = variableBinding.width.getText().toString();
            String h = variableBinding.height.getText().toString();

            if (!TextUtils.isEmpty(w) && !TextUtils.isEmpty(h)) {
                try {
                    int width = Integer.parseInt(w);
                    int height = Integer.parseInt(h);
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
                    String currentDateAndTime = sdf.format(new Date());
                    BearImage newImage = new BearImage(width, height, currentDateAndTime);
                    FrameLayout fragmentLocation = findViewById( R.id.fragmentLocation);

                    boolean location = fragmentLocation != null;
                    BearImageResultFragment resultFragment = new BearImageResultFragment(newImage, images);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("")
                            .replace(R.id.fragmentLocation, resultFragment)
                            .commit();
                }
                catch(NumberFormatException e){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(getString(R.string.err))
                            .setMessage(getString(R.string.invalidNumerr))
                            .show();
                }
            } else {
                showError(getString(R.string.empty));
            }
        });

        //when user clicks the view all images button, the AllImagesFragment will show up, displaying
        //all images stored in database
        variableBinding.viewAllImages.setOnClickListener( view -> {
            // Close the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            //show error message if there is no record of generated image
            if(images.size()==0){
                showError(getString(R.string.noimgerr));
            }
            else{
                FrameLayout fragmentLocation = findViewById( R.id.fragmentLocation);

                boolean location = fragmentLocation != null;
                AllImagesFragment allImagesFragment = new AllImagesFragment(images);
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("")
                        .replace(R.id.fragmentLocation, allImagesFragment)
                        .commit();
            }
        });

        //this sets the adapter
        myAdapter = new RecyclerView.Adapter<MyRowHolder>(){
            /**
             * This loads the row holder
             * @param parent   The ViewGroup into which the new View will be added after it is bound to
             *                 an adapter position.
             * @param viewType The view type of the new View.
             * @return image history row holder
             */
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                BearImageBinding binding = BearImageBinding.inflate(getLayoutInflater(),parent, false);
                return new MyRowHolder(binding.getRoot());
            }

            /**
             * This method binds the view holder, setting the button text to image size
             * @param holder   The ViewHolder which should be updated to represent the contents of the
             *                 item at the given position in the data set.
             * @param position The position of the item within the adapter's data set.
             */
            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                BearImage obj = images.get(position);
                holder.sizeBtn.setText(obj.width + " * " + obj.height);
            }

            /**
             * This gets the size of images
             * @return size of images
             */
            @Override
            public int getItemCount() {
                return images.size();
            }
        };
        variableBinding.recyclerView.setAdapter(myAdapter);
        variableBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Log.d("BearImageGenerator", "onCreate() debugging");
    }

    /**
     * This method deletes the selected image from from the database
     * @param p position of the image
     */
    public void deleteImage(int p){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.deleteMsgBear))
                .setTitle("Question")
                .setNegativeButton(getString(R.string.no), (dialog, cl) -> {
                })
                .setPositiveButton(getString(R.string.yes), (dialog, cl) -> {
                    BearImage i = images.get(p);
                    Executor thread1 = Executors.newSingleThreadExecutor();
                    thread1.execute(() -> {
                        bearDAO.deleteImage(i);
                        images.remove(p);//remove from our array list
                        //remove from the main UI thread
                        runOnUiThread(() -> {
                            myAdapter.notifyItemRemoved(p);
                        });
                    });
                    Snackbar.make(findViewById(R.id.recyclerView), getString(R.string.deleteBear )+ i.id, Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.undo), click ->{
                                images.add(p, i);
                                myAdapter.notifyItemInserted(p);
                            })
                            .show();
                })
                .create().show();
    }
    /**
     * This class extends the RecyclerView.ViewHolder, which is information of
     * each generated image
     */
    protected class MyRowHolder extends RecyclerView.ViewHolder{
        /**
         * the image size on the row holder
         */
        TextView sizeBtn;
        /**
         * the clear button on the row holder
         */
        ImageButton clearBtn;

        /**
         * The row holder constructor that takes the view as parameter
         * @param itemView the generated image history with a size button and a clear button
         */
        public MyRowHolder(View itemView){
            super(itemView);
            sizeBtn = itemView.findViewById(R.id.sizeBtn);

            //when user clicks the size, which is clickable, the BearImageFragment will show up
            sizeBtn.setOnClickListener( clk ->{
                int position = getAbsoluteAdapterPosition();
                BearImage i = images.get(position);
                FrameLayout fragmentLocation = findViewById( R.id.fragmentLocation);

                boolean location = fragmentLocation != null;
                BearImageFragment BearImageFragment = new BearImageFragment(i, images);
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("")
                        .replace(R.id.fragmentLocation, BearImageFragment)
                        .commit();
            });
            clearBtn = itemView.findViewById(R.id.clearBtn);
            clearBtn.setOnClickListener(clk ->{
                int p = getAbsoluteAdapterPosition();
                deleteImage(p);
            });
        }

    }

    /**
     * When user leaves the application, it stores information of editText in SharedPreference
     */
    @Override
    protected void onPause() {
        super.onPause();
        String widthNum = variableBinding.width.getText().toString();
        String heightNum = variableBinding.height.getText().toString();
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("width", widthNum);
        editor.putString("height", heightNum);
        editor.apply();
    }
}