package org.techtown.accountbook.DI

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.techtown.accountbook.Model.AppDatabase
import org.techtown.accountbook.Model.SpendingDataDao
import org.techtown.accountbook.Repository.DBRepository
import org.techtown.accountbook.ViewModel.ViewModel
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object modules{

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,AppDatabase::class.java,"spending")
            .fallbackToDestructiveMigration()
            .build()
    }
    @Provides
    fun provideDao(database: AppDatabase): SpendingDataDao = database.dao

    @Provides
    @Singleton
    fun provideRepo(dao: SpendingDataDao) : DBRepository = DBRepository(dao)
    @Provides
    fun provideVM(repo: DBRepository) = ViewModel(repo)
}