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
import android.text.Editable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynote.R;
import com.example.mynote.db.NoteDb;

import java.io.FileNotFoundException;

public class ShowContent extends BaseActivity {
    private EditText mTextview;
    private TextView time;
    private NoteDb mDb;
    private SQLiteDatabase mSql;
    private ImageView ibt_delete;
    private ImageView ibt_save;
    private final String auto_save="AUTO_SAVE";
    private final String manual_save="MANUAL_SAVE";
    private Handler mHandler;
    private Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_content);
        mTextview = (EditText) findViewById(R.id.showtext);
        time = (TextView) findViewById(R.id.showtime);
        ibt_delete = (ImageView) findViewById(R.id.ibt_delete);
        ibt_save = (ImageView)findViewById(R.id.ibt_save);
        mDb = new NoteDb(this);
        mSql = mDb.getWritableDatabase();
        mTextview.setText(getIntent().getStringExtra(NoteDb.CONTENT));
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
                save(manual_save);
            }
        });
    }

    public void delete() {
        int id = getIntent().getIntExtra(NoteDb.ID, 0);
        mSql.delete(NoteDb.TABLE_NAME, " _id = " + id, null);
        finish();

    }

    public void goBack(View v) {
        finish();
    }

    public void save(String type) {
        int id = getIntent().getIntExtra(NoteDb.ID, 0);
        ContentValues cv = new ContentValues();
        cv.put(NoteDb.CONTENT, mTextview.getText().toString());
        mSql.update(NoteDb.TABLE_NAME, cv, "_id = " + id, null);
        if (!type.equals("AUTO_SAVE")){
            finish();
        }
    }
    private void saveImage(String Context,Bitmap bitmap) {
        ContentValues cv = new ContentValues();
        cv.put(NoteDb.CONTENT,Context+bitmap);

    }
    public void addPic(View view) {
        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
        getAlbum.setType("image/*");
        startActivityForResult(getAlbum,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            if(requestCode==1){
                try {
                    Uri uri = data.getData();
                    ContentResolver contentResolver = ShowContent.this.getContentResolver();
                    Bitmap bitmap = null;
                    Bundle extras = null;
                    bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
                    final ImageSpan imageSpan = new ImageSpan(this,bitmap);
                    SpannableString spannableString = new SpannableString("IMG");
                    spannableString.setSpan(imageSpan, 0, spannableString.length(), SpannableString.SPAN_MARK_MARK);
                    //光标移到下一行
                    mTextview.append("\n");
                    Editable editable = mTextview.getEditableText();
                    int selectionIndex = mTextview.getSelectionStart();
                    spannableString.getSpans(0, spannableString.length(), ImageSpan.class);
                    //将图片添加进EditText中
                    editable.insert(selectionIndex, spannableString);
                    //添加图片后自动空出两行
                    mTextview.append("\n");
                    saveImage(mTextview.getText().toString(),bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

//            int imgWidth = bitmap.getWidth();
//            int imgHeight = bitmap.getHeight();
//            double partion = imgWidth*1.0/imgHeight;
//            double sqrtLength = Math.sqrt(partion*partion + 1);
//            //新的缩略图大小
//            double newImgW = 1000*(partion / sqrtLength);
//            double newImgH = 1000*(1 / sqrtLength);
//            float scaleW = (float) (newImgW/imgWidth);
//            float scaleH = (float) (newImgH/imgHeight);
//            Matrix mx = new Matrix();
//            //对原图片进行缩放
//            mx.postScale(scaleW, scaleH);
//            bitmap = Bitmap.createBitmap(bitmap, 0, 0, imgWidth, imgHeight, mx, true);

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
                mHandler.postDelayed(this,60000);
                Toast.makeText(ShowContent.this, "自动保存成功", Toast.LENGTH_SHORT).show();
                save(auto_save);
            }
        };
        mHandler.postDelayed(runnable,60000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacks(runnable);
    }
}