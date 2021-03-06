package com.mobdeve.s14.group24.everyday;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;

public class MediaEntryViewHolder extends RecyclerView.ViewHolder {

    private TextView tvDate;
    private ImageView ivImage;
    private ImageView ivMood;

    //Constructor for view holder
    public MediaEntryViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        ivMood = itemView.findViewById(R.id.iv_item_media_mood);
        tvDate = itemView.findViewById(R.id.tv_item_media_date);
        ivImage = itemView.findViewById(R.id.iv_item_media_image);
    }

    public void setTvDate(String date) {
        tvDate.setText(date);
    }

    public void setIvImage(String imagePath) {
        String ext = imagePath.contains(".") ? imagePath.substring(imagePath.lastIndexOf(".")).toLowerCase() : "";

        Bitmap bitmap = null;

        if (ext.equals(".jpeg") || ext.equals(".jpg")) {
            try {
                File file = new File(imagePath);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, options);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            bitmap = ThumbnailUtils.createVideoThumbnail(imagePath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
        }

        //Scales down image resource (bitmap)
        int height = (int) (bitmap.getHeight() * (240.0 / bitmap.getWidth()));
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 240, height, true);

        ivImage.setImageBitmap(scaledBitmap);
    }

    public void setIvImage(Bitmap image) {
        ivImage.setImageBitmap(image);
    }

    public void setIvMood(int mood) {
        if (mood == 1) {
            ivMood.setImageResource(R.drawable.mood_border_1);
        } else if (mood == 2) {
            ivMood.setImageResource(R.drawable.mood_border_2);
        } else if (mood == 3) {
            ivMood.setImageResource(R.drawable.mood_border_3);
        } else if (mood == 4) {
            ivMood.setImageResource(R.drawable.mood_border_4);
        } else if (mood == 5) {
            ivMood.setImageResource(R.drawable.mood_border_5);
        } else {
            ivMood.setImageResource(R.drawable.mood_border_0);
        }
    }
}



