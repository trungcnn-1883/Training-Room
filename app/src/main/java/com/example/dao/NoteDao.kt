package com.example.dao

import androidx.room.*
import com.example.entity.Note
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface NoteDao {

//    @Query("SELECT * FROM note WHERE title = :name")
//    fun getAllNotes(name: String): Flowable<List<Note>>

    @Query("SELECT * FROM note ")
    fun getAllNotes(): Flowable<List<Note>>

    @Insert
    fun addNote(note: Note): Single<Long>

    @Delete
    fun deleteNode(note: Note): Single<Int>

    @Query("SELECT COUNT(title) FROM note")
    fun getNoteCount(): Int

    @Update
    fun updateNote(note: Note): Single<Int>


}