package courthaven.pack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {


    public DataBaseHelper(@Nullable Context context) {
        super(context, "baza.db", null, 1);
    }

    //this is called the first time a database is created, there should be code in here to create a new database
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createUserTable = "create table USER (id_user integer primary key autoincrement, name text, email text, password text, isAdmin boolean);";
        String createCourtTable = "create table COURT (id_court integer primary key autoincrement, name text, address text, sport text, id_admin integer, foreign key(id_admin) references USER(id_user));";
        String createBookingTable = "create table BOOKING (id_booking integer primary key autoincrement, ddate text, ttime real, duration real, price real, id_user integer,  id_court integer, foreign key(id_user) references USER(id_user), foreign key(id_court) references COURT(id_court));";


        db.execSQL("pragma foreign_keys = on;");
        db.execSQL(createUserTable);
        db.execSQL(createCourtTable);
        db.execSQL(createBookingTable);
        db.execSQL("begin transaction;");

        for (int i = 1; i < 13; i++){
            String sport = (i % 2 == 0) ? "basketball" : ((i % 3 == 0) ? "football" : "tennis");
            db.execSQL("insert into COURT  values ("+ i +", \"Court "+ i +"\", \"Street "+ i +"\", \""+ sport +"\", 0);");
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
        cv.put("email", user.getEmail());
        cv.put("password", user.getPassword());
        cv.put("isAdmin", user.isAdmin());
        //id autoincrement

        long insert = db.insert("USER", null, cv);

        return insert != -1;
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

    public ArrayList<Court> getCourts(){
        ArrayList<Court> returnList = new ArrayList<>();

        //get data from the database
        String query = "select * from COURT";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            //loop through the cursor (the results) and create new court objects, put them into the return list
            do {
                int id_court = cursor.getInt(0);
                String name = cursor.getString(1);
                String address = cursor.getString(2);
                String sport = cursor.getString(3);
                int id_admin = cursor.getInt(4);

                Court newCourt = new Court(id_court, name, address, sport, id_admin);
                returnList.add(newCourt);
            } while (cursor.moveToNext());

        } else {
            //failiure - do not add anything to the list
        }

        cursor.close();
        db.close();
        return returnList;
    }

}
