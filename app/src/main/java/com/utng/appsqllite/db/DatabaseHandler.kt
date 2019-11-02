package com.utng.appsqllite.db

import com.utng.appsqllite.model.Users
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class DatabaseHandler(context: Context): SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){



        override fun onCreate(db: SQLiteDatabase?) {
            val CREATE_TABLE = "CREATE TABLE $TABLE_NAME " +
                    "($ID Integer PRIMARY KEY, $FIRST_NAME TEXT, $LAST_NAME TEXT)"

            db?.execSQL(CREATE_TABLE)

        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            // Called when the database needs to be upgraded
        }

        //Inserting (Creating) data
        fun addUser(user: Users): Boolean {
            //Create and/or open a database that will be used for reading and writing.
            val db = this.writableDatabase
            val values = ContentValues()
            values.put(FIRST_NAME, user.firstName)
            values.put(LAST_NAME, user.lastName)
            val _success = db.insert(TABLE_NAME, null, values)
            db.close()
            Log.v("InsertedID", "$_success")
            return (Integer.parseInt("$_success") != -1)
        }


        //Delete User
        fun deleteUser(user: Users): Boolean{
            val db=this.writableDatabase
            val firstname=user.firstName
            val idUs= db.rawQuery("select ID from TABLE_NAME where FIST_NAME=$firstname", null)
            val _success = db.delete(TABLE_NAME,"ID=$idUs", null)
            db.close()
            Log.v("DeleteID", "$_success")
            return (Integer.parseInt("$_success") != -1)
        }

    //get all users
    fun getAllUsers(): String {
        var allUser: String = "";
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var id = cursor.getString(cursor.getColumnIndex(ID))
                    var firstName = cursor.getString(cursor.getColumnIndex(FIRST_NAME))
                    var lastName = cursor.getString(cursor.getColumnIndex(LAST_NAME))

                    allUser = "$allUser\n$id $firstName $lastName"
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return allUser
    }

    companion object {
        private val DB_NAME = "UsersDB"
        private val DB_VERSION = 1;
        private val TABLE_NAME = "users"
        private val ID = "id"
        private val FIRST_NAME = "FirstName"
        private val LAST_NAME = "LastName"
    }

}