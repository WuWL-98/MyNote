package com.example.mynote.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mynote.R;
import com.example.mynote.adapter.MyAdapter;
import com.example.mynote.db.NoteDb;

public class MainActivity extends BaseActivity {
    private ListView mList;
    private Intent mIntent;
    private MyAdapter mAdapter;
    private NoteDb mNotedb;
    private Cursor cursor;
    private SQLiteDatabase dbreader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList = (ListView) findViewById(R.id.list);
        mNotedb = new NoteDb(this);
        dbreader = mNotedb.getReadableDatabase();
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cursor.moveToPosition(i);
                Intent intent = new Intent(MainActivity.this, ShowContent.class);
                intent.putExtra(NoteDb.ID, cursor.getInt(cursor.getColumnIndex(NoteDb.ID)));
                intent.putExtra(NoteDb.CONTENT, cursor.getString(cursor.getColumnIndex(NoteDb.CONTENT)));
                intent.putExtra(NoteDb.imagePath, cursor.getString(cursor.getColumnIndex(NoteDb.imagePath)));
                intent.putExtra(NoteDb.TIME, cursor.getString(cursor.getColumnIndex(NoteDb.TIME)));
                startActivity(intent);
            }
        });
        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("您确认删除此项目吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbreader.delete(NoteDb.TABLE_NAME, NoteDb.ID + "=" + cursor.getInt(cursor.getColumnIndex(NoteDb.ID)), null);
                                selectDb();
                            }
                        })
                        .setNegativeButton("取消",null)
                        .create()
                        .show();
                return true;
            }
        });
    }


    public void add(View v) {
        mIntent = new Intent(MainActivity.this, AddContent.class);
        startActivity(mIntent);
    }

    public void search(View view) {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    public void selectDb() {
        //重新检索数据
        cursor = dbreader.query
                (NoteDb.TABLE_NAME, null, null, null, null, null, null);
        mAdapter = new MyAdapter(this, cursor);
        mList.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectDb();
    }


}