package com.mobdeve.s14.group24.everyday;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ShareActionProvider;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MediaEntryAdapter extends RecyclerView.Adapter<MediaEntryViewHolder> {

    public static final String KEY_DATE = "KEY_DATE";
    public static final String KEY_IMAGE_PATH = "KEY_IMAGE_PATH";
    public static final String KEY_CAPTION = "";
    public static final String KEY_LIKES = "";

    private ArrayList<MediaEntry> mediaEntries;

    //Constructor for the adapter that accepts and stores mediaEntries arraylist value to instance variable
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
        //Sets onClick for every media entry item in recycler view to redirect on view media entry activity
        mediaEntryViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialize intent for view media entry activity
                Intent intent = new Intent(v.getContext(), ViewMediaEntryActivity.class);

                MediaEntry mediaEntry = mediaEntries.get(mediaEntryViewHolder.getBindingAdapterPosition());
                //Adds value for intent to be loaded and passed to view media entry activity
                intent.putExtra(Keys.KEY_ID.name(), mediaEntry.getId());
                intent.putExtra(Keys.KEY_DATE.name(), mediaEntry.getDate().toStringFull());
                intent.putExtra(Keys.KEY_IMAGE_PATH.name(), mediaEntry.getImagePath());
                intent.putExtra(Keys.KEY_CAPTION.name(), mediaEntry.getCaption());
                intent.putExtra(Keys.KEY_MOOD.name(), mediaEntry.getMood());
                PreferenceManager.getDefaultSharedPreferences(v.getContext().getApplicationContext())
                    .edit()
                    .putInt(Keys.CUR_DATA_SET_POS.name(), mediaEntryViewHolder.getBindingAdapterPosition())
                    .commit();

                v.getContext().startActivity(intent);
            }
        });

        return mediaEntryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MediaEntryViewHolder holder, int position) {
        MediaEntry mediaEntry = mediaEntries.get(position);
        //Sets values for the date, image, and mood on the media entry on current position
        holder.setTvDate(mediaEntry.getDate().toStringNoYear());
        holder.setIvImage(mediaEntry.getImagePath());
        holder.setIvMood(mediaEntry.getMood());
    }

    //Returns the total number of items in the ArrayList mediaEntries instance variable
    @Override
    public int getItemCount() {
        return mediaEntries.size();
    }

    //Clears the current instance var arraylist media entries of value and completely replace with the values in the parameter, then notify changes in data set
    public void setData(ArrayList<MediaEntry> mediaEntries) {
        this.mediaEntries.clear();
        this.mediaEntries.addAll(mediaEntries);
        notifyDataSetChanged();
    }
}
