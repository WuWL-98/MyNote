package com.example.mynote.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynote.R;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public MainAdapter(Context mContext, Cursor mCursor) {
        this.mContext = mContext;
        this.mCursor = mCursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(mContext).inflate(R.layout.item_main, parent, false);
        ViewHolder myViewHolder = new ViewHolder(inflater);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        mCursor.moveToPosition(position);
        String dbcontext1 = mCursor.getString(mCursor.getColumnIndex("content"));
        String dbcontext = dbcontext1;
        if (dbcontext1.length() >= 18) {
            dbcontext = dbcontext1.substring(0, 18) + "……";
        }
        String dbtime = mCursor.getString(mCursor.getColumnIndex("time"));
        holder.tv_list_content.setText(dbcontext);
        holder.tv_list_time.setText(dbtime);
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickLitener.onItemLongClick(holder.itemView, position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_list_content;
        public TextView tv_list_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_list_content = (TextView) itemView.findViewById(R.id.tv_content);
            tv_list_time = (TextView) itemView.findViewById(R.id.tv_time);
        }

    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
}
