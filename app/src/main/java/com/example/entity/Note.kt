package com.example.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Note(
    @ColumnInfo(name = "title") var title: String?,
    @ColumnInfo(name = "content") var content: String?
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "date") var date: String? = null


    constructor(id: Int, title: String, content: String) : this(title, content) {
        this.id = id
    }
}