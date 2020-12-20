package com.iceberry.keysave;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.iceberry.keysave.activity.main.MainActivity;
import com.iceberry.keysave.utils.SnackbarUtil;

import org.litepal.crud.DataSupport;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MyItemTouchCallback extends ItemTouchHelper.Callback {
    private KeyAdapter adapter;
    private int undoPosition;
    private BlockingQueue queue = new ArrayBlockingQueue(3);

    public MyItemTouchCallback(KeyAdapter adapter) {
        this.adapter = adapter;
    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int swipeFlag;
        int dragFlag;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        dragFlag = ItemTouchHelper.DOWN | ItemTouchHelper.UP;
        swipeFlag = ItemTouchHelper.START;
        return makeMovementFlags(dragFlag, swipeFlag);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        final Key undoKey = adapter.getDataList().get(position);
        undoPosition = position;
        queue.add(undoKey);
        if (direction == ItemTouchHelper.START) {
            adapter.getDataList().remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, adapter.getItemCount() - position);
        }

    }

    @Override
    public void clearView(@NonNull final RecyclerView recyclerView, @NonNull final RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (!queue.isEmpty()) {
            SnackbarUtil.LongSnackbar(recyclerView, recyclerView.getResources().getString(R.string.mainActivitySnackbar_info),Color.WHITE,Color.rgb(47,48,52)).setAction(R.string.mainActivitySnackbar_action, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Key key = (Key) queue.remove();
                    adapter.notifyItemInserted(undoPosition);
                    adapter.getDataList().add(undoPosition, key);
                    if (undoPosition == 0) {
                        recyclerView.smoothScrollToPosition(0);
                    }
                }
            }).setCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    if (event != DISMISS_EVENT_ACTION) {
                        final Key key = (Key) queue.remove();
                        DataSupport.deleteAll(Key.class, "sort=? and account=? and password=?", key.getSort(), key.getAccount(), key.getPassword());
                    }
                }
            }).setActionTextColor(Color.rgb(102, 153, 255)).show();
            /*
            Snackbar.make(recyclerView,"已删除", Snackbar.LENGTH_LONG).setAction("撤销", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Key key = (Key) queue.remove();
                    adapter.notifyItemInserted(undoPosition);
                    adapter.getDataList().add(undoPosition, key);
                    if (undoPosition == 0) {
                        recyclerView.smoothScrollToPosition(0);
                    }
                }
            }).setCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    if (event != DISMISS_EVENT_ACTION) {
                        final Key key = (Key) queue.remove();
                        DataSupport.deleteAll(Key.class, "sort=? and account=? and password=?", key.getSort(), key.getAccount(), key.getPassword());
                    }
                }
            }).setActionTextColor(Color.rgb(102, 153, 255)).show();

             */
        }
    }
}
