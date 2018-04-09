package com.example.android.mynotebook;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.android.mynotebook.Date.NoteContract;

public class AddNoteActivity extends AppCompatActivity {
    private int mPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        ((RadioButton) findViewById(R.id.radButton1)).setChecked(true);
        mPriority = 1;
    }

    public void onClickAddTask(View view) {
        String inputText = ((EditText) findViewById(R.id.editTextNoteDesc)).getText().toString();
        if(inputText.length() == 0){
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteContract.NoteEntry.COLUMN_DESCRIPTION,inputText);
        contentValues.put(NoteContract.NoteEntry.COLUMN_PRIOR,mPriority);
        Uri uri = getContentResolver().insert(NoteContract.NoteEntry.CONTENT_URI,contentValues);

        finish();
    }

    public void onPrioritySelected(View view) {
        if(((RadioButton) findViewById(R.id.radButton1)).isChecked()){
            mPriority = 1;
        }else if(((RadioButton) findViewById(R.id.radButton2)).isChecked()){
            mPriority = 2;
        }else if(((RadioButton) findViewById(R.id.radButton3)).isChecked()){
            mPriority = 3;
        }
    }
}
