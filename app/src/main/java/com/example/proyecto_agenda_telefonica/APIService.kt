import com.example.proyecto_agenda_telefonica.LoginResponse
import com.example.proyecto_agenda_telefonica.TelefonoResponse
import com.example.proyecto_agenda_telefonica.UsuarioResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface APIService {

    // --- USUARIOS ---
    @GET("api/usuarios/")
    suspend fun getAllUsuarios(): Response<List<UsuarioResponse>>
    @GET("api/usuarios/{id}")
    suspend fun getUsuarioById(@Path("id") id: Int): Response<UsuarioResponse>



    @POST("api/usuarios/")
    suspend fun createUsuario(@Body usuario: UsuarioResponse): Response<Map<String, String>>

    @PUT("api/usuarios/")
    suspend fun updateUsuario(@Body usuario: UsuarioResponse): Response<Map<String, String>>

    @HTTP(method = "DELETE", path = "api/usuarios/", hasBody = true)
    suspend fun deleteUsuario(@Body body: Map<String, Int>): Response<Map<String, String>>

    // --- TELEFONOS ---
    @GET("api/telefonos/")
    suspend fun getAllTelefonos(): Response<List<TelefonoResponse>>

    @POST("api/telefonos/")
    suspend fun createTelefono(@Body telefono: TelefonoResponse): Response<Map<String, String>>

    @PUT("api/telefonos/")
    suspend fun updateTelefono(@Body telefono: TelefonoResponse): Response<Map<String, String>>

    @HTTP(method = "DELETE", path = "api/telefonos/", hasBody = true)
    suspend fun deleteTelefono(@Body body: Map<String, Int>): Response<Map<String, String>>


    //loggin
    @POST("api/auth/create_token")
    suspend fun login(@Body body: Map<String, String>): Response<LoginResponse>

    @POST("api/auth/logout")
    suspend fun logout(): Response<Map<String, String>>

}
