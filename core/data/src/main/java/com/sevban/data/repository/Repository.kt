package com.sevban.data.repository

import com.sevban.model.Character
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getCharacterList() : Flow<List<Character>>
    fun getCharacterById(id: String) : Flow<Character>
}