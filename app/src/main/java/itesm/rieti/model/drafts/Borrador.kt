package itesm.rieti.model.drafts

import android.location.Address
import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.JsonParser
import itesm.rieti.model.api.FormError
import itesm.rieti.model.enums.RangoEdad
import itesm.rieti.model.enums.TipoTrabajo
import itesm.rieti.model.esquemas.Reporte
import itesm.rieti.viewModel.nuevoReporte.SaveState
import itesm.rieti.viewModel.nuevoReporte.SendState
import java.util.Date
import java.util.Locale

@Entity(tableName = "borradores")
data class Borrador(
    @PrimaryKey(autoGenerate = true)
    val idBorrador: Int = 0,
    val correoU: String = "",
    val rangoEdad: String? = null,
    val tipoTrabajo: String? = null,
    val detallesAdicionales: String? = null,
    val numNinios: Int = 0,
    val direccion: String? = null,
    val municipio: String? = null,
    val latitud: Float? = null,
    val longitud: Float? = null,
    val rawName: String = "",
    val date: Date = Date(),
    val imageUri: Uri? = null,
    val rawAddress: Address? = null,
    val persistentImagePath: String? = null,
)

private data class AddressDto(
    val localeLanguage: String? = null,
    val localeCountry: String? = null,
    val featureName: String? = null,
    val adminArea: String? = null,
    val subAdminArea: String? = null,
    val locality: String? = null,
    val subLocality: String? = null,
    val thoroughfare: String? = null,
    val subThoroughfare: String? = null,
    val premises: String? = null,
    val postalCode: String? = null,
    val countryCode: String? = null,
    val countryName: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val phone: String? = null,
    val url: String? = null,
    val addressLines: Map<Int, String>? = null
)

@TypeConverters
class Converters {
    @TypeConverter fun fromDate(date: Date?): Long? = date?.time
    @TypeConverter
    fun toDate(millis: Long?): Date? = millis?.let { Date(it) }

    @TypeConverter fun fromUri(uri: Uri?): String? = uri?.toString()
    @TypeConverter fun toUri(uriString: String?): Uri? = uriString?.let { Uri.parse(it) }

    @TypeConverter
    fun fromAddress(address: Address?): String? =
        address?.let { addr ->
            val linesMap = mutableMapOf<Int, String>()
            try {
                for (i in 0..addr.maxAddressLineIndex) {
                    addr.getAddressLine(i)?.let { linesMap[i] = it }
                }
            } catch (_: Exception) {}

            val dto = AddressDto(
                localeLanguage = try { addr.locale?.language } catch (_: Exception) { null },
                localeCountry = try { addr.locale?.country } catch (_: Exception) { null },
                featureName = addr.featureName,
                adminArea = addr.adminArea,
                subAdminArea = addr.subAdminArea,
                locality = addr.locality,
                subLocality = addr.subLocality,
                thoroughfare = addr.thoroughfare,
                subThoroughfare = addr.subThoroughfare,
                premises = addr.premises,
                postalCode = addr.postalCode,
                countryCode = addr.countryCode,
                countryName = addr.countryName,
                latitude = try { if (addr.hasLatitude()) addr.latitude else null } catch (_: Exception) { null },
                longitude = try { if (addr.hasLongitude()) addr.longitude else null } catch (_: Exception) { null },
                phone = addr.phone,
                url = addr.url,
                addressLines = if (linesMap.isNotEmpty()) linesMap else null
            )
            Gson().toJson(dto)
        }

    @TypeConverter
    fun toAddress(json: String?): Address? {
        if (json.isNullOrBlank()) return null
        return try {
            val jsonElement = JsonParser.parseString(json)
            if (!jsonElement.isJsonObject) return null
            val jsonObject = jsonElement.asJsonObject

            val lang = jsonObject.get("localeLanguage")?.asString
                ?: jsonObject.getAsJsonObject("mLocale")?.get("language")?.asString
                ?: Locale.getDefault().language
            val country = jsonObject.get("localeCountry")?.asString
                ?: jsonObject.getAsJsonObject("mLocale")?.get("country")?.asString
                ?: Locale.getDefault().country

            val locale = if (country.isNotBlank()) Locale(lang, country) else Locale(lang)
            val address = Address(locale)

            fun getString(vararg keys: String): String? {
                for (key in keys) {
                    val elem = jsonObject.get(key)
                    if (elem != null && !elem.isJsonNull) return elem.asString
                }
                return null
            }

            address.featureName = getString("featureName", "mFeatureName")
            address.adminArea = getString("adminArea", "mAdminArea")
            address.subAdminArea = getString("subAdminArea", "mSubAdminArea")
            address.locality = getString("locality", "mLocality")
            address.subLocality = getString("subLocality", "mSubLocality")
            address.thoroughfare = getString("thoroughfare", "mThoroughfare")
            address.subThoroughfare = getString("subThoroughfare", "mSubThoroughfare")
            address.premises = getString("premises", "mPremises")
            address.postalCode = getString("postalCode", "mPostalCode")
            address.countryCode = getString("countryCode", "mCountryCode")
            address.countryName = getString("countryName", "mCountryName")
            address.phone = getString("phone", "mPhone")
            address.url = getString("url", "mUrl")

            val lat = jsonObject.get("latitude")?.asDouble ?: jsonObject.get("mLatitude")?.asDouble
            if (lat != null) address.latitude = lat

            val lng = jsonObject.get("longitude")?.asDouble ?: jsonObject.get("mLongitude")?.asDouble
            if (lng != null) address.longitude = lng

            val linesObj = jsonObject.getAsJsonObject("addressLines") ?: jsonObject.getAsJsonObject("mAddressLines")
            linesObj?.entrySet()?.forEach { (key, value) ->
                val idx = key.toIntOrNull()
                if (idx != null && value != null && !value.isJsonNull) {
                    address.setAddressLine(idx, value.asString)
                }
            }

            address
        } catch (_: Exception) {
            null
        }
    }
}