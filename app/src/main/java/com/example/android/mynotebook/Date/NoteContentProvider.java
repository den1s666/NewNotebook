package com.example.android.mynotebook.Date;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Denys_d on 3/31/2018.
 */

public class NoteContentProvider extends ContentProvider {
    public static final int NOTES = 100;
    public static final int NOTE_WITH_ID = 101;

    private NoteDBHelper mDbHelper;
    private static final UriMatcher mUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(NoteContract.AUTHORITY, NoteContract.PATH_NOTES,NOTES);
        uriMatcher.addURI(NoteContract.AUTHORITY,NoteContract.PATH_NOTES + "/#", NOTE_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
       Context context = getContext();
       mDbHelper = new NoteDBHelper(context);
       return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int match = mUriMatcher.match(uri);
        Cursor retCursor;
        switch (match){
            case NOTES:{
                retCursor = db.query(NoteContract.NoteEntry.TABLE_NAME,projection,selection,
                        selectionArgs,null,null,sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri" + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Uri retUri;
        int match = mUriMatcher.match(uri);

        switch (match){
            case NOTES:{
                long id = db.insert(NoteContract.NoteEntry.TABLE_NAME,null,contentValues);
                if(id > 0){
                    retUri = ContentUris.withAppendedId(NoteContract.NoteEntry.CONTENT_URI,id);
                }
                else{
                    throw new android.database.SQLException("failed to insert " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("unknown uri" + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return retUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int retVal;

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = mUriMatcher.match(uri);

        switch (match){
            case NOTE_WITH_ID:{
                String path = uri.getLastPathSegment();
                retVal = db.delete(NoteContract.NoteEntry.TABLE_NAME," _id = ?", new String[]{path});
                break;
            }
            default:
                throw new UnsupportedOperationException("unknown uri" + uri);
        }
            if(retVal!=0){
            getContext().getContentResolver().notifyChange(uri,null);
            }
        return retVal;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int retVal;

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = mUriMatcher.match(uri);

        switch (match){
            case NOTES:{

                retVal = db.update(NoteContract.NoteEntry.TABLE_NAME,contentValues,s,strings);
                break;
            }
            default:
                throw new UnsupportedOperationException("unknown uri" + uri);
        }
        if(retVal!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return retVal;

    }
}
