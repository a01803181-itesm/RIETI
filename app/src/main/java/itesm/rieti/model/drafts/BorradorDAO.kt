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
    suspend fun insertBorrador(borrador: BorradorEntity)

    @Query("SELECT * FROM borradores WHERE correoU = :userEmail")
    fun obtenerBorradoresPorUsuario(userEmail: String): Flow<List<BorradorEntity>>

    @Delete
    suspend fun eliminarBorrador(borrador: BorradorEntity)
}