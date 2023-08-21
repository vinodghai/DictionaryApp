package com.example.dictionaryapp.core.util

import com.example.dictionaryapp.core.parser.JsonParser
import com.example.dictionaryapp.feature.dictionary.data.remote.dto.ErrorDto
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException


class SafeApiCall(
    private val jsonParser: JsonParser
) {

    suspend operator fun <T> invoke(apiCall: suspend () -> Response<T>): Resource<T> =
        try {
            val response: Response<T> = apiCall()

            if (response.isSuccessful && response.body() != null) {
                Resource.Success(data = response.body()!!)
            } else {
                val errorResponse = convertErrorBody(response.errorBody())
                Resource.Error(errorResponse.toError())
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()
            Resource.Error(convertErrorBody(errorBody).toError())
        } catch (e: IOException) {
            val errorDto = ErrorDto(
                message = "Unable to access the internet",
                resolution = "Please connect to the internet",
                title = "No internet connection"
            )
            Resource.Error(errorDto.toError())
        } catch (e: Exception) {
            Resource.Error(defaultError().toError())
        }

    private fun convertErrorBody(errorBody: ResponseBody?): ErrorDto {
        return if (errorBody == null)
            defaultError()
        else
            jsonParser.fromJson(errorBody.string(), ErrorDto::class.java) ?: defaultError()
    }

    private fun defaultError(): ErrorDto = ErrorDto(
        message = "Something went wrong!",
        resolution = "Try again!",
        title = "Error"
    )
}