package org.techtown.accountbook.Model

import android.content.Context
import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.Room

@Database(entities = [SpendingData::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract val dao : SpendingDataDao

    companion object{
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context) : AppDatabase? {
            if(instance == null){
                synchronized(AppDatabase::class){
                    instance = Room.databaseBuilder(context.applicationContext,AppDatabase::class.java,"spending").build()
                }
            }
            return instance
        }
    }
}