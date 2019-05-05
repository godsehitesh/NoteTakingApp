package com.example.notesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.io.IOException;

public class NoteActivity extends AppCompatActivity {

    String finalText;
    EditText editText;
    String operation;
    int notePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        editText = (EditText)findViewById(R.id.editText);

        Intent intent = getIntent();
        if(intent.hasExtra("existingNote"))
        {
            editText.setText(intent.getStringExtra("existingNote"));
        }
        if (intent.hasExtra("operation"))
        {
            operation = intent.getStringExtra("operation");
            Log.d("NA", "operation received:"+operation);
        }
        if (intent.hasExtra("notePosition"))
        {
            notePosition = intent.getIntExtra("notePosition", -1);
        }

        editText.setSelection(editText.getText().length());

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if(notePosition==-1)
                    {
                        MainActivity.notes.add(String.valueOf(s));
                        notePosition = MainActivity.notes.size()-1;
                    }
                    else{
                        MainActivity.notes.set(notePosition, String.valueOf(s));
                    }
                    MainActivity.arrayAdapter.notifyDataSetChanged();

                try {
                    MainActivity.sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(MainActivity.notes)).apply();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void finish() {
        super.finish();

        finalText = String.valueOf(editText.getText());

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("finalText", finalText);

        Log.d("NA", "operation being sent:"+ operation);
        intent.putExtra("operation", operation);
        intent.putExtra("notePosition", notePosition);
        startActivity(intent);

    }
}
