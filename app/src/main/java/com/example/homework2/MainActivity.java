package com.example.homework2;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.homework2.data.TestUtil;
import com.example.homework2.data.WaitlistContract;
import com.example.homework2.data.WaitlistDbHelper;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    RecyclerView recyclerView;
    public GuestListAdapter mAdapter;
//    public SQLiteDatabase mDb;
    WaitlistDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rv_guest_list);

        dbHelper = new WaitlistDbHelper(this);

        ShowRecyclerView();

        setupSharedPreferences();
    }

    private void ShowRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get data from the dbHelper directly to be showed in the activity
        Cursor cursor = dbHelper.getData();

        // Create an adapter for that cursor to display the data
        mAdapter = new GuestListAdapter(this, cursor);

        // Set the ADAPTER for the RV
        recyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                //do nothing, we only care about swiping
                return false;
            }

            //int direction = direction of the swipe
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final RecyclerView.ViewHolder viewHolder1 = viewHolder;

                final AlertDialog delete_dialog = new AlertDialog.Builder(MainActivity.this).create();
                delete_dialog.setTitle("Please Confirm");
                delete_dialog.setMessage("Are you sure you want to delete this?");
                delete_dialog.setButton(AlertDialog.BUTTON_POSITIVE, "DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //get the viewHolder's itemView's tag and store in a long variable id
                        //get the id of the item being swiped
                        long id = (long) viewHolder1.itemView.getTag();

                        //remove the data from the DB
                        dbHelper.removeData(id);

                        //update the adapter
                        mAdapter.swapCursor(dbHelper.getData());
                    }
                });
                delete_dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete_dialog.dismiss();

                        //always update the adapter, although transaction is cancelled
                        mAdapter.swapCursor(dbHelper.getData());
                    }
                });
                delete_dialog.show();
            }
        }).attachToRecyclerView(recyclerView);
    }


    //Don't need this too, can directly get the data from the dbHelper
//    //Taken from waitlist
//    private Cursor getAllGuests() {
//        //Call query on mDb passing in the table name and projection String [] order by COLUMN_TIMESTAMP
//        return mDb.query(
//                WaitlistContract.WaitlistEntry.TABLE_NAME,
//                null,
//                null,
//                null,
//                null,
//                null,
//                WaitlistContract.WaitlistEntry.COLUMN_TIMESTAMP
//        );
//    }

    //Below this is to create MENU -- ADD and SETTINGS
    //Use onCreateOptionsMenu to inflate the Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Handle item selection
        switch (item.getItemId()){
            case R.id.add_list:
                Intent add_intent = new Intent(this, AddActivity.class);
                startActivity(add_intent);
                return true;
            case R.id.settings:
                Intent settings_intent = new Intent(this, SettingsActivity.class);
                startActivity(settings_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }



    private void setupSharedPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadColorFromPreferences(sharedPreferences);
        //Register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_color_pref_key))){
            loadColorFromPreferences(sharedPreferences);
        }
    }

    private void loadColorFromPreferences(SharedPreferences sharedPreferences){
        this.setColor(sharedPreferences.getString(getString(R.string.pref_color_pref_key),
                getString(R.string.pref_color_blue_value)));
    }

    public void setColor(String newColor){
//        TextView partySize_TextView = (TextView) findViewById(R.id.party_size_text_view);

//        partySize_TextView.setBackgroundColor(backGroundColor);
        Drawable unwrappedOval = AppCompatResources.getDrawable(this, R.drawable.circle);
        Drawable wrappedOval = DrawableCompat.wrap(unwrappedOval);

        int backGroundColor = 0;

        if(newColor.equals(this.getString(R.string.pref_color_blue_value))){
            backGroundColor = ContextCompat.getColor(this, R.color.blueSky);
        } else if(newColor.equals(this.getString(R.string.pref_color_red_value))){
            backGroundColor = ContextCompat.getColor(this, R.color.redWine);
        } else if(newColor.equals(this.getString(R.string.pref_color_green_value))){
            backGroundColor = ContextCompat.getColor(this, R.color.greenGrass);
        }

        DrawableCompat.setTint(wrappedOval, backGroundColor);

        mAdapter.swapCursor(dbHelper.getData());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregister the listener
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}
