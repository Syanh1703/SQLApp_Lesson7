package com.example.sqlapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler (context: Context) :SQLiteOpenHelper(context,DATABASE_NAME, null, DATABASE_VERSION) {

    companion object
    {
        private const val DATABASE_NAME = "FolderDatabase"
        private const val DATABASE_VERSION = 1

        private const val SHEET_NAME = "folder"

        private const val KEY_ID :String = "_id"
        private const val KEY_NAME = "name"
        private const val KEY_DES = "description"

        //Create the Database Table
        val SQL_CREATE_ENTRIES = ("CREATE TABLE " + SHEET_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_DES + " TEXT" + ")")
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + SHEET_NAME
        db!!.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    /**
     * Function to insert data to the table
     */
    fun addData (data :DataModels):Long
    {
        val db = this.writableDatabase//interact with the data

        val contentValues = ContentValues()
        contentValues.put(KEY_NAME,data.folderName)
        contentValues.put(KEY_DES, data.folderDes)

        //Insert Data to the table using insert query
        val success = db.insert(SHEET_NAME,null,contentValues)
        db.close()
        return success
    }

    /**
     * Get the inserted folder from the database in form of the Array List
     */
    fun viewFolder():ArrayList<DataModels>
    {
        val folderList :ArrayList<DataModels> = ArrayList()

        //Query to select all the items from the table
        val selectQuery = "SELECT * FROM $SHEET_NAME"
        val db = this.readableDatabase//Get permission to read the database

        /**
         * Use the cursor to read the table one by one
         */
        val cursor : Cursor?
        try {
            cursor = db.rawQuery(selectQuery,null)
        }catch (e:SQLiteException)
        {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var name:String
        var id :Int
        var des :String
        if(cursor!!.moveToFirst())
        {
            while (!cursor.isAfterLast)
            {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME))
                des = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DES))

                val folder = DataModels(id = id, folderName = name, folderDes = des)
                folderList.add(folder)
                cursor.moveToNext()
            }
        }
        return folderList
    }

    /**
     * Update the Folder
     */
    fun updateFolder(dataModels: DataModels):Int
    {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, dataModels.folderName)
        contentValues.put(KEY_DES,dataModels.folderDes)

        val success = db.update(SHEET_NAME,contentValues, KEY_ID + "=" + dataModels.id, null)

        db.close()
        return success
    }

    /**
     * Delete the Folder
     */
    fun deleteFolder(data: DataModels):Int
    {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, data.id)
        val deleteSuccess = db.delete(SHEET_NAME, KEY_ID + "=" + data.id, null)
        db.close()
        return deleteSuccess
    }
}