package com.apprentice.firstandroidapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import layout.BlueFragment;
import layout.RedFragment;

public class MyActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.apprentice.firstandroidapp.MESSAGE";
    public final static String PREFERENCE_FILE_NAME = "preference_file_name";
    public final static String FILE_NAME = "file_name";
    public final static String PREFERENCE_MESSAGE_KEY = "preference_message_key";
    public Bundle savedInstanceState;
    public boolean isBlueFragmentAdded;
    Fragment newFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_my);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    /**
     * Called when the user clicks the Send button
     */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        intent.putExtra(EXTRA_MESSAGE, editText.getText().toString());
        startActivity(intent);
    }

    ///region fragment
    public void addFragment(View view) {
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            newFragment = new BlueFragment();

            // Add the fragment to the 'fragment_container' FrameLayout
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
            isBlueFragmentAdded = true;
            newFragment = new RedFragment();
        }
    }

    public void replaceFragment(View view) {
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (isBlueFragmentAdded) {
                newFragment = new RedFragment();
                isBlueFragmentAdded = false;
            } else {
                newFragment = new BlueFragment();
                isBlueFragmentAdded = true;
            }
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public void removeFragment(View view) {
        if (findViewById(R.id.fragment_container) != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().remove(newFragment);
            transaction.commit();
        }
    }
    ///endregion

    ///region Save data
    public String getKeyValuePairs(String filename, String key)
    {
        String value =  getSharedPreferences(filename, Context.MODE_PRIVATE).getString(key, "");
        return value;
    }
    public void saveKeyValuePairs(String filename, String key, String value)
    {
        getSharedPreferences(filename, Context.MODE_PRIVATE).edit().putString(key, value).commit();
    }
    public String getInternalFileData()
    {
        FileInputStream inputStream;
        String value = "";
        try
        {
            inputStream = openFileInput(FILE_NAME);
            byte[] bytes = new byte[1000];
            inputStream.read(bytes);
            value = new String(bytes);
            inputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return value;
    }
    public void saveInternalFileData(String value)
    {
        FileOutputStream outStream;
        try
        {
            outStream = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            outStream.write(((EditText)findViewById(R.id.edit_message)).getText().toString().getBytes());
            outStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    ///endregion
    @Override
    public void onPause() {
        super.onPause();

    }
    @Override
    public void onStop()
    {
        super.onStop();
        //saveKeyValuePairs(PREFERENCE_FILE_NAME, PREFERENCE_MESSAGE_KEY, ((EditText)findViewById(R.id.edit_message)).getText().toString());
        saveInternalFileData(((EditText)findViewById(R.id.edit_message)).getText().toString());
    }
    @Override
    public void onStart() {
        super.onStart();
        EditText editText = (EditText) findViewById(R.id.edit_message);
        //editText.setText(getKeyValuePairs(PREFERENCE_FILE_NAME, PREFERENCE_MESSAGE_KEY));
        editText.setText(getInternalFileData());
    }

}
