import android.content.Context
import com.example.proyecto_agenda_telefonica.MyApp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://89d785da1621.ngrok-free.app/"

    private fun getToken(): String {
        val prefs = MyApp.instance.getSharedPreferences("agenda_prefs", Context.MODE_PRIVATE)
        return prefs.getString("jwt_token", "") ?: ""
    }

    private val logging: HttpLoggingInterceptor
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            return interceptor
        }

    fun getInstance(): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                val token = getToken()
                if (token.isNotEmpty()) {
                    requestBuilder.addHeader("Authorization", token)
                }
                chain.proceed(requestBuilder.build())
            }
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
