package com.example.praasessment.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "pra_asesmen_db";
    public Database(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE = "CREATE TABLE mhs (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nim TEXT NOT NULL, " +
                "nama TEXT NOT NULL, " +
                "ttl TEXT NOT NULL, " +
                "jenis_kelamin TEXT NOT NULL, " +
                "alamat TEXT NOT NULL)";
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS mhs");
        onCreate(db);
    }

    public ArrayList<HashMap<String, String>> getAll() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        String QUERY = "SELECT * FROM mhs";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(QUERY, null);

        if(cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", cursor.getString(0));
                map.put("nim", cursor.getString(1));
                map.put("nama", cursor.getString(2));
                map.put("ttl", cursor.getString(3));
                map.put("jenis_kelamin", cursor.getString(4));
                map.put("alamat", cursor.getString(5));
                list.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean isNimExists(String nim) {
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "SELECT COUNT(*) FROM mhs WHERE nim = ?";
        Cursor cursor = db.rawQuery(QUERY, new String[]{nim});
        boolean exists = false;

        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            exists = (count > 0);
        }
        cursor.close();
        db.close();
        return exists;
    }

    // Query Insert Data
    public boolean insert(String nim, String nama, String ttl, String jenis_kelamin, String alamat) {
        if (isNimExists(nim)) {
            Log.e("DatabaseInsert", "NIM already exists: " + nim);
            return false;
        }

        try (SQLiteDatabase db = this.getWritableDatabase()) {
            String QUERY = "INSERT INTO mhs (nim, nama, ttl, jenis_kelamin, alamat) VALUES (?, ?, ?, ?, ?)";
            db.execSQL(QUERY, new Object[]{nim, nama, ttl, jenis_kelamin, alamat});
            return true;
        } catch (Exception e) {
            Log.e("DatabaseInsert", "Error inserting data: " + e.getMessage());
            return false;
        }
    }
    // /Query Insert Data

    // Query Update Data
    public boolean update(int id, String nim, String nama, String ttl, String jenis_kelamin, String alamat) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Cek apakah NIM sudah ada di database, kecuali untuk ID yang sedang diedit
        String queryCheckNim = "SELECT COUNT(*) FROM mhs WHERE nim = ? AND id != ?";
        Cursor cursor = db.rawQuery(queryCheckNim, new String[]{nim, String.valueOf(id)});

        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();

            if (count > 0) {
                // Jika ada NIM lain dengan NIM yang sama dan ID berbeda, tampilkan pesan
                return false; // Berarti NIM sudah ada
            }
        }

        // Jika tidak ada NIM yang sama, lanjutkan update
        String query = "UPDATE mhs SET nim = ?, nama = ?, ttl = ?, jenis_kelamin = ?, alamat = ? WHERE id = ?";
        try {
            db.execSQL(query, new Object[]{nim, nama, ttl, jenis_kelamin, alamat, id});
            return true;
        } catch (Exception e) {
            Log.e("DatabaseUpdate", "Error updating data: " + e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }
    // /Query Update Data

    // Query Delete Data
    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String QUERY = "DELETE FROM mhs WHERE id = "+id;
        db.execSQL(QUERY);
    }
    // /Query Delete Data

    public HashMap<String, String> getDataById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "SELECT nim, nama, ttl, jenis_kelamin, alamat FROM mhs WHERE id = ?";
        Cursor cursor = db.rawQuery(QUERY, new String[]{id});

        HashMap<String, String> rowData = null;
        if (cursor.moveToFirst()) {
            rowData = new HashMap<>();
            rowData.put("nim", cursor.getString(cursor.getColumnIndexOrThrow("nim")));
            rowData.put("nama", cursor.getString(cursor.getColumnIndexOrThrow("nama")));
            rowData.put("ttl", cursor.getString(cursor.getColumnIndexOrThrow("ttl")));
            rowData.put("jenis_kelamin", cursor.getString(cursor.getColumnIndexOrThrow("jenis_kelamin")));
            rowData.put("alamat", cursor.getString(cursor.getColumnIndexOrThrow("alamat")));
        }
        cursor.close();
        db.close();
        return rowData;
    }

}
