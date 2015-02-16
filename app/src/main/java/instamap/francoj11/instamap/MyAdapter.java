package instamap.francoj11.instamap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Adapter for the RecyclerView in PhotosActivity
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<PhotoPost> photoList;
    private Context mContext;

    public MyAdapter(ArrayList l, Context c) {
        photoList = l;
        mContext = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PhotoPost pp = photoList.get(position);

        holder.username.setText(pp.getUsername());
        Picasso.with(mContext).load(pp.getUserProfilePicture()).into(holder.profilePic);
        Picasso.with(mContext).load(pp.getImageURL()).placeholder(R.drawable.gray).into(holder.pic);
        //Picasso.with(mContext).load(R.drawable.gray).into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return (null != photoList ? photoList.size() : 0);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profilePic;
        public TextView username;
        public ImageView pic;

        public ViewHolder(View itemView) {
            super(itemView);
            profilePic = (ImageView) itemView.findViewById(R.id.profile_picture);
            username = (TextView) itemView.findViewById(R.id.username);
            pic = (ImageView) itemView.findViewById(R.id.photo);
        }
    }

}
