package com.example.olimpitmo.data.remote


data class Matches (

  val status  : String?         = null,
  val count   : Int?            = null,
  val data    : List<DatesMatch>,
  val offset  : Int?            = null,
  val traceId : String?         = null

)