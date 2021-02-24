package com.example.yemeksepeti

import java.io.Serializable

class Sepettekiler(var yemek_id:Int, var yemek_adi:String, var yemek_resim_adi:String, var yemek_fiyat:Int, var yemek_siparis_adet: Int) : Serializable {
    // for transitioning info from one activity to another
}