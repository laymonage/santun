package id.ac.ui.cs.mobileprogramming.sage.santun.data.remote

class APIWise {
    companion object {
        private const val BASE_URL = "https://wise.laymonage.com/"
        const val STATIC_URL = "https://wise.laymonage.com/static/"

        fun getAPIService(): APIService {
            return RetrofitClient.getClient(BASE_URL).create(APIService::class.java)
        }
    }
}
