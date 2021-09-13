package uz.techie.schedulize.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.techie.schedulize.db.SubjectsListDataBase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context):SubjectsListDataBase{
        return Room.databaseBuilder(
            context,
            SubjectsListDataBase::class.java,
            "time_table"
        ).build()
    }
}