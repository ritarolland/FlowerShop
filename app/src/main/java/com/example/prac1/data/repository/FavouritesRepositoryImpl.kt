package com.example.prac1.data.repository

import android.util.Log
import com.example.prac1.data.api.FlowerApi
import com.example.prac1.data.api.model.FavouriteDataModel
import com.example.prac1.domain.mapper.FlowerMapper
import com.example.prac1.domain.model.Flower
import com.example.prac1.domain.repository.FavouritesRepository
import com.example.prac1.domain.repository.TokenRepository
import com.example.prac1.domain.repository.UserUidRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

class FavouritesRepositoryImpl @Inject constructor(
    private val api: FlowerApi,
    private val uidRepository: UserUidRepository,
    private val tokenRepository: TokenRepository
) : FavouritesRepository {

    private val _favourites = MutableStateFlow<List<FavouriteDataModel>>(emptyList())
    private val ioScope = CoroutineScope(Dispatchers.IO)

    init {
        ioScope.launch {
            loadFavourites()
            Log.d("FAVOURITES", _favourites.value.toString())
        }
    }

    private suspend fun loadFavourites() {
        val uid = "eq.${uidRepository.getUserUid()}"
        tokenRepository.executeApiCall(
            apiCall = {
                api.loadFavourites(
                    token = tokenRepository.createAuthHeader(),
                    userId = uid
                )
            },
            onSuccess = { response ->
                response.body()?.let {
                    _favourites.value = it
                }
            },
            onError = { _favourites.value = emptyList() }
        )

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getFavouriteFlowers(): Flow<List<Flower>> =
        _favourites.flatMapLatest { favourites ->
            flow {
                val favouriteFlowers = mutableListOf<Flower>()
                for (favourite in favourites) {
                    val response = api.getFlowerById(flowerId = "eq.${favourite.flower_id}", token = tokenRepository.createAuthHeader())
                    if (response.isSuccessful && response.body() != null) {
                        val flower = response.body()?.firstOrNull()?.let {
                            FlowerMapper.mapToDomain(
                                it
                            )
                        }
                        if (flower != null) favouriteFlowers.add(flower)
                    }
                }
                Log.d("FAVOURITES", "flowers: $favouriteFlowers")
                emit(favouriteFlowers)
            }
        }

    private fun addFavourite(flowerId: String) {
        ioScope.launch {
            val userId = uidRepository.getUserUid()
            if (userId != null)
            tokenRepository.executeApiCall(
                apiCall = {
                    api.addFavourite(tokenRepository.createAuthHeader(), FavouriteDataModel(
                        id = UUID.randomUUID().toString(),
                        user_id = userId,
                        flower_id = flowerId
                    ))
                },
                onSuccess = {
                    ioScope.launch {
                        loadFavourites()
                    }
                }
            )
        }
    }

    private fun deleteFavourite(flowerId: String) {
        ioScope.launch {
            val userId = uidRepository.getUserUid()
            val id = _favourites.value.find { it.flower_id == flowerId && it.user_id == userId}?.id
            if (id != null) {
                tokenRepository.executeApiCall(
                    apiCall = {
                        api.deleteFavourite(tokenRepository.createAuthHeader(), "eq.$id")
                    },
                    onSuccess = {
                        ioScope.launch { loadFavourites() }
                    },
                    onError = { message ->
                        Log.d("FAVOURITE", "Error deleting: $message")
                    }
                )
            }
        }
    }

    override fun toggleIsFavourite(flowerId: String) {
        if(flowerId in _favourites.value.map { it.flower_id }) {
            deleteFavourite(flowerId)
        } else {
            addFavourite(flowerId)
        }
    }

}