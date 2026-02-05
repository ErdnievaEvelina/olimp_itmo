package com.example.olimpitmo.data.remote

import com.google.gson.annotations.SerializedName


data class AwayTeam (

  @SerializedName("id"      ) var id      : Int?     = null,
  @SerializedName("name"    ) var name    : String?  = null,
  @SerializedName("flashId" ) var flashId : String?  = null,
  @SerializedName("country" ) var country : Country?

)