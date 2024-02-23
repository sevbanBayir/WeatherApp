package com.sevban.domain.usecase

import com.sevban.data.repository.Repository
import com.sevban.model.Character
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharacterUseCase @Inject constructor(
    private val repository: Repository
){
    fun execute(id: String) : Flow<Character> = repository.getCharacterById(id)
}