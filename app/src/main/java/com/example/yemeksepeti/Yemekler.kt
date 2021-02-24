package com.example.yemeksepeti

import java.io.Serializable

class Yemekler(var yemek_id:Int, var yemek_adi:String, var yemek_resim_adi:String, var yemek_fiyat:Int) : Serializable {
// this class NEEDS to extend Serializable so that its info can be transferred from one activity to another
}