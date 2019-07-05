package com.example.database

import android.content.Context
import androidx.room.Room
import com.example.dao.NoteDao
import com.example.entity.Note
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DataBaseManager {

    companion object {

        private var mInstance: NoteDao? = null

        @Synchronized
        fun getNoteDaoInstance(context: Context): NoteDao {
            if (mInstance == null) {
                mInstance = MyDataBaseHelper.getMyDataBaseHelper(context).noteDao()
            }
            return mInstance as NoteDao
        }
    }

}