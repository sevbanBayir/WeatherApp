package com.sevban.network.source.remote

import com.sevban.network.model.CharacterDTO
import com.sevban.network.model.CharactersDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: RetrofitService
) : RemoteDataSource {

    override fun getAllCharacters(): Flow<CharactersDTO> = flow {
        emit(apiService.getAllCharacters())
    }


    override fun getCharacterById(id: String): Flow<CharacterDTO> = flow {
        emit(apiService.getCharacterById(id.toInt()))
    }
}