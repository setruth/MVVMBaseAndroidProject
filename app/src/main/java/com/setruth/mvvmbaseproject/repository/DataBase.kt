package com.setruth.mvvmbaseproject.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.setruth.mvvmbaseproject.repository.dao.UserDao
import com.setruth.mvvmbaseproject.repository.entity.UserEntity


@Database(
    version = 1,
    entities = [UserEntity::class],
    exportSchema = false
)
abstract class MyDataBase:RoomDatabase(){
    companion object{
        private var db:MyDataBase?=null
        private val name="app"
        fun getDB(context: Context)=if (db==null){
            Room.databaseBuilder(context,MyDataBase::class.java,name).enableMultiInstanceInvalidation().build().apply {
                db=this
            }
        }else{
            db!!
        }
    }
    abstract fun getUserDao():UserDao
}