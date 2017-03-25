package clashacks.commandoengineer.clashhackspic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by shilpi on 26/3/17.
 */

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {

    Context context;

    public LibraryAdapter(Context context)
    {
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);

        }
    }

    @Override
    public LibraryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_library, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LibraryAdapter.ViewHolder holder, int position) {
    //Todo change this
       Glide.with(context).load("https://images-assets.nasa.gov/image/PIA00342/PIA00342~thumb.jpg").into(holder.mImageView);
    }

    @Override
    public int getItemCount() {

        //todo change this
        return 5;
    }


}
