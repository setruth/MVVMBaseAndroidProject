package com.setruth.mvvmbaseproject.repository.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int?=null,
    @ColumnInfo(name = "account")
    var act: String,
    @ColumnInfo
    var nickname: String,
)