package uz.techie.schedulize.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.techie.schedulize.db.SubjectsListDataBase
import uz.techie.schedulize.db.RoomSubjectDataSource
import uz.techie.schedulize.repositories.SubjectLocalDataSource
import uz.techie.schedulize.repositories.SubjectRepository
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class SubjectDataModule {
    @Provides
    @Singleton
    fun provideLocalDataSource(roomDatabase: SubjectsListDataBase):SubjectLocalDataSource = RoomSubjectDataSource(roomDatabase.subjectDAO())

    @Provides
    @Singleton
    fun provideSubjectRepository(local:SubjectLocalDataSource) = SubjectRepository(local)
}