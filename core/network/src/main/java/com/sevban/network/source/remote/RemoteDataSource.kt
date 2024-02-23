package com.sevban.network.source.remote

import com.sevban.network.model.CharacterDTO
import com.sevban.network.model.CharactersDTO
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    fun getAllCharacters(): Flow<CharactersDTO>
    fun getCharacterById(id: String): Flow<CharacterDTO>
}