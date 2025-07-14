import com.example.proyecto_agenda_telefonica.TelefonoResponse
import com.example.proyecto_agenda_telefonica.UsuarioResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT

interface APIService {

    // --- USUARIOS ---
    @GET("api/usuarios/")
    suspend fun getAllUsuarios(): Response<List<UsuarioResponse>>

    @POST("api/usuarios/")
    suspend fun createUsuario(@Body usuario: UsuarioResponse): Response<String>

    @PUT("api/usuarios/")
    suspend fun updateUsuario(@Body usuario: UsuarioResponse): Response<String>

    @HTTP(method = "DELETE", path = "api/usuarios/", hasBody = true)
    suspend fun deleteUsuario(@Body body: Map<String, Int>): Response<String>

    // --- TELEFONOS ---
    @GET("api/telefonos/")
    suspend fun getAllTelefonos(): Response<List<TelefonoResponse>>

    @POST("api/telefonos/")
    suspend fun createTelefono(@Body telefono: TelefonoResponse): Response<String>

    @PUT("api/telefonos/")
    suspend fun updateTelefono(@Body telefono: TelefonoResponse): Response<String>

    @HTTP(method = "DELETE", path = "api/telefonos/", hasBody = true)
    suspend fun deleteTelefono(@Body body: Map<String, Int>): Response<String>
}
