package com.example.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dao.NoteDao
import com.example.entity.Note
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration


@Database(entities = [Note::class], version = 3)
abstract class MyDataBaseHelper() : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {

        private var mInstance: MyDataBaseHelper? = null

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Since we didn't alter the table, there's nothing else to do here.
            }
        }

        val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE note ADD COLUMN date TEXT")
            }
        }


        @Synchronized
        fun getMyDataBaseHelper(mContext: Context): MyDataBaseHelper {
            if (mInstance == null) {
                mInstance = Room.databaseBuilder(mContext, MyDataBaseHelper::class.java, "training_room.db")
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
            }
            return mInstance as MyDataBaseHelper
        }
    }

}