package com.example.yemeksepeti

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detay.*

class DetayActivity : AppCompatActivity() {
    private lateinit var yemek: Yemekler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detay)

        toolbarDetay.setTitle("Yemek Detay")
        toolbarDetay.setTitleTextColor(Color.BLACK)
        setSupportActionBar(toolbarDetay)

        yemek = intent.getSerializableExtra("nesne") as Yemekler

        detay_ad.setText(yemek.yemek_adi)
        detay_fiyat.setText(yemek.yemek_fiyat.toString() + " ₺")
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${yemek.yemek_resim_adi}"
        Picasso.get().load(url).into(detay_resim)

        buttonEkle.setOnClickListener {
            val yemekAdet = detay_adet.text.toString() // no need to check because inputType is set to numbers (see activity_detay.xml)
            // only the yemekAdet is gotten from the user. others are yemek object attributes and are already known
            sepeteYemekEkle(yemek.yemek_id, yemek.yemek_adi, yemek.yemek_resim_adi, yemek.yemek_fiyat, yemekAdet)
        }

    }
    fun sepeteYemekEkle(yemek_id: Int, yemek_adi: String, yemek_resim_adi: String, yemek_fiyat: Int, yemek_siparis_adet: String) {
        Log.e("Sepete yemek ekle", "$yemek_id - $yemek_adi - $yemek_resim_adi - $yemek_fiyat - $yemek_siparis_adet")

        val url = "http://kasimadalan.pe.hu/yemekler/insert_sepet_yemek.php"
        // POST is used to update
        val istek = object : StringRequest(Request.Method.POST, url, Response.Listener { cevap ->
            Log.e("Sepete Ekle", cevap)
            startActivity(Intent(this@DetayActivity, SepetActivity::class.java)) // when the food is ordered, cart page is opened
            finish() // so that this page is skipped when clicked on the 'back' button

        }, Response.ErrorListener { Log.e("Ekle", "Hata") }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>() // params work with hashmap
                params["yemek_id"] = yemek_id.toString()
                params["yemek_adi"] = yemek_adi
                params["yemek_resim_adi"] = yemek_resim_adi
                params["yemek_fiyat"] = yemek_fiyat.toString()
                params["yemek_siparis_adet"] = yemek_siparis_adet//.toString()
                return params
            }
        }
        Volley.newRequestQueue(this@DetayActivity).add(istek)
    }
}
