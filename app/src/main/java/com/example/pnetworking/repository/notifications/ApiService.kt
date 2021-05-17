package com.example.pnetworking.repository.notifications

import com.example.pnetworking.models.Sender
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Authorization:key=AAAAe9Dl1T4:APA91bG6-zFtuPT7JCd14H6FOaI-MEixrYBmIVAC6cdsPYF5TLL5ioE-8GyBjDBzGI9co0Z9hd8XyaY9-1SqWeHutj7M-SWY1Ot123rtmXuQNTZKMkZJybHZnTnRXWIjD5AjxgLklm-8",
        "Content-Type:application/json")
    @POST("fcm/send")
     fun postNotification(
        @Body body: Sender
    ): Single<ResponseBody>
}