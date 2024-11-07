package org.maternalcare.shared.laravel  // This Laravel Container Folder Name

import org.maternalcare.modules.main.user.model.dto.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("request-token")
    suspend fun requestToken(@Body userDto: UserDto): Response<Any>

    @POST("verify-token")
    suspend fun verifyToken(@Body requestBody: Map<String, String>): Response<Any>

    @POST("reset-password")
    suspend fun resetPassword(@Body requestBody: Map<String, String>): Response<Any>
}