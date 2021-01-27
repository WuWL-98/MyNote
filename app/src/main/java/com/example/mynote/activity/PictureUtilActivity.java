package com.example.mynote.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.Nullable;

import java.io.FileNotFoundException;

public class PictureUtilActivity extends Activity {
    public void addPic(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    private void startActivityForResult(Intent intent, int resultCode, @Nullable Intent data) {
        if (resultCode==RESULT_OK){
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();

            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
