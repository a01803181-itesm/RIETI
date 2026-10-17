package itesm.rieti.model.drafts

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(version = 1, entities = [Borrador::class], exportSchema = false)
@TypeConverters(Converters::class)
abstract class BorradorDB : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: BorradorDB? = null

        fun init(context: Context){
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            BorradorDB::class.java,
                            "RIETI Offline DB"
                        )
                        .fallbackToDestructiveMigration(false)
                        .build()
                    }
                }
            }
        }

        fun getInstance(): BorradorDB {
            return INSTANCE ?: throw IllegalStateException(
                "BorradorDB no ha sido inicializada. Llama a BorradorDB.init(applicationContext) en MainActivity."
            )
        }
    }
    abstract fun getBorradorDAO(): BorradorDAO
}