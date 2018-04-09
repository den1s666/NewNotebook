package com.example.android.mynotebook.Date;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Denys_d on 3/31/2018.
 */

public class NoteContract {

    public static final String AUTHORITY = "com.example.android.mynotebook";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_NOTES = "notes";
    public static final String PATH_NOTES_ID = "notes/#";

    public static final class NoteEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_NOTES).build();
        public static final Uri CONTENT_URI2 = BASE_CONTENT_URI.buildUpon().appendPath(PATH_NOTES_ID).build();
        public static final String TABLE_NAME = "notes";

        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_PRIOR = "priority";

        public static Uri buildNoteUriWithId(int id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }
}
