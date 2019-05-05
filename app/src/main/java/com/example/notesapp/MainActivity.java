package com.example.notesapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayAdapter arrayAdapter;
    ListView listView;
    static ArrayList<String> notes;
    static SharedPreferences sharedPreferences;
    int posToBeDeleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listView);
        notes = new ArrayList<String>();

        sharedPreferences = this.getSharedPreferences("com.example.notesapp", Context.MODE_PRIVATE);

        if(sharedPreferences.contains("notes"))
        {
            try {
                notes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("notes", ObjectSerializer.serialize(new ArrayList<String>())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*
        Intent intent = getIntent();
        if(intent.hasExtra("finalText"))
        {
            if(intent.hasExtra("operation")) {

                if((intent.getStringExtra("operation")).equalsIgnoreCase("edit")) {

                    if (!((intent.getStringExtra("finalText")).equalsIgnoreCase(""))) {
                        notes.set(intent.getIntExtra("notePosition", 0), intent.getStringExtra("finalText"));
                        try {
                            sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(notes)).apply();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    if (!((intent.getStringExtra("finalText")).equalsIgnoreCase(""))) {
                        notes.add(intent.getStringExtra("finalText"));
                        try {
                            sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(notes)).apply();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        */

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                intent.putExtra("existingNote", notes.get(position));
                intent.putExtra("operation", "edit");
                intent.putExtra("notePosition", position);

                startActivity(intent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {

                Log.v("long clicked","pos: " + pos);
                posToBeDeleted = pos;

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delete Note")
                        .setMessage("Do you want to delete the note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                notes.remove(posToBeDeleted);
                                try {
                                    sharedPreferences.edit().putString("notes", ObjectSerializer.serialize(notes)).apply();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                listView.setAdapter(arrayAdapter);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);


        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch(item.getItemId()){

            case R.id.addnewnote:
                Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                intent.putExtra("operation", "new");
                intent.putExtra("notePosition", -1);
                startActivity(intent);
                return true;


            default:
                return false;
        }
    }
}
