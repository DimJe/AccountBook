package org.techtown.accountbook.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
class Item(
    @PrimaryKey(autoGenerate = true) var id : Int?,
    @ColumnInfo(name = "year") var year : String?,
    @ColumnInfo(name = "month") var month : String?,
    @ColumnInfo(name = "day") var day : String?,

    @ColumnInfo(name = "eat") var eat : Int = 0,

    @ColumnInfo(name = "beer") var beer : Int = 0,

    @ColumnInfo(name = "culture") var culture : Int = 0,

    @ColumnInfo(name = "snack") var snack : Int = 0,

    @ColumnInfo(name = "tax") var tax : Int = 0,

    @ColumnInfo(name = "etc") var etc : Int = 0

)