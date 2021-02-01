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
    private Handler mHandler;
    private Runnable runnable;
//    private String imgPath = "IMG";
//    private String res = null;
//    private int index = 1;


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
//        mTextview.setInputEnabled(false);
        time.setTextColor(this.getResources().getColor(R.color.colorBlack));
        time.setText(getIntent().getStringExtra(NoteDb.TIME));
//        Glide.with(ShowContent.this).load(getIntent().getStringExtra(NoteDb.imagePath)).into(iv_image);
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
        int id = getIntent().getIntExtra(NoteDb.ID, 0);
        mSql.delete(NoteDb.TABLE_NAME, " _id = " + id, null);
        finish();

    }

    public void goBack(View v) {
        finish();
    }

//    private String getEditData() {
//        StringBuilder content = new StringBuilder();
//        try {
//            List<RichTextEditor.EditData> editList = mTextview.buildEditData();
//            for (RichTextEditor.EditData itemData : editList) {
//                if (itemData.inputStr != null) {
//                    content.append(itemData.inputStr);
//                } else if (itemData.imagePath != null) {
//                    content.append("<img src=\"").append(itemData.imagePath).append("\"/>");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return content.toString();
//    }


    public void save(String type) {
        int id = getIntent().getIntExtra(NoteDb.ID, 0);
        ContentValues cv = new ContentValues();
        cv.put(NoteDb.CONTENT, mTextview.getHtml());
        mSql.update(NoteDb.TABLE_NAME, cv, "_id = " + id, null);
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
                    Bundle extras = null;
                    bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
                    /*int imgWidth = bitmap.getWidth();
                    int imgHeight = bitmap.getHeight();
                    double partion = imgWidth * 1.0 / imgHeight;
                    double sqrtLength = Math.sqrt(partion * partion + 1);
                    //新的缩略图大小
                    double newImgW = 1000 * (partion / sqrtLength);
                    double newImgH = 1000 * (1 / sqrtLength);
                    float scaleW = (float) (newImgW / imgWidth);
                    float scaleH = (float) (newImgH / imgHeight);
                    Matrix mx = new Matrix();
                    //对原图片进行缩放
                    mx.postScale(scaleW, scaleH);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, imgWidth, imgHeight, mx, true);*/
//                    iv_image.setImageBitmap(bitmap);
                    //String imgPath = SDCardUtil.saveToSdCard(bitmap);
                    String imgPath = SDCardUtil.saveMyBitmap(bitmap, System.currentTimeMillis() + "");
                    //Glide.with(AddContent.this).load(imgPath).into(iv_image);
                    Log.e("imgPath", imgPath);
                    mTextview.focusEditor();
                    mTextview.insertImage(imgPath, "sloop");
                    //final ImageSpan imageSpan = new ImageSpan(this, bitmap);
                    /*  String imgPath = SDCardUtil.saveToSdCard(bitmap);
                     *//*SpannableString spannableString = new SpannableString("<img src=" + imgPath + " />");
                    spannableString.setSpan(imageSpan, 0, spannableString.length(), SpannableString.SPAN_MARK_MARK);
                    //光标移到下一行
                    mTextview.append("\n");
                    Editable editable = mTextview.getEditableText();
                    int selectionIndex = mTextview.getSelectionStart();
                    spannableString.getSpans(0, spannableString.length(), ImageSpan.class);
                    //将图片添加进EditText中
                    editable.insert(selectionIndex, spannableString);
                    //添加图片后自动空出两行
                    mTextview.append("\n");*//*

                     */
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