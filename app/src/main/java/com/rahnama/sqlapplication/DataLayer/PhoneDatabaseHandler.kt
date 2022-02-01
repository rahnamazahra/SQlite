package com.rahnama.sqlapplication.DataLayer

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.rahnama.sqlapplication.model.*
import kotlin.contracts.Returns

class PhoneDatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    /********************************************************/
    override fun onCreate(db: SQLiteDatabase?) {
        //SQL=>Structured Query Language
        val CREATE_TABLE_CONTACT =
            "CREATE TABLE IF NOT EXISTS $DATABASE_TABLE($KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,$KEY_NAME TEXT,$KEY_FAMILY TEXT,$KEY_PHONE TEXT)"
        db?.execSQL(CREATE_TABLE_CONTACT)
    }

    /*****************************************************/
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $DATABASE_TABLE") //delete table if exists

        //create new table
        onCreate(db)
    }


    /***************************************************************/
    //CRUD=>create,read,update,delete
    fun create(contact: ContactModel) {
        var db: SQLiteDatabase = writableDatabase
        var values: ContentValues = ContentValues()
        values.put(KEY_NAME, contact.name)
        values.put(KEY_FAMILY, contact.family)
        values.put(KEY_PHONE, contact.phone)
        db.insert(DATABASE_TABLE, null, values)
        db.close()
        Log.d("create", "success")

    }

    /***********************************************************/
    @SuppressLint("Range")
    fun Get_One_contact(id: Int): ContactModel? {
        val db: SQLiteDatabase = writableDatabase
        val contact = ContactModel()
        val cursor = db.query(
            DATABASE_TABLE, arrayOf(KEY_ID, KEY_NAME, KEY_FAMILY, KEY_PHONE),
            "$KEY_ID=?",
            arrayOf(id.toString()), null, null, null
        )

        if (cursor != null) {
            cursor.moveToFirst()
            contact.id=cursor.getInt(cursor.getColumnIndex(KEY_ID))
            contact.name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
            contact.family = cursor.getString(cursor.getColumnIndex(KEY_FAMILY))
            contact.phone = cursor.getString(cursor.getColumnIndex(KEY_PHONE))
            return contact
        }
        return null
    }

    /***********************************************************/
    fun update(contact: ContactModel) {
        val db: SQLiteDatabase = writableDatabase
        val values = ContentValues()
        values.put(KEY_NAME, contact.name)
        values.put(KEY_FAMILY, contact.family)
        values.put(KEY_PHONE, contact.phone)
        db.update(DATABASE_TABLE, values, "$KEY_ID=?", arrayOf(contact.id.toString()))
        Log.d("update", "success")

    }

    /***********************************************************/
    fun delete(contact: ContactModel) {

            val db: SQLiteDatabase = writableDatabase
            db.delete(
                DATABASE_TABLE,
                "$KEY_ID=?",
                arrayOf(contact.id.toString())
            )//delete one record
            db.close()
            Log.d("delete item: ", contact.id.toString())

    }

    /***********************************************************/
    fun clearDatabase() {
        val database: SQLiteDatabase = writableDatabase
        database.delete(DATABASE_TABLE, null, null) //delete all record table.
        database.close()
        Log.d("count all item list: ",countContact().toString())
    }

    /****************************************/
    fun countContact(): Int {
        val db: SQLiteDatabase = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $DATABASE_TABLE", null)
        return cursor.count
        
    }

    /***********************************************************/
    @SuppressLint("Range")
    fun show_all_list(): ArrayList<ContactModel> {
        val db: SQLiteDatabase = readableDatabase
        var arrayList: ArrayList<ContactModel> = ArrayList()

        var cursor: Cursor =
            db.rawQuery("SELECT * FROM $DATABASE_TABLE ORDER BY $KEY_ID DESC", null)
        if (cursor.moveToFirst()) {
            do {
                val contact = ContactModel()
                contact.id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                contact.name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                contact.family = cursor.getString(cursor.getColumnIndex(KEY_FAMILY))
                contact.phone = cursor.getString(cursor.getColumnIndex(KEY_PHONE))
                arrayList.add(contact)

            } while (cursor.moveToNext())

        }
        return arrayList
    }
    /***********************************************************/
}