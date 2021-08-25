package com.mobdeve.s14.group24.everyday;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;

public class MediaEntryAdapter extends RecyclerView.Adapter<MediaEntryViewHolder> {

    private ArrayList<MediaEntry> mediaEntries;

    public MediaEntryAdapter (ArrayList<MediaEntry> mediaEntries) {
        this.mediaEntries = mediaEntries;
    }

    @NonNull
    @NotNull
    @Override
    public MediaEntryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_media, parent, false);
        MediaEntryViewHolder mediaEntryViewHolder = new MediaEntryViewHolder(view);

        mediaEntryViewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentUserID = mediaEntryViewHolder.getAdapterPosition();
                Intent intent = new Intent(v.getContext(), ViewMediaEntryActivity.class);
                //intent.putExtra();
                v.getContext().startActivity(intent);
            }
        });

        return mediaEntryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MediaEntryViewHolder holder, int position) {
        MediaEntry mediaEntry = mediaEntries.get(position);


        holder.setTvDate(mediaEntry.getDate().toStringNoYear());

        String path = mediaEntry.getImagePath().toString();
        String ext = path.contains(".") ? path.substring(path.lastIndexOf(".")).toLowerCase() : "";
        if (ext.equals(".jpeg") || ext.equals(".jpg"))
            holder.setIvImage(mediaEntry.getImagePath());
        else
            holder.setIvImage(ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND));

        holder.setIvMoodRating(mediaEntry.getMoodRating());
    }

    @Override
    public int getItemCount() {
        return this.mediaEntries.size();
    }
}
