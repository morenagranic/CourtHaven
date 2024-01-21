package courthaven.pack;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.SharedPreferences;
import android.widget.DatePicker;
import android.widget.Toast;
import android.provider.BaseColumns;
import android.util.Log;


import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Arrays;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(@Nullable Context context) {
        super(context, "baza.db", null, 2);
    }

    //this is called the first time a database is created, there should be code in here to create a new database
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createUserTable = "create table USER (id_user integer primary key autoincrement, name text, description text, email text, password text,courts text, isAdmin boolean);";
        String createCourtTable = "create table COURT (id_court integer primary key autoincrement, name text, address text, sport text, distance integer, id_admin integer, foreign key(id_admin) references USER(id_user));";
        String createBookingTable = "create table BOOKING (id_booking integer primary key autoincrement, cost real, id_user integer,  id_court integer, foreign key(id_user) references USER(id_user), foreign key(id_court) references COURT(id_court));";
        String createDateTimeTable = "create table DATE_TIME (id_dt integer primary key autoincrement, day text, ddate text, ttime int, id_booking integer, foreign key(id_booking) references BOOKING(id_booking));";


        db.execSQL("pragma foreign_keys = on;");
        db.execSQL(createUserTable);
        db.execSQL(createCourtTable);
        db.execSQL(createBookingTable);
        db.execSQL(createDateTimeTable);

        //fill court table
        db.execSQL("begin transaction;");
        for (int i = 1; i < 13; i++){
            String sport = (i % 2 == 0) ? "basketball" : ((i % 3 == 0) ? "football" : "tennis");
            int distance = 10 * i;
            db.execSQL("INSERT INTO COURT VALUES (" + i + ", 'Court " + i + "', 'Street " + i + "', '" + sport + "',  " + distance + ",0);");
        }
        db.execSQL("commit;");


        //fill date_time table
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String ddate = simpleDateFormat.format(calendar.getTime());
        String[] days = new String[] {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        //String day_of_week = "bla";

        db.execSQL("begin transaction;");
        for (int i = 0; i < 14; i++){
            String day_of_week = days[calendar.get(Calendar.DAY_OF_WEEK) - 1];
            for (int j = 7; j < 24; j++) {
                db.execSQL("insert into DATE_TIME(day, ddate, ttime)  values (\"" + day_of_week + "\", \"" + ddate + "\", " + j*100 +");");
                db.execSQL("insert into DATE_TIME(day, ddate, ttime)  values (\"" + day_of_week + "\", \"" + ddate + "\", " + (j*100+30) +");");
            }
            calendar.add(Calendar.DATE, 1);
            ddate = simpleDateFormat.format(calendar.getTime());
        }
        db.execSQL("commit;");
    }


    //this is called if the database version number changes, it prevents previous users apps from breaking when you change the database design
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", user.getName());
        cv.put("description",user.getDescription());
        cv.put("email", user.getEmail());
        cv.put("password", user.getPassword());
        cv.put("courts", user.getCourts());
        cv.put("isAdmin", user.isAdmin());
        //id autoincrement

        long insert = db.insert("USER", null, cv);

        return insert != -1;
    }
    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM USER WHERE id_user = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        User user = null;
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
            @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("email"));
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("password"));
            @SuppressLint("Range") String courts = cursor.getString(cursor.getColumnIndex("courts"));
            @SuppressLint("Range") boolean isAdmin = cursor.getInt(cursor.getColumnIndex("isAdmin")) == 1;

            user = new User(userId, name, description, email, password,courts, isAdmin);
        }

        cursor.close();
        db.close();

        return user;
    }

    public boolean addCourt(Court court){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", court.getName());
        cv.put("address", court.getAddress());
        cv.put("sport", court.getSport());
        cv.put("id_admin", court.getId_admin());
        //id autoincrement

        long insert = db.insert("COURT", null, cv);

        return insert != -1;
    }

    public boolean dataExists(String tableName, String column, String value){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from " + tableName + " where " + column + " = "  + "\"" + value + "\"";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean dataMatches(String tableName, String column1, String value1, String column2, String value2){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from " + tableName + " where " + column1 + " = "  + "\"" + value1 + "\" and " + column2 + " = "  + "\"" + value2 + "\"";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public ArrayList<Court> getCourts() {
        ArrayList<Court> returnList = new ArrayList<>();

        //get data from the database
        String query = "select * from COURT";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            // Loop through the cursor (the results) and create new court objects, put them into the return list
            do {
                @SuppressLint("Range") int id_court = cursor.getInt(cursor.getColumnIndex("id_court"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("address"));
                @SuppressLint("Range") String sport = cursor.getString(cursor.getColumnIndex("sport"));
                @SuppressLint("Range") int id_admin = cursor.getInt(cursor.getColumnIndex("id_admin"));
                // Retrieve the distance from the cursor
                @SuppressLint("Range") int distance = cursor.getInt(cursor.getColumnIndex("distance"));

                // Create a Court object with the distance
                Court newCourt = new Court(id_court, name, address, sport, id_admin,distance);
                // Set the distance in the Court object

                returnList.add(newCourt);
            } while (cursor.moveToNext());

        } else {
            // Failure - do not add anything to the list
        }

        cursor.close();
        db.close();
        return returnList;
    }
    public void updateUserName(String newName, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", newName);

        String whereClause = "id_user=?";
        String[] whereArgs = new String[]{String.valueOf(userId)};

        db.update("USER", values, whereClause, whereArgs);


    }
    private static final String PREFS_NAME = "CourtHavenPrefs";

    // Retrieve user favorites from shared preferences
    public String getUserFavoritesFromPreferences(Context context, int userId) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getString("user_" + userId + "_favorites", "");
    }

    // Update user favorites in shared preferences
    public void updateUserFavoritesInPreferences(Context context, int userId, String updatedFavorites) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_" + userId + "_favorites", updatedFavorites);
        editor.apply();
    }

    public int getUserId(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select id_user from USER where email = ? and password = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        int userId = -1;

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return userId;
    }

    public void updateUserDescription(String newDescription, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("description", newDescription);

        String whereClause = "id_user=?";
        String[] whereArgs = new String[]{String.valueOf(userId)};

        db.update("USER", values, whereClause, whereArgs);


    }

    @SuppressLint("Range")
    public String getUserFavorites(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT courts FROM USER WHERE id_user = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        String userFavorites = "";
        if (cursor.moveToFirst()) {
            userFavorites = cursor.getString(cursor.getColumnIndex("courts"));
        }

        cursor.close();
        db.close();

        return userFavorites;
    }

    // Update user favorites in the database
    public void updateUserFavorites(int userId, String updatedFavorites) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("courts", updatedFavorites);

        String whereClause = "id_user=?";
        String[] whereArgs = {String.valueOf(userId)};

        db.update("USER", values, whereClause, whereArgs);

        db.close();
    }
    public class CourtEntry implements BaseColumns {
        public static final String TABLE_NAME = "court";

        public static final String COLUMN_ID_COURT = "id_court";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_SPORT = "sport";



        // Add other columns as needed
    }
    public ArrayList<Court> getFilteredCourts(String[] selectedSports, int maxDistance) {
        ArrayList<Court> filteredCourts = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {
                "id_court",
                "name",
                "address",
                "sport",
                "distance" // Include the new column
        };

        // Define the WHERE clause based on selected sports and maximum distance
        StringBuilder selection = new StringBuilder();
        List<String> selectionArgsList = new ArrayList<>();

        for (int i = 0; i < selectedSports.length; i++) {
            selection.append("sport = ?");
            selectionArgsList.add(selectedSports[i]);

            if (i < selectedSports.length - 1) {
                selection.append(" OR ");
            }
        }

        if (maxDistance > 0) {
            if (selection.length() > 0) {
                selection.append(" AND ");
            }
            selection.append("distance <= ?");
            selectionArgsList.add(String.valueOf(maxDistance));
        }

        // Convert the list to an array for the query method
        String[] selectionArgs = new String[selectionArgsList.size()];
        selectionArgsList.toArray(selectionArgs);

        Log.d("DataBaseHelper", "SQL query: " + selection.toString());
        Log.d("DataBaseHelper", "Selection Args: " + Arrays.toString(selectionArgs));

        // Query the database
        Cursor cursor = db.query(
                "COURT", // Table name directly used here
                projection,
                selection.toString(),
                selectionArgs,
                null,
                null,
                null
        );

        // Process the cursor and populate the filteredCourts list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Retrieve column indices
                int idIndex = cursor.getColumnIndexOrThrow("id_court");
                int nameIndex = cursor.getColumnIndexOrThrow("name");
                int addressIndex = cursor.getColumnIndexOrThrow("address");
                int sportIndex = cursor.getColumnIndexOrThrow("sport");
                // Add other column indices as needed

                // Retrieve values from the cursor
                int id = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String address = cursor.getString(addressIndex);
                String sport = cursor.getString(sportIndex);
                int distance = cursor.getInt(cursor.getColumnIndexOrThrow("distance")); // Retrieve distance

                // Check if the distance is within the specified maximum distance
                if (maxDistance <= 0 || distance <= maxDistance) {
                    // Create a Court object and add it to the list
                    Court court = new Court(id, name, address, sport, 0, distance); // Include the new distance value
                    filteredCourts.add(court);
                }
            } while (cursor.moveToNext());

            // Close the cursor
            cursor.close();
        }

        // Close the database
        db.close();

        return filteredCourts;
    }

    public ArrayList<String> getTimes(String ddate){
        ArrayList<String> returnList = new ArrayList<>();

        //get data from the database
        String query = "select ttime from DATE_TIME where ddate = \""+ ddate +"\"";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            //loop through the cursor (the results) and put the results into the return list
            do {
                int ttime = cursor.getInt(0);
                String t = (ttime < 1000) ? "0" + (ttime/100) + ":" + (ttime%100)/10 + "0" : (ttime/100) + ":" + (ttime%100)/10 + "0";

                returnList.add(t);
            } while (cursor.moveToNext());

        } else {
            //failure - do not add anything to the list
        }

        cursor.close();
        db.close();
        return returnList;
    }
}