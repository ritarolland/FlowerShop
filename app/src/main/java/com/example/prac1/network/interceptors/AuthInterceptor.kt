package com.example.prac1.network.interceptors

import com.example.prac1.domain.auth.TokenProvider
import com.example.prac1.network.annotations.RequiresAuthorization
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

class AuthInterceptor(
    private val tokenProvider: TokenProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val method = request.tag(Invocation::class.java)?.method()
        val token = tokenProvider.getToken()
        val requiresAuth = method?.getAnnotation(RequiresAuthorization::class.java) != null

        if(requiresAuth) {
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        }

        var response = chain.proceed(request)

        if (response.code == 401) {  // Unauthorized
            if (tokenProvider.refreshToken()) {
                val newToken = tokenProvider.getToken()
                request = request.newBuilder()
                    .addHeader("Authorization", "Bearer $newToken")
                    .build()
                response.close()
                response = chain.proceed(request)
            }
        }

        return response
    }
}