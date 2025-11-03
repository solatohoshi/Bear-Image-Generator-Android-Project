package algonquin.cst2335.finalandroidgroupproject;

import static algonquin.cst2335.finalandroidgroupproject.AllImagesFragment.imageAdapter;
import static algonquin.cst2335.finalandroidgroupproject.BearImageGeneratorActivity.myAdapter;
import static algonquin.cst2335.finalandroidgroupproject.BearImageGeneratorActivity.bearDAO;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalandroidgroupproject.databinding.BearImageFragmentBinding;

/**
 * This fragment shows a single image
 */
public class BearImageFragment extends Fragment {

    BearImage i;
    ArrayList<BearImage> images;

    /**
     * BearImageFragment that takes an image history as parameter
     * @param ii the image history object
     */
    public BearImageFragment(BearImage ii, ArrayList<BearImage> imgs)
    {
        i = ii;
        images = imgs;
    }
    Bitmap bearImage;

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

        BearImageFragmentBinding binding = BearImageFragmentBinding.inflate(inflater, container, false);

        binding.width.setText(getString(R.string.widthText) + i.width + "px");
        binding.height.setText(getString(R.string.heightText)+i.height + "px");

        int position = images.indexOf(i);
        Activity currentActivity = getActivity();
        binding.deletebtn.setOnClickListener( clk ->{
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(R.string.deleteMsgBear)
                    .setTitle(R.string.currency_question_title)
                    .setNegativeButton(R.string.currency_no, (dialog, cl) -> {
                    })
                    .setPositiveButton(R.string.currency_yes, (dialog, cl) -> {
                        Executor thread1 = Executors.newSingleThreadExecutor();
                        thread1.execute(() -> {
                            // Delete image from the database
                            bearDAO.deleteImage(i);

                            // Remove image from the ArrayList
                            if (position != -1) {
                                images.remove(position);
                            }

                            // Remove image from the RecyclerView on the main UI thread
                            getActivity().runOnUiThread(() -> {
                                myAdapter.notifyItemRemoved(position);
                                imageAdapter.notifyItemRemoved(position);
                            });
                        });
                        getFragmentManager().popBackStack();
                        Snackbar.make(requireView(), getString(R.string.deleteBear)+ i.id, Snackbar.LENGTH_LONG)
                                .setAction(getString(R.string.currency_undo), click -> {
                                    // Undo the deletion by adding the image back to the list
                                        Executor thread = Executors.newSingleThreadExecutor();
                                        thread.execute(() -> {
                                        bearDAO.insertImage(i);
                                            if (position != -1) {
                                                images.add(position, i);
                                            }
                                            currentActivity.runOnUiThread(() ->{
                                                myAdapter.notifyItemInserted(position);
                                                imageAdapter.notifyItemInserted(position);
                                            });
                                            });
                                })
                                .show();
                    })
                    .create().show();
        });


        String iconName = i.width + "_" + i.height;
        String pathname = getContext().getFilesDir()+"/"+ iconName + ".png";

        bearImage = BitmapFactory.decodeFile(pathname);
        binding.imageFragment.setImageBitmap(bearImage);

        return binding.getRoot();
    }
}
