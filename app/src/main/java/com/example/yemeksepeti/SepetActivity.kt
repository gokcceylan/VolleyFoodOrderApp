package com.example.yemeksepeti

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_sepet.*
import org.json.JSONException
import org.json.JSONObject

class SepetActivity : AppCompatActivity() {
    private lateinit var sepetListe:ArrayList<Sepettekiler>
    private lateinit var adapter:SepetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sepet)

        toolbarSepet.title = "Sepet"
        toolbarSepet.setTitleTextColor(Color.BLACK)
        setSupportActionBar(toolbarSepet)

        rvSepet.setHasFixedSize(true) // so that it'll look more put together
        rvSepet.layoutManager = LinearLayoutManager(this)

        tumSepet() // if volley wasn't used, this part would contain the val k = ... codes to create the array list and send it to the adapter

    }
    fun tumSepet(){
        val url = "http://kasimadalan.pe.hu/yemekler/tum_sepet_yemekler.php"

        val istek = StringRequest(Request.Method.GET,url, Response.Listener { cevap ->

            try{
                sepetListe = ArrayList()

                val jsonObj = JSONObject(cevap)
                val sepet_yemekler = jsonObj.getJSONArray("sepet_yemekler") // json array name is sepet_yemekler

                for (i in 0 until sepet_yemekler.length()){
                    val k = sepet_yemekler.getJSONObject(i)

                    val yemek_id = k.getInt("yemek_id")
                    val yemek_adi = k.getString("yemek_adi")
                    val yemek_resim_adi = k.getString("yemek_resim_adi")
                    val yemek_fiyat = k.getInt("yemek_fiyat")
                    val yemek_siparis_adet = k.getInt("yemek_siparis_adet")

                    // the jsonobject is used to get one item at each iteration
                    // the jsonobject's attributes are gotten and then sent to the constructor
                    // once Sepettekiler object is created, it is added to the array list
                    val yemek = Sepettekiler(yemek_id, yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet)
                    sepetListe.add(yemek)
                }
                // the array list in question is sent to the adapter
                // adapter is responsible for handling card related actions
                // the adapter of the activity is updated to be the same as the adapter we coded (SepetAdapter instance)
                adapter = SepetAdapter(this, sepetListe)
                rvSepet.adapter = adapter

            }catch (e: JSONException){
                e.printStackTrace()
            }
        }, Response.ErrorListener { Log.e("Hata","Veri okuma") })

        Volley.newRequestQueue(this@SepetActivity).add(istek)
    }
}