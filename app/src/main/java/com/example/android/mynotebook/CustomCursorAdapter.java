package com.example.android.mynotebook;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.mynotebook.Date.NoteContract;

/**
 * Created by Denys_d on 3/31/2018.
 */

public class CustomCursorAdapter extends RecyclerView.Adapter<CustomCursorAdapter.NoteViewHolder> {
    private Cursor mCursor;
    private Context mContext;

    final private CustomCursorAdapterOnClickHandler mClickHandler;

    public interface CustomCursorAdapterOnClickHandler{
        void onClick(int id,String descr, int pos);
    }

    public CustomCursorAdapter(Context mContext, CustomCursorAdapterOnClickHandler clickHandler) {
        this.mContext = mContext;
        this.mClickHandler = clickHandler;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.note_layout,parent,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        int indexId = mCursor.getColumnIndex(NoteContract.NoteEntry._ID);
        int indexDescription = mCursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_DESCRIPTION);
        int indexPriority = mCursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_PRIOR);

        mCursor.moveToPosition(position);
        final int id = mCursor.getInt(indexId);
        String description = mCursor.getString(indexDescription);
        int priority = mCursor.getInt(indexPriority);
        String priorityString = "" + priority;
        holder.itemView.setTag(id);

        holder.noteDescription.setText(description);
        holder.notePriority.setText(priorityString);
        GradientDrawable priorityCircle = (GradientDrawable) holder.notePriority.getBackground();
        int priorityColor = getPriorityColor(priority);
        priorityCircle.setColor(priorityColor);
    }

    private int getPriorityColor(int priority) {
        int priorityColor = 0;

        switch(priority) {
            case 1: priorityColor = ContextCompat.getColor(mContext, R.color.materialRed);
                break;
            case 2: priorityColor = ContextCompat.getColor(mContext, R.color.materialOrange);
                break;
            case 3: priorityColor = ContextCompat.getColor(mContext, R.color.materialYellow);
                break;
            default: break;
        }
        return priorityColor;
    }

    @Override
    public int getItemCount() {
        if(mCursor == null){
            return 0;
        }
        return mCursor.getCount();
    }
    public Cursor swapCursor(Cursor c){
        if(mCursor == c){
            return null;
        }
        Cursor tmp = mCursor;
        this.mCursor = c;
        if(c!= null){
            this.notifyDataSetChanged();
        }
        return tmp;
    }

    class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView noteDescription;
        TextView notePriority;

        public NoteViewHolder(View itemView) {
            super(itemView);
            noteDescription = (TextView) itemView.findViewById(R.id.noteDescription);
            notePriority = (TextView) itemView.findViewById(R.id.priorityTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int indexId = mCursor.getColumnIndex(NoteContract.NoteEntry._ID);
            int indexDescription = mCursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_DESCRIPTION);
            int indexPriority = mCursor.getColumnIndex(NoteContract.NoteEntry.COLUMN_PRIOR);
            final int id = mCursor.getInt(indexId);
            String description = mCursor.getString(indexDescription);
            int priority = mCursor.getInt(indexPriority);
            String priorityString = "" + priority;
            mClickHandler.onClick(id, description, priority);
        }
    }
}
