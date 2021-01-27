package com.example.mynote.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.mynote.R;
import com.example.mynote.db.NoteDb;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddContent extends BaseActivity {
    private EditText mEt;
    private NoteDb mDb;
    private SQLiteDatabase mSqldb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);
        mEt = (EditText) this.findViewById(R.id.et_content);
        mDb = new NoteDb(this);
        mSqldb = mDb.getWritableDatabase();
    }
    public void save(View v) {
        DbAdd();
        finish();
    }
    public void goBack(View v) {
        mEt.setText("");
        finish();
    }
    public void DbAdd() {
        ContentValues cv = new ContentValues();
        cv.put(NoteDb.CONTENT,mEt.getText().toString());
        cv.put(NoteDb.TIME,getTime());
        mSqldb.insert(NoteDb.TABLE_NAME,null,cv);
    }
    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        String str = sdf.format(date);
        return str;
    }
}