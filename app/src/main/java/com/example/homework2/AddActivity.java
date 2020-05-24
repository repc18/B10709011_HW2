package com.example.homework2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.homework2.data.WaitlistContract;
import com.example.homework2.data.WaitlistDbHelper;

public class AddActivity extends AppCompatActivity {

    private EditText mNewGuestNameEditText;
    private EditText mNewPartySizeEditText;

    private Button mEnter;
    private Button mCancel;

    //No need
//    public SQLiteDatabase mDb;
//    public GuestListAdapter mAdapter;

    WaitlistDbHelper dbHelper;

    private final static String LOG_TAG = AddActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mNewGuestNameEditText = (EditText) this.findViewById(R.id.person_name_edit_text);
        mNewPartySizeEditText = (EditText) this.findViewById(R.id.party_count_edit_text);

        mEnter = (Button) this.findViewById(R.id.enter_button);
        mCancel = (Button) this.findViewById(R.id.cancel_button);

        dbHelper = new WaitlistDbHelper(this);

        mEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //First thing, check if any of the EditTexts are empty, return if so
                if (mNewGuestNameEditText.getText().length() == 0 ||
                        mNewPartySizeEditText.getText().length() == 0) {
                    return;
                }

                String guestName = mNewGuestNameEditText.getText().toString();
                int partySize = 1;
                try {
                    partySize = Integer.parseInt(mNewPartySizeEditText.getText().toString());
                } catch (NumberFormatException ex){
                    Log.e(LOG_TAG, "Failed to parse party size text to number: " + ex.getMessage());
                }

                addNewGuest(guestName, partySize);
                Log.d("Add Data", "Add successful");

                mNewGuestNameEditText.getText().clear();
                mNewPartySizeEditText.getText().clear();
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void addNewGuest(String name, int partySize){
        boolean insertData = dbHelper.addData(name, partySize);

        if(insertData){
            toastMessage("Add Success");
        } else {
            toastMessage("Add Failed");
        }
//        //create a ContentValues instance to pass the values onto the insert query
//        ContentValues cv = new ContentValues();
//        //call put to insert the name value with the key COLUMN_GUEST_NAME
//        cv.put(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME, name);
//        //call put to insert the party size value with the key COLUMN_PARTY_SIZE
//        cv.put(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE, partySize);
//
//        return mDb.insert(WaitlistContract.WaitlistEntry.TABLE_NAME, null, cv);
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

//    public void addToWaitlist(View view){
//        //First thing, check if any of the EditTexts are empty, return if so
//        if (mNewGuestNameEditText.getText().length() == 0 ||
//                mNewPartySizeEditText.getText().length() == 0) {
//            return;
//        }
//
//        //Create an integer to store the party size and initialize to 1
//        //Default party size to 1
//        int partySize = 1;
//
//        //Use Integer.parseInt to parse mNewPartySizeEditText.getText to an integer
//        try {
//            partySize = Integer.parseInt(mNewPartySizeEditText.getText().toString());
//        } catch (NumberFormatException ex){
//            Log.e(LOG_TAG, "Failed to parse party size text to number: " + ex.getMessage());
//        }
//
//        addNewGuest(mNewGuestNameEditText.getText().toString(), partySize);
//
//        mAdapter.swapCursor(getAllGuests());
//
//        mNewPartySizeEditText.clearFocus();
//        mNewGuestNameEditText.getText().clear();
//        mNewPartySizeEditText.getText().clear();
//    }

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
}
