package com.example.android.mynotebook;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.android.mynotebook.Date.NoteContract;

public class UpdateActivity extends AppCompatActivity {
    private int mPriority;
    private int mId;
    private EditText mUpdateText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        mUpdateText = (EditText) findViewById(R.id.updateTextNoteDesc);
        if(getIntent().hasExtra("pos")){
            mPriority = getIntent().getIntExtra("pos",3);
        }
        if (getIntent().hasExtra("desc")){
            mUpdateText.setText(getIntent().getStringExtra("desc"));
        }
        if(getIntent().hasExtra("id")){
            mId =  getIntent().getIntExtra("id",0);
        }
        if(mPriority == 1){
            ((RadioButton) findViewById(R.id.radButtonN1)).setChecked(true);
        }
        else if(mPriority == 2){
            ((RadioButton) findViewById(R.id.radButtonN2)).setChecked(true);
        }
        else if (mPriority == 3){
            ((RadioButton) findViewById(R.id.radButtonN3)).setChecked(true);
        }
    }
    public void onClickAddNewTask(View view) {
        String inputText = ((EditText) findViewById(R.id.updateTextNoteDesc)).getText().toString();
        if(inputText.length() == 0){
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteContract.NoteEntry.COLUMN_DESCRIPTION,inputText);
        contentValues.put(NoteContract.NoteEntry.COLUMN_PRIOR,mPriority);
        String stringid = "" + mId;
        int ret = getContentResolver().update(NoteContract.NoteEntry.CONTENT_URI,contentValues," _id = ?",new String[]{stringid});
        finish();
    }

    public void onNewPrioritySelected(View view) {
        if(((RadioButton) findViewById(R.id.radButtonN1)).isChecked()){
            mPriority = 1;
        }else if(((RadioButton) findViewById(R.id.radButtonN2)).isChecked()){
            mPriority = 2;
        }else if(((RadioButton) findViewById(R.id.radButtonN3)).isChecked()){
            mPriority = 3;
        }
    }
}
