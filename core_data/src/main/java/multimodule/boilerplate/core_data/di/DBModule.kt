package multimodule.boilerplate.core_data.di

import androidx.room.Room
import multimodule.boilerplate.core_data.AppDatabase
import multimodule.boilerplate.data.db.TestDao
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val dbModule = DI.Module(name = "DB") {
    bind<AppDatabase>() with singleton {
        Room.databaseBuilder(instance(), AppDatabase::class.java, "database")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    bind<TestDao>() with singleton {
        instance<AppDatabase>().testDao()
    }
}