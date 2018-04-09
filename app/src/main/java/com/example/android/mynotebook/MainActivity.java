package com.example.android.mynotebook;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.example.android.mynotebook.Date.NoteContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        CustomCursorAdapter.CustomCursorAdapterOnClickHandler{

    private CustomCursorAdapter mAdapter;
    RecyclerView mRecyclerView;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int NOTE_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleViewNotes);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CustomCursorAdapter(this,this);
        mRecyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int id = (int) viewHolder.itemView.getTag();
                Uri uri = NoteContract.NoteEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(Integer.toString(id)).build();
                getContentResolver().delete(uri,null,null);
                getSupportLoaderManager().restartLoader(NOTE_LOADER_ID, null, MainActivity.this);
            }
        }).attachToRecyclerView(mRecyclerView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNote = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(addNote);
            }
        });
        getSupportLoaderManager().initLoader(NOTE_LOADER_ID,null, MainActivity.this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(NOTE_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mNote = null;
            @Override
            public Cursor loadInBackground() {
                try{
                    return getContentResolver().query(NoteContract.NoteEntry.CONTENT_URI,null,null,null,
                            NoteContract.NoteEntry.COLUMN_PRIOR);

                }catch (Exception e){
                    Log.e(TAG,"Failed to load data");
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onStartLoading() {
               if(mNote != null){
                   deliverResult(mNote);
               }else{
                   forceLoad();
               }
            }

            @Override
            public void deliverResult(Cursor data) {
                mNote = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onClick(int id,String desc, int pos) {
        Intent updateActivityIntent = new Intent(MainActivity.this, UpdateActivity.class);
        updateActivityIntent.putExtra("id", id);
        updateActivityIntent.putExtra("desc", desc);
        updateActivityIntent.putExtra("pos", pos);
        startActivity(updateActivityIntent);
    }
}
