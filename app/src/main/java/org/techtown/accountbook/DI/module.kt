package org.techtown.accountbook.DI

import android.app.Application
import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.techtown.accountbook.Model.AppDatabase
import org.techtown.accountbook.Model.SpendingDataDao
import org.techtown.accountbook.Repository.DBRepository
import org.techtown.accountbook.ViewModel.ViewModel

val module  = module{

    fun provideDatabase(application: Application): AppDatabase{
        return Room.databaseBuilder(
            application,AppDatabase::class.java,"spending")
            .fallbackToDestructiveMigration()
            .build()
    }
    fun provideDao(database: AppDatabase): SpendingDataDao = database.dao

    single {
        provideDatabase(androidApplication())
    }
    single {
        provideDao(get())
    }
    single {
        DBRepository(get())
    }
    viewModel { ViewModel(get()) }
}