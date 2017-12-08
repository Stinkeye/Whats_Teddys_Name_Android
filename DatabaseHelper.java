package com.soc.matthewhaynes.sqliteapp; //this is different depending on the PATH to your project. remember to CLEAN -> REBUILD when downloading

/**********************************  CUT BELOW HERE TO PASTE *****************************************************/

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthew.haynes on 10/14/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "myDb.db"; //declare name of database
    public static final String TABLE_NAME = "SOCtable";  //declare name of table in database
    public static final String TABLE_SCHEDULE = "schedTable";
    public static final String COL_1 = "Name";           //declare column name
    public static final String COL_2 = "Description";          //declare column name
    public static final String COL_3 = "tag1";        //declare column name
    public static final String COL_4 = "tag2";           //declare column name



    /* CONSTRUCTOR */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1); //database created when constructor is called
        //context.deleteDatabase(DATABASE_NAME); //deletes the database
    }

    @Override
    public void onCreate(SQLiteDatabase db) {//creates table on creation of object, takes the db as a parameter
        db.execSQL("create table " + TABLE_NAME +" (DEPT TEXT,CLASS TEXT,SECTION TEXT,TIME TEXT)"); //create DB ROW names and data type
        db.execSQL("create table " + TABLE_SCHEDULE +" (ID TEXT, SUBJECT TEXT, CLASS TEXT, SECTION TEXT)");
    }

    /* not sure, methinks it upgrades to new versions of database */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME );
        onCreate(db);
    }

    /* Insert data into the DB */
    public boolean insertData(String name, String descr, String tag1, String tag2) {
        SQLiteDatabase db = this.getWritableDatabase();    //declare db we want to alter
        ContentValues contentValues = new ContentValues(); //ContentValues creates and EMPTY SET of values ..below fills empty set
        contentValues.put(COL_1,name);           //insert data into db column 1
        contentValues.put(COL_2,descr);         //insert data into db column 2
        contentValues.put(COL_3,tag1);        //insert data into db column 3
        contentValues.put(COL_4,tag2);           //insert data into db column 4
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }




    /* A query to get all the data */
    public Cursor getAllData(String tablename) {   //a Cursor object points to a SINGLE row of the result fetched by a db query ..so we return Cursor object 'res'
        SQLiteDatabase db = this.getWritableDatabase();             //declare db we want to alter
        Cursor res = db.rawQuery("select * from "+ tablename,null); //this is the actual db query
        return res;
    }

    /* UPDATE IS CURRENTLY BROKEN, ..needs a primary key declared (in place of TEXT) in onCreate() to work as written.   */
    public boolean updateData(String dept,String classs,String section,String time) {
        SQLiteDatabase db = this.getWritableDatabase();        //retrieve db to write to it
        ContentValues contentValues = new ContentValues();     //declare ContentValue object
        contentValues.put(COL_1,dept);                         //update data into column 1
        contentValues.put(COL_2,classs);                       //update data into column 2
        contentValues.put(COL_3,section);                      //update data into column 3
        contentValues.put(COL_4,time);                         //update data into column 4
        db.update(TABLE_NAME, contentValues, "Name = ?",new String[] { dept });  //update the database with primary key (not declared ..is why broke)
        return true;
    }

    /* returns 1 for successful delete from db, 0 for fail */
    public Integer deleteData (String dept) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "Name = ?",new String[] {dept}); //deletes according to string in DEPT filed (1st txt field)
    }



    /* get set info to set to cardview */
    public List<GetInfo> getSetInfo(){

        //array of columns to fedtch
        String[] columns={COL_1, COL_2, COL_3, COL_4};

        //sorting orders
        //String sortOrder= COL_2 + "ASC";
        List<GetInfo> infoList= new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);

        if(cursor.moveToFirst()){
            do{
                GetInfo getInfo = new GetInfo("stuff","stuff","stuff","stuff");

                getInfo.setId(cursor.getString(cursor.getColumnIndex(COL_1)));
                getInfo.setSubject(cursor.getString(cursor.getColumnIndex(COL_2)));
                getInfo.setClas(cursor.getString(cursor.getColumnIndex(COL_3)));
                getInfo.setSection(cursor.getString(cursor.getColumnIndex(COL_4)));

                //add record to list
                infoList.add(getInfo); 
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return infoList;
    }
}
