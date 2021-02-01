package com.example.mynote.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.mynote.R;
import com.example.mynote.db.NoteDb;
import com.example.mynote.richEditor.RichEditor;
import com.example.mynote.util.PermissionsUtil;
import com.example.mynote.util.SDCardUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddContent extends BaseActivity implements PermissionsUtil.IPermissionsCallback {
    private RichEditor contentTv;
    private NoteDb mDb;
    private SQLiteDatabase mSqldb;
    private String imagePath = "";
    private PermissionsUtil permissionsUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);
        contentTv = this.findViewById(R.id.showtext);
        contentTv.setBackgroundColor(0);
        mDb = new NoteDb(this);
        mSqldb = mDb.getWritableDatabase();
    }

    public void save(View v) {
        DbAdd();
        finish();
    }

    public void goBack(View v) {
        finish();
    }

    public void DbAdd() {
        ContentValues cv = new ContentValues();
        cv.put(NoteDb.CONTENT, contentTv.getHtml());
        cv.put(NoteDb.imagePath, imagePath);
        cv.put(NoteDb.TIME, getTime());
        mSqldb.insert(NoteDb.TABLE_NAME, null, cv);
    }

    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        String str = sdf.format(date);
        return str;
    }

    public void addPic(View view) {
        permissionsUtil = PermissionsUtil
                .with(this)
                .requestCode(2)
                .isDebug(true)//开启log
                .permissions(
                        PermissionsUtil.Permission.Storage.READ_EXTERNAL_STORAGE,
                        PermissionsUtil.Permission.Storage.WRITE_EXTERNAL_STORAGE
                )
                .request();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //需要调用onRequestPermissionsResult
        permissionsUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        permissionsUtil.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                try {
                    Uri uri = data.getData();
                    ContentResolver contentResolver = AddContent.this.getContentResolver();
                    Bitmap bitmap;
                    bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
                    String imgPath = SDCardUtil.saveMyBitmap(bitmap, System.currentTimeMillis() + "");
                    Log.e("imgPath", imgPath);
                    imagePath = imgPath;
                    contentTv.focusEditor();
                    contentTv.insertImage(imagePath, "sloop");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPermissionsGranted(int requestCode, String... permission) {

        if (requestCode == 2) {
            Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
            getAlbum.setType("image/*");
            startActivityForResult(getAlbum, 1);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, String... permission) {

    }
}