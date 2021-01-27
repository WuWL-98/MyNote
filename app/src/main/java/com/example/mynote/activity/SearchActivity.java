package com.example.mynote.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.example.mynote.R;
import com.example.mynote.adapter.MyAdapter;
import com.example.mynote.db.NoteDb;

public class SearchActivity extends BaseActivity {
    private EditText mEtSearch;
    private ListView mList;
    private Cursor cursor;
    private MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mEtSearch = (EditText)findViewById(R.id.et_search);
        mList = findViewById(R.id.list);

        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("textchange","beforeTextChange");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("textchange","onTextChanged");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("textchange","afterTextChanged");
                search();
            }
        });

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cursor.moveToPosition(i);
                Intent intent = new Intent(SearchActivity.this, ShowContent.class);
                intent.putExtra(NoteDb.ID, cursor.getInt(cursor.getColumnIndex(NoteDb.ID)));
                intent.putExtra(NoteDb.CONTENT, cursor.getString(cursor.getColumnIndex(NoteDb.CONTENT)));
                intent.putExtra(NoteDb.TIME, cursor.getString(cursor.getColumnIndex(NoteDb.TIME)));
                startActivityForResult(intent,2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2){
            search();
        }
    }

    private void search() {
        String findText = mEtSearch.getText().toString().trim();
        if (!findText.isEmpty()){
            NoteDb mNotedb = new NoteDb(this);
            SQLiteDatabase sql = mNotedb.getReadableDatabase();
            cursor = sql.rawQuery("SELECT * FROM "+NoteDb.TABLE_NAME+" WHERE "+NoteDb.CONTENT+" LIKE '%"+findText+"%'",null);
            myAdapter = new MyAdapter(this,cursor);
            mList.setAdapter(myAdapter);
        }else {
            mList.setAdapter(null);
        }
    }

    //返回按钮点击事件
    public void goBack(View view) {
        finish();
    }
    //清除按钮点击事件
    public void clean(View view) {
        mEtSearch.setText("");
    }

}
