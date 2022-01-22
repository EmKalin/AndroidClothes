package com.example.mytesting;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    @NonNull

    private Context mContext;
    private List<UploadImage> uploadImages;
    private OnItemClickListner mlistner;

    public ImageAdapter(Context contex, List<UploadImage> uploadIm){
        mContext = contex;
        uploadImages = uploadIm;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        UploadImage uploadCurrent = uploadImages.get(position);
        holder.textViewName.setText(uploadCurrent.mName);
        Picasso.with(mContext).load(uploadCurrent.mImageUrl).fit().centerCrop().into(holder.imageViewInAdapter);
    }

    @Override
    public int getItemCount() {
        return uploadImages.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView textViewName;
        public ImageView imageViewInAdapter;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.text_view_name);
            imageViewInAdapter = itemView.findViewById(R.id.image_view_upload);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }


        @Override
        public void onClick(View view) {
            if(mlistner != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    mlistner.onItemClick(position);
                }
            }
        }


        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuItem delete = contextMenu.add(Menu.NONE, 1, 1, "Delete");
            delete.setOnMenuItemClickListener(this);
        }


        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            if(mlistner != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    mlistner.onDeleteClick(position);
                }
            }
            return false;
        }
    }

    public interface OnItemClickListner {
        void onItemClick(int position);


        void onDeleteClick(int position);
    }

    public void setOnItemClickListner(OnItemClickListner listner){
        mlistner = listner;
    }
}
