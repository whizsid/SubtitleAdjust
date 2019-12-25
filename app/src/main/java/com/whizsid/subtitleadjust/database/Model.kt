package com.whizsid.subtitleadjust.database

import android.database.sqlite.SQLiteDatabase

/**
 * All database models should implement to this interface
 */
interface Model<T> {
    val tableName:String

    val primaryKey: String

    /**
     * Inserting the object into table
     *
     * @param db Database instance
     * @param item Object instance
     *
     * @return Last inserted auto increment id
     */
    open fun insert(db:SQLiteDatabase,item:T):Long

    /**
     * Creating the table
     *
     * @param db Database instance
     */
    open fun create(db:SQLiteDatabase)

    /**
     * Truncate the database
     *
     * @param db Database instance
     */
    open fun truncate(db:SQLiteDatabase)

    /**
     * Searching for a given keyword
     *
     * @param db Database instance
     * @param keyword Searching keyword
     *
     * @return List of result objects
     */
    open fun search(db:SQLiteDatabase, keyword: String, limit: Int?): MutableList<T>

    /**
     * Deleting an instance from the table
     *
     * @param db Database instance
     * @param item Instance to delete
     */
    open fun delete(db:SQLiteDatabase, item: T)

    /**
     * Updating the table row from an instance
     *
     * @param db Database instance
     * @param item Instance to update
     */
    open fun update(db:SQLiteDatabase, item: T)

    /**
     * Finding an item by unique id
     *
     * @param db Database instance
     * @param id Auto increment id
     */
    open fun find(db:SQLiteDatabase, id: Long): T?

    /**
     * Finding an item by index
     */
    open fun getNth(db:SQLiteDatabase, id: Long): T?

}