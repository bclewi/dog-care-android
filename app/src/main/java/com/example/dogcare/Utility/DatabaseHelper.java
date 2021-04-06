package com.example.dogcare.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.dogcare.Model.DogModel;
import com.example.dogcare.Model.EventModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 2;
    private static final String NAME = "dogCareDatabase";

    private static final String EVENTS_TABLE = "events";
    private static final String EVENT_ID = "id";
    //TODO private static final String DOG_ID_FK = "dog_id"; // References foreign key "id" in "pack" table
    private static final String DOG_NAME = "name"; // TODO Remove this after referencing dog_id
    private static final String EVENT_TYPE = "type"; // Ex: Walk, Poop, Eat, ...
    private static final String EVENT_DATE_TIME = "date_time";
    private static final String EVENT_NOTES = "notes";
    private static final String EVENT_STATUS = "status"; // From to-do list app (1 = done, 0 = not)
    private static final String CREATE_EVENT_TABLE = "CREATE TABLE " + EVENTS_TABLE + "("
            + EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            //TODO + DOG_ID_FK + " INTEGER, "
            + DOG_NAME + " TEXT, "
            + EVENT_TYPE + " TEXT, "
            + EVENT_DATE_TIME + " LONG, "
            + EVENT_NOTES + " TEXT, "
            + EVENT_STATUS + " INTEGER)";

    private static final String DOGS_TABLE = "dogs";
    private static final String DOG_ID = "id";
    //TODO private static final String DOG_NAME = "name"; // Add this after referencing dog_id
    private static final String DOG_BREED = "breed";
    private static final String DOG_AGE = "age";
    private static final String DOG_IMAGE = "image";
    private static final String CREATE_PACK_TABLE = "CREATE TABLE " + DOGS_TABLE + "("
            + DOG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            //TODO + DOG_NAME + " TEXT, "
            + DOG_BREED + " TEXT, "
            + DOG_AGE + " INTEGER, "
            + DOG_IMAGE + " BLOB);";

    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_EVENT_TABLE);
            db.execSQL(CREATE_PACK_TABLE);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + EVENTS_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DOGS_TABLE);
            // Create tables again
            onCreate(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }



    /*
    EVENT METHODS
     */
    public void insertEvent(EventModel event){
        ContentValues cv = new ContentValues();
        //TODO cv.put(DOG_ID_FK, event.getDogID());
        cv.put(DOG_NAME, event.getDogName());
        cv.put(EVENT_TYPE, event.getType());
        cv.put(EVENT_DATE_TIME, event.getDateTime());
        cv.put(EVENT_NOTES, event.getNotes());
        cv.put(EVENT_STATUS, 0);
        try {
            db.insert(EVENTS_TABLE, null, cv);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<EventModel> getAllEvents(){
        List<EventModel> eventList = new ArrayList<>();
        Cursor cursor = null;
        db.beginTransaction();
        try {
            cursor = db.query(EVENTS_TABLE, null, null, null, null, null, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        EventModel event = new EventModel();
                        event.setId(cursor.getInt(cursor.getColumnIndex(EVENT_ID)));
                        event.setDogName(cursor.getString(cursor.getColumnIndex(DOG_NAME)));
                        event.setType(cursor.getString(cursor.getColumnIndex(EVENT_TYPE)));
                        event.setDateTime(cursor.getLong(cursor.getColumnIndex(EVENT_DATE_TIME)));
                        event.setNotes(cursor.getString(cursor.getColumnIndex(EVENT_NOTES)));
                        event.setStatus(cursor.getInt(cursor.getColumnIndex(EVENT_STATUS)));
                        eventList.add(event);
                    }
                    while(cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            assert cursor != null;
            cursor.close();
        }
        return eventList;
    }

    /* TODO Add this back in when using dog_id
    public void updateDog(int id, int dogID) {
        ContentValues cv = new ContentValues();
        cv.put(DOG_ID_FK, dogID);
        try {
            db.update(EVENTS_TABLE, cv, EVENT_ID + "= ?", new String[] {String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     */

    // TODO Remove this when using dog_id
    public void updateName(int id, String name) {
        ContentValues cv = new ContentValues();
        cv.put(DOG_NAME, name);
        try {
            db.update(EVENTS_TABLE, cv, EVENT_ID + "= ?", new String[] {String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateType(int id, String type) {
        ContentValues cv = new ContentValues();
        cv.put(EVENT_TYPE, type);
        try {
            db.update(EVENTS_TABLE, cv, EVENT_ID + "= ?", new String[] {String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateDateTime(int id, Long dateTime) {
        ContentValues cv = new ContentValues();
        cv.put(EVENT_DATE_TIME, dateTime);
        try {
            db.update(EVENTS_TABLE, cv, EVENT_ID + "= ?", new String[] {String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateNotes(int id, String notes) {
        ContentValues cv = new ContentValues();
        cv.put(EVENT_NOTES, notes);
        try {
            db.update(EVENTS_TABLE, cv, EVENT_ID + "= ?", new String[] {String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateStatus(int id, int status) {
        ContentValues cv = new ContentValues();
        cv.put(EVENT_STATUS, status);
        try {
            db.update(EVENTS_TABLE, cv, EVENT_ID + "= ?", new String[] {String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteEvent(int id) {
        try {
            db.delete(EVENTS_TABLE, EVENT_ID + "= ?", new String[] {String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /*
    DOG METHODS
     */
    // https://stackoverflow.com/questions/9357668/how-to-store-image-in-sqlite-database
    public static byte[] getByteArrayFromBitmap(Bitmap dogImageBitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        dogImageBitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
    public void insertDog(DogModel dog){
        ContentValues cv = new ContentValues();
        //TODO cv.put(DOG_NAME, dog.getName());
        cv.put(DOG_BREED, dog.getBreed());
        cv.put(DOG_AGE, dog.getAge());
        cv.put(DOG_IMAGE, getByteArrayFromBitmap(dog.getImage()));
        try {
            db.insert(DOGS_TABLE, null, cv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<DogModel> getAllDogs() {
        List<DogModel> dogList = new ArrayList<>();
        Cursor cursor = null;
        db.beginTransaction();
        try {
            cursor = db.query(DOGS_TABLE, null, null, null, null, null, null, null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        DogModel dog = new DogModel();
                        dog.setId(cursor.getInt(cursor.getColumnIndex(DOG_ID)));
                        dog.setName(cursor.getString(cursor.getColumnIndex(DOG_NAME)));
                        dog.setAge(cursor.getInt(cursor.getColumnIndex(DOG_AGE)));
                        dog.setBreed(cursor.getString(cursor.getColumnIndex(DOG_BREED)));
                        // https://stackoverflow.com/questions/9357668/how-to-store-image-in-sqlite-database
                        byte[] dogImageByteArray = cursor.getBlob(cursor.getColumnIndex(DOG_IMAGE));
                        Bitmap dogImageBitmap = BitmapFactory.decodeByteArray(dogImageByteArray, 0, dogImageByteArray.length);
                        dog.setImage(dogImageBitmap);
                        dogList.add(dog);
                    }
                    while(cursor.moveToNext());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            assert cursor != null;
            cursor.close();
        }
        return dogList;
    }

    /* TODO Add this when using dog_id
    public void updateName(int id, String name) {
        ContentValues cv = new ContentValues();
        cv.put(DOG_NAME, name);
        try {
            db.update(DOGS_TABLE, cv, DOG_ID + "= ?", new String[] {String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

    public void updateBreed(int id, String breed) {
        ContentValues cv = new ContentValues();
        cv.put(DOG_BREED, breed);
        try {
            db.update(DOGS_TABLE, cv, DOG_ID + "= ?", new String[] {String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAge(int id, int age) {
        ContentValues cv = new ContentValues();
        cv.put(DOG_AGE, age);
        try {
            db.update(DOGS_TABLE, cv, DOG_ID + "= ?", new String[] {String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateImage(int id, Bitmap dogImageBitmap) {
        ContentValues cv = new ContentValues();
        cv.put(DOG_IMAGE, getByteArrayFromBitmap(dogImageBitmap));
        try {
            db.update(DOGS_TABLE, cv, DOG_ID + "= ?", new String[] {String.valueOf(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}