package com.example.olimpitmo.data.remote

import com.google.gson.annotations.SerializedName


data class DatesMatch (

  @SerializedName("id"           ) var id           : Int?      = null,
  @SerializedName("flashId"      ) var flashId      : String?   = null,
  @SerializedName("date"         ) var date         : String?   = null,
  @SerializedName("dateUtc"      ) var dateUtc      : Int?      = null,
  @SerializedName("status"       ) var status       : Int?      = null,
  @SerializedName("statusName"   ) var statusName   : String?   = null,
  @SerializedName("homeResult"   ) var homeResult   : String?   = null,
  @SerializedName("awayResult"   ) var awayResult   : String?   = null,
  @SerializedName("homeHTResult" ) var homeHTResult : String?   = null,
  @SerializedName("awayHTResult" ) var awayHTResult : String?   = null,
  @SerializedName("homeFTResult" ) var homeFTResult : String?   = null,
  @SerializedName("awayFTResult" ) var awayFTResult : String?   = null,
  @SerializedName("homeTeam"     ) var homeTeam     : HomeTeam? ,
  @SerializedName("awayTeam"     ) var awayTeam     : AwayTeam?,
  @SerializedName("season"       ) var season       : Season? ,
  @SerializedName("roundName"    ) var roundName    : String?   = null

)