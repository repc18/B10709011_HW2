package com.example.homework2;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homework2.data.WaitlistContract;

public class GuestListAdapter extends RecyclerView.Adapter<GuestListAdapter.GuestListViewHolder> {

    private Context mContext;
    //mCursor stores the count of items to be displayed in the recycler view
    private Cursor mCursor;

    public GuestListAdapter(Context context, Cursor cursor){
        this.mContext = context;
        this.mCursor = cursor;
//        this.mCount = count;
    }
    @NonNull
    //onCreateViewHolder asks the Adapter to create viewHolders for the data
    //correspond to the number of items needed
    //This also inflate the layout created earlier
    @Override
    public GuestListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.guest_list, parent, false);
        return new GuestListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuestListViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position))return;

        //Get the name of the guest and also the party size
        String name = mCursor.getString(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME));
        int partySize = mCursor.getInt(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE));

//        Retrieve the id from the cursor
        long id = mCursor.getLong(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry._ID));

        //Binding it with the itemView -- guestName and partySize
        holder.guestName_TextView.setText(name);
        holder.partySize_TextView.setText(String.valueOf(partySize));

//        Set the tag of the itemview in the holder to the id
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    //This is for the content of the ViewHolder
    class GuestListViewHolder extends RecyclerView.ViewHolder{
        TextView partySize_TextView;
        TextView guestName_TextView;

        public GuestListViewHolder(@NonNull View itemView) {
            super(itemView);
            partySize_TextView = (TextView) itemView.findViewById(R.id.party_size_text_view);
            guestName_TextView = (TextView) itemView.findViewById(R.id.name_text_view);
        }
    }

    private Context getContext() {
        return mContext;
    }

    //used to keep the view if deleting is cancelled
    public void swapCursor(Cursor newCursor){
        //check if the current cursor is not null, and close it if so
        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();

        //Update the local mCursor to be equal to  newCursor
        mCursor = newCursor;

        if(newCursor != null){
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    public void refreshCursor(Cursor cursor){
        mCursor = cursor;
    }
    public void setColor(String newColor){
//        TextView partySize_TextView = GuestListViewHolder.
//        Drawable unwrappedOval = AppCompatResources.getDrawable(this.getContext(), R.drawable.circle);
//        Drawable wrappedOval = DrawableCompat.wrap(unwrappedOval);
//
//        int backGroundColor = 0;
//
//        if(newColor.equals(getContext().getString(R.string.pref_color_blue_value))){
//            backGroundColor = ContextCompat.getColor(getContext(), R.color.blueSky);
//        } else if(newColor.equals(getContext().getString(R.string.pref_color_red_value))){
//            backGroundColor = ContextCompat.getColor(getContext(), R.color.redWine);
//        } else if(newColor.equals(getContext().getString(R.string.pref_color_green_value))){
//            backGroundColor = ContextCompat.getColor(getContext(), R.color.greenGrass);
//        }
//
//        DrawableCompat.setTint(wrappedOval, backGroundColor);
    }
}
