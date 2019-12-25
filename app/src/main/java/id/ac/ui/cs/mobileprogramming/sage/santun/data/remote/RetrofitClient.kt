package id.ac.ui.cs.mobileprogramming.sage.santun.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {
        private var client: Retrofit? = null

        fun getClient(baseUrl: String): Retrofit {
            if (client == null) {
                client = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return client as Retrofit
        }
    }
}
