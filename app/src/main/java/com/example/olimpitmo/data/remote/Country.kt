package com.example.olimpitmo.data.remote

import com.google.gson.annotations.SerializedName


data class Country (

  @SerializedName("code" ) var code : String? = null,
  @SerializedName("name" ) var name : String? = null

)