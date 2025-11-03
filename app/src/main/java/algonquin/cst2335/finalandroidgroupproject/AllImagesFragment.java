package algonquin.cst2335.finalandroidgroupproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import algonquin.cst2335.finalandroidgroupproject.databinding.AllImagesBinding;
import algonquin.cst2335.finalandroidgroupproject.databinding.ImageForAllImagesBinding;

/**
 * this fragment shows all generated bear images by reading the images from files and
 * set image bitmap
 */
public class AllImagesFragment extends Fragment {
    ArrayList<BearImage> images;
    static RecyclerView.Adapter<MyRowHolder> imageAdapter;
    Bitmap singleImage;

    /**
     * AllImagesFragment that takes array list of images as parameter
     * @param images
     */
    public AllImagesFragment(ArrayList<BearImage> images){
        this.images = images;
    }

    /**
     * OnCreateView for AllBearImageFragment
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return layout for AllImagesFragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        AllImagesBinding allImagesBinding = AllImagesBinding.inflate(inflater, container, false);

        Log.d("images fragment", "something wrong.");

        /**
         * adapter setting for recyclerview
         */
        imageAdapter = new RecyclerView.Adapter<MyRowHolder>() {

            /**
             * This returns the row holder layout
             * @param parent   The ViewGroup into which the new View will be added after it is bound to
             *                 an adapter position.
             * @param viewType The view type of the new View.
             * @return MyRowHolder row holder in this fragment
             */
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                ImageForAllImagesBinding binding = ImageForAllImagesBinding.inflate(getLayoutInflater(), parent, false);
                return new MyRowHolder(binding.getRoot());
            }

            /**
             * This changes the UI when the view holder is bound
             * @param holder   The ViewHolder which should be updated to represent the contents of the
             *                 item at the given position in the data set.
             * @param position The position of the item within the adapter's data set.
             */
            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                BearImage obj = images.get(position);
                holder.width.setText(getString(R.string.widthText) + obj.width + "px");
                holder.height.setText(getString(R.string.heightText) + obj.height + "px");
                holder.time.setText(getString(R.string.timeText) + obj.timeGenerated);
                String iconName = obj.width + "_" + obj.height;
                String pathName = getContext().getFilesDir()+"/" + iconName + ".png";
                singleImage = BitmapFactory.decodeFile(pathName);
                holder.singleImage.setImageBitmap(singleImage);
            }

            /**
             * get item count
             * @return images size
             */
            @Override
            public int getItemCount() {
                return images.size();
            }
        };
        allImagesBinding.imageRecyclerView.setAdapter(imageAdapter);
        allImagesBinding.imageRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return allImagesBinding.getRoot();
    }

    /**
     * RowHolder for AllImagesFragment
     */
    protected class MyRowHolder extends RecyclerView.ViewHolder{
        ImageView singleImage;
        TextView width;
        TextView height;
        TextView time;

        /**
         * MyRowHolder constructor taking item view as parameter
         * @param itemView the item view of the RowHolder, which is the ImageForAllImage
         */
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener( clk -> {
                int position = getAbsoluteAdapterPosition();
                BearImage i = images.get(position);
                FrameLayout fragmentLocation = itemView.findViewById( R.id.fragmentLocation);

                boolean IAmTablet = fragmentLocation != null;
                BearImageFragment BearImageFragment = new BearImageFragment(i, images);
                //BearImageFragment.setMyAdapter(imageAdapter);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("")
                        .replace(R.id.fragmentLocation, BearImageFragment)
                        .commit();
            });
            singleImage = itemView.findViewById(R.id.imageRow);
            width = itemView.findViewById(R.id.width);
            height = itemView.findViewById(R.id.height);
            time = itemView.findViewById(R.id.time);
        }
    }
}
