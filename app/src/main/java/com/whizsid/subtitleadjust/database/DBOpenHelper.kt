package com.whizsid.subtitleadjust.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBOpenHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    private val models = mutableListOf<Model<*>>(
        SubtitleModel
    )

    override fun onCreate(db: SQLiteDatabase) {
        models.forEach {
            it.create(db)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        models.forEach {
            it.truncate(db)
        }

        onCreate(db)
    }

    /**
     * Empty the database
     */
    fun empty(){
        models.forEach {
            it.truncate(this.writableDatabase)
        }
    }

    /**
     * Searching for records by a keyword
     *
     * @param model Table Model
     * @param keyword Search term
     */
    fun <T>search(model:Model<T>,keyword: String,limit:Int?): MutableList<T> {
        return model.search(this.readableDatabase,keyword,limit)
    }

    /**
     * Inserting a record to database
     *
     * @param model Table model
     * @param instance Instance to delete
     *
     * @return Inserted id
     */
    fun <T>insert(model:Model<T>,instance:T): Long {
        return model.insert(this.writableDatabase,instance)
    }

    /**
     * Updating a record in the database
     *
     * @param model Table model
     * @param instance Updated instance
     */
    fun <T>update(model:Model<T>,instance:T) {
        return model.update(this.writableDatabase,instance)
    }

    /**
     * Deleting a record from the database
     *
     * @param model Table model
     * @param instance Instance to delete
     */
    fun <T>delete(model:Model<T>,instance:T) {
        return model.delete(this.writableDatabase,instance)
    }

    /**
     * Finding a record by primary key
     *
     * @param model Table model
     * @param id Auto incremental id of the record
     */
    fun <T>find(model:Model<T>,id:Long) :T? {
        return model.find(this.readableDatabase,id)
    }

    /**
     * Fetching a item by the index
     *
     * @param model Table modal
     * @param number Index
     *
     * @return nth item of the table
     */
    fun <T>getNth(model:Model<T>,number:Long):T? {
        return model.getNth(this.readableDatabase,number)
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "subtitleAdjust.db"
    }
}