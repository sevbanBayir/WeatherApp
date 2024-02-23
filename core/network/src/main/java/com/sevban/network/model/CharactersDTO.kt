package com.sevban.network.model


import com.google.gson.annotations.SerializedName

data class CharactersDTO(
    @SerializedName("info")
    val info: Info?,

    @SerializedName("results")
    val results: List<CharacterDTO>?
)