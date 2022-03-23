package org.techtown.accountbook.Room

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface DataBaseDao {

    @Insert
    fun insert(item: Item)

    
}