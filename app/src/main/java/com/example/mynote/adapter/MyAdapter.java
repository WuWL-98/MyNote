package com.example.mynote.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mynote.R;


public class MyAdapter extends BaseAdapter {
    private Context mContext;
    private Cursor mCursor;
    private LinearLayout mLayout;
    public MyAdapter(Context mContext,Cursor mCursor) {
        this.mContext = mContext;
        this.mCursor = mCursor;
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return mCursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mLayout = (LinearLayout) inflater.inflate(R.layout.item,null);
        TextView content = (TextView) mLayout.findViewById(R.id.tv_content);
        TextView time = (TextView) mLayout.findViewById(R.id.tv_time);
        mCursor.moveToPosition(position);
        String dbcontext1 = mCursor.getString(mCursor.getColumnIndex("content"));
        //如果字符串长度超过18，那么取前18，再加一个省略号，如果不超过18，则用原始字符串
        String dbcontext = dbcontext1;
        if (dbcontext1.length()>=18){
            dbcontext=dbcontext1.substring(0,18)+"……";
        }
        String dbtime = mCursor.getString(mCursor.getColumnIndex("time"));
        content.setText(dbcontext);
        time.setText(dbtime);
        return mLayout;
    }
}