package com.whizsid.subtitleadjust.database

import android.content.ContentValues
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import com.whizsid.subtitleadjust.lib.Subtitle
import com.whizsid.subtitleadjust.lib.Time
import java.util.*

object SubtitleModel : Model<Subtitle> {
    override val tableName = "subtitles"
    override val primaryKey = "id"

    private const val idColumn = "sub_id"
    private const val contentColumn = "sub_content"
    private const val startTimeColumn = "sub_start_time"
    private const val endTimeColumn = "sub_end_time"

    override fun create(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE ${this.tableName} (
                 ${this.primaryKey} INTEGER PRIMARY KEY,
                 ${this.idColumn} INTEGER,
                 ${this.contentColumn} TEXT,
                 ${this.startTimeColumn} INTEGER,
                 ${this.endTimeColumn} INTEGER
            )
            """.trimIndent()
        )
    }

    override fun insert(db:SQLiteDatabase,subtitle:Subtitle):Long{
        var values = ContentValues()

        values.put(this.idColumn,subtitle.getId())
        values.put(this.contentColumn,subtitle.getContent())
        values.put(this.startTimeColumn,subtitle.getStartTime().toInt())
        values.put(this.endTimeColumn,subtitle.getEndTime().toInt())

        return db.insert(this.tableName,null,values)
    }

    override fun truncate(db: SQLiteDatabase) {
        db.execSQL("DELETE FROM ${this.tableName}")
    }

    private fun extractFromCursor(cursor:Cursor): Subtitle {
        val id = cursor.getInt(cursor.getColumnIndex(this.idColumn))
        val autoIncrement = cursor.getInt(cursor.getColumnIndex(this.primaryKey))
        val content = cursor.getString(cursor.getColumnIndex(this.contentColumn))
        val startTime = cursor.getLong(cursor.getColumnIndex(this.startTimeColumn))
        val endTime = cursor.getLong(cursor.getColumnIndex(this.endTimeColumn))

        return Subtitle(autoIncrement,id,Time(startTime),Time(endTime),content)
    }

    override fun search(db:SQLiteDatabase,keyword:String,limit:Int?) : MutableList<Subtitle>{
        val cursor = db.rawQuery("SELECT * FROM ${this.tableName} WHERE ${this.contentColumn} LIKE '%$keyword%' LIMIT ${limit?:30}",null)

        var subtitles = mutableListOf<Subtitle>()

        if(cursor!!.moveToFirst()){

            do {
                subtitles.add(this.extractFromCursor(cursor))
            } while (cursor.moveToNext())
        }

        return subtitles

    }

    override fun delete(db: SQLiteDatabase, item: Subtitle) {
        db.execSQL("DELETE FROM ${this.tableName} WHERE ${this.idColumn}=\"${DatabaseUtils.sqlEscapeString(item.getIncrementalIndex().toString())}\" ")
    }

    override fun update(db: SQLiteDatabase, item: Subtitle) {
        db.execSQL(
            """
                UPDATE ${this.tableName}
                SET
                    ${this.idColumn} = "${DatabaseUtils.sqlEscapeString(item.getId().toString())}",
                    ${this.contentColumn} = "${DatabaseUtils.sqlEscapeString(item.getContent())}",
                    ${this.startTimeColumn} = "${DatabaseUtils.sqlEscapeString(item.getStartTime().toInt().toString())}",
                    ${this.endTimeColumn} = "${DatabaseUtils.sqlEscapeString(item.getEndTime().toInt().toString())}"
                WHERE
                    ${this.primaryKey} = "${DatabaseUtils.sqlEscapeString(item.getIncrementalIndex().toString())}";
            """.trimIndent()
        )
    }

    override fun find(db: SQLiteDatabase, id: Long): Subtitle? {
        val cursor = db.rawQuery("SELECT * FROM ${this.tableName} WHERE ${this.primaryKey} = \"${DatabaseUtils.sqlEscapeString(id.toString())}\" ",null)

        if(cursor!!.moveToFirst()){
            return extractFromCursor(cursor)
        }

        return null
    }

    override fun getNth(db: SQLiteDatabase, number: Long): Subtitle? {
        val cursor = db.rawQuery("SELECT * FROM ${this.tableName} ORDER BY ${this.primaryKey} LIMIT 1 OFFSET $number ",null)

        if(cursor!!.moveToFirst()){
            return extractFromCursor(cursor)
        }

        return null
    }
}
