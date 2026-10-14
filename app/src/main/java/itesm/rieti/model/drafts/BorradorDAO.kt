package itesm.rieti.model.drafts

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BorradorDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBorrador(borrador: Borrador)

    @Query("SELECT * FROM borradores WHERE correoU = :userEmail")
    fun obtenerBorradoresPorUsuario(userEmail: String): Flow<List<Borrador>>

    @Delete
    suspend fun eliminarBorrador(borrador: Borrador)
}