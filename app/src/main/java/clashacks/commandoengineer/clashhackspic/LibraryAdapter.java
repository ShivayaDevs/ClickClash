package clashacks.commandoengineer.clashhackspic;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

/**
 * Created by shilpi on 26/3/17.
 */

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {

    Context context;
    List<String> mImageUrls;

    public LibraryAdapter(Context context, List<String> imageUrls)
    {
        this.context = context;
        mImageUrls = imageUrls;

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
        Glide.with(context).load(mImageUrls.get(position)).into(holder.mImageView);

    }

    @Override
    public int getItemCount() {

        //todo change this
        return mImageUrls.size();
    }


}
