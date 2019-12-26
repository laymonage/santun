package id.ac.ui.cs.mobileprogramming.sage.santun.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface APIService {
    @GET("/messages")
    suspend fun getMessages(): List<MessageBody>

    @POST("/messages/create")
    suspend fun createMessage(@Body message: MessageBody): Response<MessageBody>

    @Multipart
    @POST("images/upload")
    suspend fun upload(@Part("uuid") uuid: RequestBody, @Part file: MultipartBody.Part): Response<ResponseBody>
}
