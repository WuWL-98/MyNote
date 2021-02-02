package com.example.mynote.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynote.R;
import com.example.mynote.adapter.MainAdapter;
import com.example.mynote.db.NoteDb;

public class    MainActivity extends BaseActivity {
    private Intent mIntent;
    private NoteDb mNotedb;
    private Cursor cursor;
    private SQLiteDatabase dbreader;

    private RecyclerView mRvList;
    private MainAdapter mRvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRvList = (RecyclerView) findViewById(R.id.rv_list);

        mNotedb = new NoteDb(this);
        dbreader = mNotedb.getReadableDatabase();

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
        mRvAdapter = new MainAdapter(this,cursor);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRvList.setLayoutManager(linearLayoutManager);
        mRvAdapter.setOnItemClickLitener(new MainAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                cursor.moveToPosition(position);
                Intent intent = new Intent(MainActivity.this, ShowContent.class);
                intent.putExtra(NoteDb.ID, cursor.getInt(cursor.getColumnIndex(NoteDb.ID)));
                intent.putExtra(NoteDb.CONTENT, cursor.getString(cursor.getColumnIndex(NoteDb.CONTENT)));
                intent.putExtra(NoteDb.TIME, cursor.getString(cursor.getColumnIndex(NoteDb.TIME)));
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
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
            }
        });
        mRvList.setAdapter(mRvAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectDb();

    }


}