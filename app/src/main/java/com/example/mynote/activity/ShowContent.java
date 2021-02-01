package com.example.mynote.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynote.R;
import com.example.mynote.db.NoteDb;
import com.example.mynote.richEditor.RichEditor;
import com.example.mynote.util.SDCardUtil;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ShowContent extends BaseActivity {
    private RichEditor mTextview;
    private TextView time;
    private NoteDb mDb;
    private SQLiteDatabase mSql;
    private ImageView ibt_delete;
    private ImageView ibt_save;
    private final String AUTO_SAVE = "auto_save";
    private final String MANUAL_SAVE = "manual_save";
    private int mNoteID;
    private Handler mHandler;
    private Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_content);
        mTextview = findViewById(R.id.showtext);
        mTextview.setBackgroundColor(0);
        time = (TextView) findViewById(R.id.showtime);
        ibt_delete = (ImageView) findViewById(R.id.ibt_delete);
        ibt_save = (ImageView) findViewById(R.id.ibt_save);
        mDb = new NoteDb(this);
        mSql = mDb.getWritableDatabase();
        mTextview.setHtml(getIntent().getStringExtra(NoteDb.CONTENT));
        time.setTextColor(this.getResources().getColor(R.color.colorBlack));
        time.setText(getIntent().getStringExtra(NoteDb.TIME));
        ibt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        ibt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(MANUAL_SAVE);
            }
        });
    }

    public void delete() {
        mNoteID = getIntent().getIntExtra(NoteDb.ID, 0);
        mSql.delete(NoteDb.TABLE_NAME, " _id = " + mNoteID, null);
        finish();

    }

    public void goBack(View v) {
        finish();
    }

    public void save(String type) {
        mNoteID = getIntent().getIntExtra(NoteDb.ID, 0);
        ContentValues cv = new ContentValues();
        cv.put(NoteDb.CONTENT, mTextview.getHtml());
        mSql.update(NoteDb.TABLE_NAME, cv, "_id = " + mNoteID, null);
        if (!type.equals(AUTO_SAVE)) {
            finish();
        }
    }

    public void addPic(View view) {
        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
        getAlbum.setType("image/*");
        startActivityForResult(getAlbum, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                try {
                    Uri uri = data.getData();
                    ContentResolver contentResolver = ShowContent.this.getContentResolver();
                    Bitmap bitmap;
                    bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
                    String imgPath = SDCardUtil.saveMyBitmap(bitmap, System.currentTimeMillis() + "");//System.currentTimeMillis()用来获取时间来对图片文件进行命名
                    Log.e("imgPath", imgPath);
                    mTextview.focusEditor();
                    mTextview.insertImage(imgPath, "sloop");
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
    protected void onResume() {
        super.onResume();
        mHandler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(this, 10000);
                Toast.makeText(ShowContent.this, "自动保存成功", Toast.LENGTH_SHORT).show();
                save(AUTO_SAVE);
            }
        };
        mHandler.postDelayed(runnable, 10000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacks(runnable);
    }
}