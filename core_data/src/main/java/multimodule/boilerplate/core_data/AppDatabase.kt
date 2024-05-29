package multimodule.boilerplate.core_data

import androidx.room.Database
import androidx.room.RoomDatabase
import multimodule.boilerplate.data.db.TestDao

@Database(
    entities = [TestDao::class,],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun testDao(): TestDao
}
