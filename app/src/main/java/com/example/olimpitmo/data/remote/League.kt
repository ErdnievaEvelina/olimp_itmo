package com.example.olimpitmo.data.remote

import com.google.gson.annotations.SerializedName


data class League (

  @SerializedName("id"      ) var id      : Int?     = null,
  @SerializedName("name"    ) var name    : String?  = null,
  @SerializedName("country" ) var country : Country?

)