package com.iceberry.keysave;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.iceberry.keysave.activity.main.ViewKeyActivity;

import java.util.List;

public class KeyAdapter extends RecyclerView.Adapter<KeyAdapter.ViewHolder> {
    private Context mContext;
    private List<Key> mKeyList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView keyImage;
        TextView sortName;
        TextView account;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            keyImage = (ImageView) view.findViewById(R.id.item_image);
            sortName = (TextView) view.findViewById(R.id.item_sort);
            account = (TextView) view.findViewById(R.id.item_account);
        }
    }


    public KeyAdapter(List<Key> keyList) {
        mKeyList = keyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
        final KeyAdapter.ViewHolder holder = new KeyAdapter.ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Key key = mKeyList.get(position);
                //holder.cardView.setCardBackgroundColor(Color.BLUE);
                Intent intent = new Intent(mContext, ViewKeyActivity.class);
                intent.putExtra(ViewKeyActivity.KEY_IMAGE, key.getIconPath());
                intent.putExtra(ViewKeyActivity.KEY_SORT, key.getSort());
                intent.putExtra(ViewKeyActivity.KEY_ACCOUNT, key.getAccount());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Key key = mKeyList.get(position);
        holder.sortName.setText(key.getSort());
        holder.account.setText(key.getAccount());
        /*
        if (key.getIconPath()==null){
            Glide.with(mContext).load(R.drawable.ic_header_default).into(holder.keyImage);
        }else {
            Glide.with(mContext).load(key.getIconPath()).into(holder.keyImage);
        }

         */
        Glide.with(mContext).load(key.getIconPath()).error(R.drawable.ic_header_default).into(holder.keyImage);
    }

    @Override
    public int getItemCount() {
        return mKeyList.size();
        //return mKeyList.size();
    }

    public List<Key> getDataList() {
        return mKeyList;
    }

    /*
    public void setEmptyView(View emptyView){
        this.emptyView=emptyView;
    }

     */

}
