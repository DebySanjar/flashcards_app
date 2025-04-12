package com.example.flashcards.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.flashcards.models.fc_model

class MyDbHelper(context: Context) : SQLiteOpenHelper(context, "my_db", null, 1),
    FlashCardsInterface {

    override fun onCreate(db: SQLiteDatabase?) {
        val query = """
            CREATE TABLE my_card (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                ask TEXT NOT NULL,
                answer TEXT NOT NULL
            )
        """.trimIndent()
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS my_card")
        onCreate(db)
    }

    override fun getAllFc(): ArrayList<fc_model> {
        val list = ArrayList<fc_model>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM my_card", null)

        if (cursor.moveToFirst()) {
            do {
                val fcModel = fc_model(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2)
                )
                list.add(fcModel)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    override fun addFc(fcModel: fc_model) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("ask", fcModel.ask)
        values.put("answer", fcModel.answer)
        db.insert("my_card", null, values)
        db.close()
    }

    override fun deleteFc(fcModel: fc_model) {
        val db = writableDatabase
        db.delete("my_card", "id=?", arrayOf(fcModel.id.toString()))
        db.close()
    }

    override fun updateFc(fcModel: fc_model) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("ask", fcModel.ask)
        values.put("answer", fcModel.answer)
        db.update("my_card", values, "id=?", arrayOf(fcModel.id.toString()))
        db.close()
    }
}
