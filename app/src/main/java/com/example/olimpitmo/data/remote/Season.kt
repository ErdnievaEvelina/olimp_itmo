package com.example.olimpitmo.data.remote

import com.google.gson.annotations.SerializedName


data class Season (

  @SerializedName("uid"    ) var uid    : String? = null,
  @SerializedName("year"   ) var year   : Int?    = null,
  @SerializedName("league" ) var league : League?

)