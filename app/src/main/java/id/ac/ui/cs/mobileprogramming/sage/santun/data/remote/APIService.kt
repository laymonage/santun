package id.ac.ui.cs.mobileprogramming.sage.santun.data.remote

import retrofit2.http.*

interface APIService {
    @GET("/messages")
    suspend fun getMessages(): List<MessageBody>

    @POST("/messages/create")
    suspend fun createMessage(@Body message: MessageBody): MessageBody
}
