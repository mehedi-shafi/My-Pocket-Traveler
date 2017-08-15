package Utilities;

/**
 * Created by shafi on 7/17/2017.
 */

public class Variables {

    public static final String DATABASE_NAME = "pocket_travel";
    public static final int DATABASE_VERSION = 1;

    //travel table
    public static final String TRAVEL_LIST_TABLE_NAME = "travel_table";
    public static final String TRAVEL_TABLE_ID = "_id";
    public static final String TRAVEL_TABLE_TITLE = "title";
    public static final String TRAVEL_TABLE_LOCACTION = "location";
    public static final String TRAVEL_TABLE_DATE_ADDED = "date_added";
    public static final String TRAVEL_TABLE_DURATION = "duration";
    public static final String TRAVEL_TABLE_RATING = "rating";

    //Description table
    public static final String DESCRIPTION_TABLE_NAME = "description_table";
    public static final String DESCRIPTION_TABLE_ID= "_id";
    public static final String DESCRIPTION_TABLE_TRAVEL_ID = "travel_id";
    public static final String DESCRIPTION_TABLE_DESCRIPTION = "description";

    //Tag table
    public static final String TAG_TABLE_NAME = "tag_table";
    public static final String TAG_TABLE_ID = "_id";
    public static final String TAG_TABLE_TRAVEL_ID = "travel_id";
    public static final String TAG_TABLE_TAG = "tag";


}
