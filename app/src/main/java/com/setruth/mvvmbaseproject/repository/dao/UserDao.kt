package com.setruth.mvvmbaseproject.repository.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.setruth.mvvmbaseproject.repository.entity.UserEntity


@Dao
interface UserDao{
    @Query("select * from user")
    fun getAll():List<UserEntity>

    @Insert
    fun insertUser(userEntity: UserEntity)

    @Query("select * from user where account = :account")
    fun getInfoByAct(account:String):List<UserEntity>
}