package itesm.rieti.model.drafts

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 0, entities = [Borrador::class])
abstract class BorradorDB : RoomDatabase() {
    abstract fun getBorradorDAO(): BorradorDAO
}