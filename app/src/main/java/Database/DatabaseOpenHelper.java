package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import Models.Travel;
import Utilities.UtilityFunctions;
import Utilities.Variables;

/**
 * Created by shafi on 7/17/2017.
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();

    private final ArrayList<Travel>   travelList = new ArrayList<>();
    Context context;


    public DatabaseOpenHelper(Context context){
        super (context, Variables.DATABASE_NAME, null, Variables.DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
            String CREATE_TRAVEL_TABLE = "CREATE TABLE " + Variables.TRAVEL_LIST_TABLE_NAME + "(" + Variables.TRAVEL_TABLE_ID + " INTEGER PRIMARY KEY, " + Variables.TRAVEL_TABLE_TITLE +
                    " TEXT, " + Variables.TRAVEL_TABLE_LOCACTION + " TEXT, " + Variables.TRAVEL_TABLE_DATE_ADDED + " TEXT, " + Variables.TRAVEL_TABLE_DURATION + " INT, " +
                    Variables.TRAVEL_TABLE_RATING + " INT);";

            String CREATE_DESCRIPTION_TABLE = "CREATE TABLE " + Variables.DESCRIPTION_TABLE_NAME + "(" + Variables.DESCRIPTION_TABLE_ID + " INTEGER PRIMARY KEY, " +
                    Variables.DESCRIPTION_TABLE_TRAVEL_ID + " INT, " + Variables.DESCRIPTION_TABLE_DESCRIPTION + " TEXT);";

            String CREATE_TAG_TABLE = "CREATE TABLE " + Variables.TAG_TABLE_NAME + "(" + Variables.TAG_TABLE_ID + " INTEGER PRIMARY KEY, " + Variables.TAG_TABLE_TRAVEL_ID + " INT, "
                    + Variables.TAG_TABLE_TAG + " TEXT);";

            db.execSQL(CREATE_TRAVEL_TABLE);
            db.execSQL(CREATE_DESCRIPTION_TABLE);
            db.execSQL(CREATE_TAG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop all the tables.
        String drop_table_statement = "DROP TABLE IF EXISTS ";
        db.execSQL(drop_table_statement + Variables.TRAVEL_LIST_TABLE_NAME);
        db.execSQL(drop_table_statement + Variables.DESCRIPTION_TABLE_NAME);
        db.execSQL(drop_table_statement + Variables.TAG_TABLE_NAME);

        //now create the new one xD
        onCreate(db);
    }

    public int getTotalTravelsNumber (){
        int totalTravels = 0;

        String query = "SELECT * FROM " + Variables.TRAVEL_LIST_TABLE_NAME;
        SQLiteDatabase dbs = this.getReadableDatabase();
        Cursor cursor =dbs.rawQuery(query, null);

        totalTravels = cursor.getCount();
        cursor.close();
        dbs.close();

        return totalTravels;
    }

    public void addTravel(Travel travel){

        SQLiteDatabase dbs = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //Values for travel table
        values.put(Variables.TRAVEL_TABLE_TITLE, travel.getTitle());
        values.put(Variables.TRAVEL_TABLE_DURATION, travel.getDuration());
        values.put(Variables.TRAVEL_TABLE_RATING, travel.getRating());
        values.put(Variables.TRAVEL_TABLE_DATE_ADDED, travel.getDateAdded());
        values.put(Variables.TRAVEL_TABLE_LOCACTION, travel.getLocation());

        //inserting the values in the travel table
        dbs.insert(Variables.TRAVEL_LIST_TABLE_NAME, null, values);

        Log.v("Added travel...", "Done.");

        //Getting the travel ID
        SQLiteDatabase dbR = this.getReadableDatabase();
        String getTravelIdQuery = "SELECT " + Variables.TRAVEL_TABLE_ID + " FROM " + Variables.TRAVEL_LIST_TABLE_NAME + " WHERE " + Variables.TRAVEL_TABLE_TITLE  + " = '" + travel.getTitle().trim() + "' AND " + Variables.TRAVEL_TABLE_DATE_ADDED  + " = '" + travel.getDateAdded().trim() + "'";
        Log.v ("The query: " , getTravelIdQuery);
        Cursor cursor = dbR.rawQuery(getTravelIdQuery, null);
        Log.v ("The query: " , getTravelIdQuery);
        cursor.moveToFirst();

        int travel_id = cursor.getInt(cursor.getColumnIndex(Variables.TRAVEL_TABLE_ID));

        travel.setDatabaseID(travel_id);

        if (currentUser != null){
            FirebaseHandler fb = new FirebaseHandler();
            fb.addOnline(travel);
        }

        //Values for description table
        values.clear();
        values.put(Variables.DESCRIPTION_TABLE_TRAVEL_ID, travel_id);
        values.put(Variables.DESCRIPTION_TABLE_DESCRIPTION, travel.getDescription());
        dbs.insert(Variables.DESCRIPTION_TABLE_NAME, null, values);
        Log.v("Added description..", "Success");


        //Values for tag table.
        ArrayList<String> tagList = travel.getTags();
        for (String tag : tagList){
            values.clear();
            values.put(Variables.TAG_TABLE_TRAVEL_ID, travel_id);
            values.put(Variables.TAG_TABLE_TAG, tag);
            dbs.insert(Variables.TAG_TABLE_NAME, null, values);
        }
        Log.v("Added tag..", "Success");

        dbR.close();
        dbs.close();
    }
    public ArrayList<Travel> getAllTravels(){
        ArrayList<Travel> travels = new ArrayList<>();

        SQLiteDatabase dbs = this.getReadableDatabase();
        Cursor travelCursor = dbs.rawQuery("SELECT * FROM " + Variables.TRAVEL_LIST_TABLE_NAME, null);
        travelCursor.moveToFirst();
            while (!travelCursor.isAfterLast()) {
                int travel_id = travelCursor.getInt(travelCursor.getColumnIndex(Variables.TRAVEL_TABLE_ID));
                String get_description = "SELECT * FROM description_table WHERE travel_id = '" + travel_id + "'";
                Cursor descriptionCursor = dbs.rawQuery(get_description, null);
                Log.v("Query gen: ", get_description);
                Log.v("cursor size: ", String.valueOf(descriptionCursor.getCount()));
                descriptionCursor.moveToFirst();
                String description_text = descriptionCursor.getString(descriptionCursor.getColumnIndex(Variables.DESCRIPTION_TABLE_DESCRIPTION));
//            String description_text = "Not found";
                int rating = travelCursor.getInt(travelCursor.getColumnIndex(Variables.TRAVEL_TABLE_RATING));
                String travel_title = travelCursor.getString(travelCursor.getColumnIndex(Variables.TRAVEL_TABLE_TITLE));
                String travel_date_added = travelCursor.getString(travelCursor.getColumnIndex(Variables.TRAVEL_TABLE_DATE_ADDED));
                int travel_duration = travelCursor.getInt(travelCursor.getColumnIndex(Variables.TRAVEL_TABLE_DURATION));
                String travel_location = travelCursor.getString(travelCursor.getColumnIndex(Variables.TRAVEL_TABLE_LOCACTION));

                //getting tags.
                String get_tags = "SELECT * FROM " + Variables.TAG_TABLE_NAME + " WHERE " + Variables.TAG_TABLE_TRAVEL_ID + " = '" + travel_id + "'";
                Cursor tagCursor = dbs.rawQuery(get_tags, null);
                tagCursor.moveToFirst();
                ArrayList<String> travel_tags = new ArrayList<>();
                while (!tagCursor.isAfterLast()) {
                    travel_tags.add(tagCursor.getString(tagCursor.getColumnIndex(Variables.TAG_TABLE_TAG)));
                    tagCursor.moveToNext();
                }

                Travel travel = new Travel();
                travel.setDatabaseID(travel_id);
                travel.setDateAdded(travel_date_added);
                travel.setDescription(description_text);
                travel.setDuration(travel_duration);
                travel.setRating(rating);
                travel.setTags(travel_tags);
                travel.setTitle(travel_title);
                travel.setLocation(travel_location);

                travels.add(travel);
                descriptionCursor.close();
                tagCursor.close();
                travelCursor.moveToNext();
            }

        travelCursor.close();
        dbs.close();
        return travels;
    }

    public void deleteTravel(Travel travel){
        int travel_id = travel.getDatabaseID();
        SQLiteDatabase dbs = this.getWritableDatabase();
        dbs.delete(Variables.TRAVEL_LIST_TABLE_NAME, Variables.TRAVEL_TABLE_ID + " = ? ", new String[] {String.valueOf(travel_id)});
        dbs.delete(Variables.DESCRIPTION_TABLE_NAME, Variables.DESCRIPTION_TABLE_TRAVEL_ID + " = ? ", new String[] {String.valueOf(travel_id)});
        dbs.delete(Variables.TAG_TABLE_NAME, Variables.TAG_TABLE_TRAVEL_ID + " = ? ", new String[] {String.valueOf(travel_id)});

        dbs.close();
    }

    public ArrayList<String> getAllLocation (){
        ArrayList<String> places = new ArrayList<>();

        SQLiteDatabase dbs = this.getReadableDatabase();
        String getLocations = "SELECT " + Variables.TRAVEL_TABLE_LOCACTION  + " FROM " + Variables.TRAVEL_LIST_TABLE_NAME;
        Cursor locationCursor = dbs.rawQuery(getLocations, null);
        locationCursor.moveToFirst();
        do{
            places.add(locationCursor.getString(locationCursor.getColumnIndex(Variables.TRAVEL_TABLE_LOCACTION)));
        }while (locationCursor.moveToNext());

        locationCursor.close();
        dbs.close();

        return places;
    }

    public ArrayList<String> getAllTags(){

        SQLiteDatabase dbs = this.getReadableDatabase();
        Set<String> tagSet = new HashSet<>();
        String query = "SELECT * FROM " + Variables.TAG_TABLE_NAME;
        Cursor cursor = dbs.rawQuery(query, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            tagSet.add(cursor.getString(cursor.getColumnIndex(Variables.TAG_TABLE_TAG)));
            cursor.moveToNext();
        }
        cursor.close();
        dbs.close();

      ArrayList<String> tags = new ArrayList<>(tagSet);
        return tags;
    }

    public ArrayList<Travel> getLookbackStories(){
        ArrayList<Travel> lookBacks = new ArrayList<>();

        SQLiteDatabase dbs = this.getReadableDatabase();

        //Getting the date properly to compare.. :P :P
        DateFormat formatDate = new SimpleDateFormat("dd/MM/");
        Date today = new Date();
        String dayLike = formatDate.format(today).toString();


        String query = "SELECT * FROM " + Variables.TRAVEL_LIST_TABLE_NAME + " WHERE " + Variables.TRAVEL_TABLE_DATE_ADDED + " LIKE '" + dayLike + "%'";

        Cursor travelCursor = dbs.rawQuery(query, null);
        travelCursor.moveToFirst();

        while (!travelCursor.isAfterLast()){

            int travel_id = travelCursor.getInt(travelCursor.getColumnIndex(Variables.TRAVEL_TABLE_ID));
            String get_description = "SELECT * FROM description_table WHERE travel_id = '" + travel_id + "'";
            Cursor descriptionCursor = dbs.rawQuery(get_description, null);
            Log.v("Query gen: ", get_description);
            Log.v("cursor size: ", String.valueOf(descriptionCursor.getCount()));
            descriptionCursor.moveToFirst();
            String description_text = descriptionCursor.getString(descriptionCursor.getColumnIndex(Variables.DESCRIPTION_TABLE_DESCRIPTION));
//            String description_text = "Not found";
            int rating = travelCursor.getInt(travelCursor.getColumnIndex(Variables.TRAVEL_TABLE_RATING));
            String travel_title = travelCursor.getString(travelCursor.getColumnIndex(Variables.TRAVEL_TABLE_TITLE));
            String travel_date_added = travelCursor.getString(travelCursor.getColumnIndex(Variables.TRAVEL_TABLE_DATE_ADDED));
            int travel_duration = travelCursor.getInt(travelCursor.getColumnIndex(Variables.TRAVEL_TABLE_DURATION));
            String travel_location = travelCursor.getString(travelCursor.getColumnIndex(Variables.TRAVEL_TABLE_LOCACTION));

            //getting tags.
            String get_tags = "SELECT * FROM " + Variables.TAG_TABLE_NAME + " WHERE " + Variables.TAG_TABLE_TRAVEL_ID + " = '" + travel_id + "'";
            Cursor tagCursor = dbs.rawQuery(get_tags, null);
            tagCursor.moveToFirst();
            ArrayList<String> travel_tags = new ArrayList<>();
            while (!tagCursor.isAfterLast()) {
                travel_tags.add(tagCursor.getString(tagCursor.getColumnIndex(Variables.TAG_TABLE_TAG)));
                tagCursor.moveToNext();
            }

            Travel travel = new Travel();
            travel.setDatabaseID(travel_id);
            travel.setDateAdded(travel_date_added);
            travel.setDescription(description_text);
            travel.setDuration(travel_duration);
            travel.setRating(rating);
            travel.setTags(travel_tags);
            travel.setTitle(travel_title);
            travel.setLocation(travel_location);

            lookBacks.add(travel);
            descriptionCursor.close();
            tagCursor.close();
            travelCursor.moveToNext();
        }

        return lookBacks;
    }

    public void updateTravel(Travel travel){
        int dbId = travel.getDatabaseID();

        SQLiteDatabase dbs = this.getWritableDatabase();

        //update Statements
        String updateTravelTable = "UPDATE " + Variables.TRAVEL_LIST_TABLE_NAME + " SET " + Variables.TRAVEL_TABLE_TITLE + " = '" + travel.getTitle() +
                "', " + Variables.TRAVEL_TABLE_LOCACTION + " = '" + travel.getLocation() + "', " + Variables.TRAVEL_TABLE_RATING + " = '" + travel.getRating() +
                "', " + Variables.TRAVEL_TABLE_DURATION + " = '" + travel.getDuration() + "' WHERE " + Variables.TRAVEL_TABLE_ID  + " = '" + travel.getDatabaseID() + "'";

        Cursor cursor = dbs.rawQuery(updateTravelTable, null);
        System.out.println ("Travel Table UPDATEd");

        String updateDescription = "UPDATE " + Variables.DESCRIPTION_TABLE_NAME + " SET " + Variables.DESCRIPTION_TABLE_DESCRIPTION + " = '" + travel.getDescription()
                + "' WHERE " + Variables.DESCRIPTION_TABLE_TRAVEL_ID + "= '" + travel.getDatabaseID() + "'";
        cursor = dbs.rawQuery(updateDescription, null);
        System.out.println("Description updated.");

    }

    public ArrayList<Travel> getTaggedTravels(String tag){
        ArrayList<Travel> travels = new ArrayList<>();

        SQLiteDatabase dbs = this.getReadableDatabase();
        Cursor cursor = dbs.rawQuery("SELECT * FROM " + Variables.TAG_TABLE_NAME + " WHERE " + Variables.TAG_TABLE_TAG + " ='" + tag + "'", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int travel_id_cursor = cursor.getInt(cursor.getColumnIndex(Variables.TAG_TABLE_TRAVEL_ID));

            String travelTableQuery = "SELECT * FROM " + Variables.TRAVEL_LIST_TABLE_NAME + " WHERE " + Variables.TRAVEL_TABLE_ID + " ='" + travel_id_cursor + "'";

            Cursor travelCursor = dbs.rawQuery(travelTableQuery, null);
            travelCursor.moveToFirst();
            while (!travelCursor.isAfterLast()) {
                int travel_id = travelCursor.getInt(travelCursor.getColumnIndex(Variables.TRAVEL_TABLE_ID));
                String get_description = "SELECT * FROM description_table WHERE travel_id = '" + travel_id + "'";
                Cursor descriptionCursor = dbs.rawQuery(get_description, null);
                Log.v("Query gen: ", get_description);
                Log.v("cursor size: ", String.valueOf(descriptionCursor.getCount()));
                descriptionCursor.moveToFirst();
                String description_text = descriptionCursor.getString(descriptionCursor.getColumnIndex(Variables.DESCRIPTION_TABLE_DESCRIPTION));
//            String description_text = "Not found";
                int rating = travelCursor.getInt(travelCursor.getColumnIndex(Variables.TRAVEL_TABLE_RATING));
                String travel_title = travelCursor.getString(travelCursor.getColumnIndex(Variables.TRAVEL_TABLE_TITLE));
                String travel_date_added = travelCursor.getString(travelCursor.getColumnIndex(Variables.TRAVEL_TABLE_DATE_ADDED));
                int travel_duration = travelCursor.getInt(travelCursor.getColumnIndex(Variables.TRAVEL_TABLE_DURATION));
                String travel_location = travelCursor.getString(travelCursor.getColumnIndex(Variables.TRAVEL_TABLE_LOCACTION));

                //getting tags.
                String get_tags = "SELECT * FROM " + Variables.TAG_TABLE_NAME + " WHERE " + Variables.TAG_TABLE_TRAVEL_ID + " = '" + travel_id + "'";
                Cursor tagCursor = dbs.rawQuery(get_tags, null);
                tagCursor.moveToFirst();
                ArrayList<String> travel_tags = new ArrayList<>();
                while (!tagCursor.isAfterLast()) {
                    travel_tags.add(tagCursor.getString(tagCursor.getColumnIndex(Variables.TAG_TABLE_TAG)));
                    tagCursor.moveToNext();
                }

                Travel travel = new Travel();
                travel.setDatabaseID(travel_id);
                travel.setDateAdded(travel_date_added);
                travel.setDescription(description_text);
                travel.setDuration(travel_duration);
                travel.setRating(rating);
                travel.setTags(travel_tags);
                travel.setTitle(travel_title);
                travel.setLocation(travel_location);

                travels.add(travel);
                descriptionCursor.close();
                tagCursor.close();
                travelCursor.moveToNext();
            }
            cursor.moveToNext();
        }

        return  new UtilityFunctions().getUniqueTravels(travels);
    }

    public void deleteRecords(){
        String deleteTravel = "DELETE FROM " + Variables.TRAVEL_LIST_TABLE_NAME;
        String deleteTags = "DELETE FROM " + Variables.TAG_TABLE_NAME;
        String deleteDescriptions = "DELETE FROM " + Variables.DESCRIPTION_TABLE_NAME;

        SQLiteDatabase dbs = this.getWritableDatabase();
        dbs.rawQuery(deleteTravel, null);
        dbs.rawQuery(deleteTags, null);
        dbs.rawQuery(deleteDescriptions, null);
    }
}
