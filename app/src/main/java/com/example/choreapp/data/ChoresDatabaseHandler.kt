package com.example.choreapp.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.choreapp.model.*

class ChoresDatabaseHandler(context: Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        var CREATE_CHORE_TABLE: String = "CREATE TABLE $TABLE_NAME ($KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                                                                    "$KEY_CHORE_NAME TEXT," +
                                                                    "$KEY_ASSIGNED_BY TEXT," +
                                                                    "$KEY_ASSIGNED_TO TEXT," +
                                                                    "$KEY_ASSIGNED_TIME LONG)"

        db?.execSQL(CREATE_CHORE_TABLE)

        Log.d("SQLite", "TABLE $TABLE_NAME CREATED.")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        Log.d("SQLite", "TABLE $TABLE_NAME DROPPED.")

        onCreate(db)
        Log.d("SQLite", "UPGRADE COMPLETED")
    }

    fun createChore(chore: Chore) : Chore {
        var db: SQLiteDatabase = writableDatabase
        var values: ContentValues = ContentValues()

        chore.TimeAssigned = System.currentTimeMillis()
        values.put(KEY_CHORE_NAME, chore.ChoreName)
        values.put(KEY_ASSIGNED_BY, chore.AssignedBy)
        values.put(KEY_ASSIGNED_TO, chore.AssignedTo)
        values.put(KEY_ASSIGNED_TIME, chore.TimeAssigned)

        var insertValue: Long = db.insert(TABLE_NAME, null, values)
        db.close()

        chore.Id = insertValue.toInt()

        return chore
    }

    fun readChore(id: Long) : Chore? {
        var db: SQLiteDatabase = writableDatabase
        var cursor: Cursor = db.query(TABLE_NAME,
                                        arrayOf(KEY_ID, KEY_CHORE_NAME, KEY_ASSIGNED_TO, KEY_ASSIGNED_BY, KEY_ASSIGNED_TIME),
                                        "$KEY_ID=?",
                                        arrayOf(id.toString()),
                                        null,
                                        null,
                                        null)
        if (cursor != null){
            cursor.moveToFirst()

            var chore = Chore()
            chore.Id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
            chore.ChoreName = cursor.getString(cursor.getColumnIndex(KEY_CHORE_NAME))
            chore.AssignedBy = cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_BY))
            chore.AssignedTo = cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_TO))
            chore.TimeAssigned = cursor.getLong(cursor.getColumnIndex(KEY_ASSIGNED_TIME))

            return chore
        }
        else{
            return null
        }
    }

    fun readAllChores() : ArrayList<Chore> {
        var db: SQLiteDatabase = writableDatabase
        var list: ArrayList<Chore> = ArrayList()
        var selectAllQuery = "SELECT * FROM $TABLE_NAME ORDER BY $KEY_ID DESC"

        var cursor : Cursor = db.rawQuery(selectAllQuery, null)
        if (cursor != null){
            cursor.moveToFirst()
            if (cursor.count != 0){
                do{
                    var chore = Chore()
                    chore.Id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                    chore.ChoreName = cursor.getString(cursor.getColumnIndex(KEY_CHORE_NAME))
                    chore.AssignedBy = cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_BY))
                    chore.AssignedTo = cursor.getString(cursor.getColumnIndex(KEY_ASSIGNED_TO))
                    chore.TimeAssigned = cursor.getLong(cursor.getColumnIndex(KEY_ASSIGNED_TIME))

                    list.add(chore)
                } while (cursor.moveToNext())
            }
        }

        return list
    }

    fun updateChore(chore: Chore) : Int {
        var db: SQLiteDatabase = writableDatabase

        var values: ContentValues = ContentValues()
        values.put(KEY_CHORE_NAME, chore.ChoreName)
        values.put(KEY_ASSIGNED_BY, chore.AssignedBy)
        values.put(KEY_ASSIGNED_TO, chore.AssignedTo)
        values.put(KEY_ASSIGNED_TIME, System.currentTimeMillis())

        return db.update(TABLE_NAME, values, "$KEY_ID=?", arrayOf(chore.Id.toString()))
    }

    fun deleteChore(id: Long) : Int{
        var db: SQLiteDatabase = writableDatabase
        var affectedRow: Int = db.delete(TABLE_NAME, "$KEY_ID=?", arrayOf(id.toString()))
        db.close()

        return affectedRow
    }

    fun getChoresCountv2() : Int {
        var db: SQLiteDatabase = writableDatabase
        var countQuery : String = "SELECT COUNT(*) AS TotalCount FROM $TABLE_NAME"
        var cursor : Cursor = db.rawQuery(countQuery, null)

        if (cursor != null){
            cursor.moveToFirst()

            return cursor.getInt(cursor.getColumnIndex("TotalCount"))
        }
        else {
            return 0
        }
    }
}