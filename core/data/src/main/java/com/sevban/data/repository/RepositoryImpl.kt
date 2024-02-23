package com.sevban.data.repository

import com.sevban.data.mapper.toCharacter
import com.sevban.model.Character
import com.sevban.network.source.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : Repository {
    override fun getCharacterList(): Flow<List<Character>> =
        remoteDataSource.getAllCharacters().map {
            it.results!!.map { characterDTO ->
                characterDTO.toCharacter()
            }
        }


    override fun getCharacterById(id: String): Flow<Character> =
        remoteDataSource.getCharacterById(id).map { characterDTO ->
            characterDTO.toCharacter()
        }

}

// TODO: Naming for repository, dataSources etc. should be reconsidered.