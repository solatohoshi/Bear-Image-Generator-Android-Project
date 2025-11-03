package com.example.bearimagegeneratorapp;
import static com.example.bearimagegeneratorapp.MainActivity.bearDAO;
import static com.example.bearimagegeneratorapp.MainActivity.myAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.example.bearimagegeneratorapp.databinding.ShowImageFragmentBinding;


public class BearImageResultFragment extends Fragment {
    Bitmap image;
    BearImage bear;
    RequestQueue queue = null;
    ArrayList<BearImage> images;

    public BearImageResultFragment(BearImage i, ArrayList<BearImage> imgs)
    {
        bear = i;
        images = imgs;
    }
    /**
     * this creates view for this fragment
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return the fragment layout
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        ShowImageFragmentBinding binding = ShowImageFragmentBinding.inflate(inflater, container, false);

        queue = Volley.newRequestQueue(getContext());

        binding.width.setText(getString(R.string.widthText) + bear.width + "px");
        binding.height.setText(getString(R.string.heightText) + bear.height + "px");


        String url = "https://placebear.com/"+ bear.width +"/" +bear.height;

        String iconName = bear.width + "_" + bear.height;
        String pathname = getContext().getFilesDir()+"/"+ iconName + ".png";
        File file = new File(pathname);

        /**
         * if file exists, the image is set to the file saved; if not, it launches an imageRequest
         * using Volley and gets the response Bitmap as image
         */
        if(file.exists()){
            image = BitmapFactory.decodeFile(file.getAbsolutePath());
            getActivity().runOnUiThread(()->{
                binding.bearimage.setImageBitmap(image);
            });
        }else{
            try {
                ImageRequest imgReq = new ImageRequest(url, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        //FileOutputStream fOut = null;
                        getActivity().runOnUiThread(() -> {
                            binding.bearimage.setImageBitmap(bitmap);
                        });
                        image = bitmap;
                        try {
                            image.compress(Bitmap.CompressFormat.PNG, 100, getContext().openFileOutput(iconName + ".png", Activity.MODE_PRIVATE));
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, 1024, 1024, ImageView.ScaleType.CENTER, null,
                        (error) -> {
                            Log.w("onResponse", "In onCreate() - Loading Widgets");
                        });
                queue.add(imgReq);
            } finally {

            }
        }

        binding.savebtn.setOnClickListener( clk ->{

            images.add(bear);

            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(() ->{
                bear.id = bearDAO.insertImage(bear);//add to database;
                /*this runs in another thread*/
            });

            myAdapter.notifyItemInserted(images.size()-1);
            Toast.makeText(getContext(), getString(R.string.beartoast) + bear.width + getString(R.string.beartoast2)+ bear.height, Toast.LENGTH_SHORT).show();
            getFragmentManager().popBackStack();
        });

        return binding.getRoot();
    }
}