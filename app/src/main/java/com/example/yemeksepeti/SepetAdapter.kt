package com.example.yemeksepeti

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.lang.Exception

class SepetAdapter(var mContext: Context, var sepetListe:ArrayList<Sepettekiler>) : RecyclerView.Adapter<SepetAdapter.CardTasarimTutucu>() { //CardTasarimTutucu is added
    // Adapter class is for handling card operations
    inner class CardTasarimTutucu(tasarim: View) : RecyclerView.ViewHolder(tasarim){
        var sepet_card : CardView
        var satir_ad : TextView
        var satir_fiyat : TextView
        var satir_toplam_fiyat : TextView
        var satir_adet : TextView
        var satir_resim : ImageView
        var satir_resim_sil : ImageView
        // the CardView attributes need to be declared as such
        // and initialized inside 'init'
        // otherwise card operations cannot be done

        init{
            sepet_card = tasarim.findViewById(R.id.sepet_card)
            satir_ad = tasarim.findViewById(R.id.satir_ad)
            satir_fiyat = tasarim.findViewById(R.id.satir_fiyat)
            satir_toplam_fiyat = tasarim.findViewById(R.id.satir_toplam_fiyat)
            satir_adet = tasarim.findViewById(R.id.satir_adet)
            satir_resim = tasarim.findViewById(R.id.satir_resim)
            satir_resim_sil = tasarim.findViewById(R.id.satir_resim_sil)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucu {
        val tasarim = LayoutInflater.from(mContext).inflate(R.layout.sepet_card_tasarim, parent, false)
        return CardTasarimTutucu(tasarim)
        // connects the design and the code
    }

    override fun getItemCount(): Int {
        return sepetListe.size
    }

    override fun onBindViewHolder(holder: CardTasarimTutucu, position: Int) {
        // position needs to be gotten and a Sepettekiler instance needs to be created to set the design items according to the web services
        val sepet = sepetListe.get(position)

        holder.satir_ad.text = sepet.yemek_adi
        holder.satir_fiyat.text = "Birim Fiyat: ${sepet.yemek_fiyat} ₺"
        holder.satir_adet.text = "Adet: ${sepet.yemek_siparis_adet}"
        holder.satir_toplam_fiyat.text = "${sepet.yemek_fiyat * sepet.yemek_siparis_adet} ₺"
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${sepet.yemek_resim_adi}"
        Picasso.get().load(url).into(holder.satir_resim)

        holder.satir_resim_sil.setOnClickListener {
            val url2 = "http://kasimadalan.pe.hu/yemekler/delete_sepet_yemek.php"

            val istek = object : StringRequest(Method.POST, url2, Response.Listener { cevap ->
                tumSepet()
            }, Response.ErrorListener {  }){
                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["yemek_id"] = sepet.yemek_id.toString() // items are deleted with id - it's about the webservice we're working with
                    return params
                }
            }
            Toast.makeText(mContext, "${sepet.yemek_adi} sepetten silindi.", Toast.LENGTH_SHORT).show()
            Volley.newRequestQueue(mContext).add(istek)
        }

    }
    fun tumSepet(){ //tumSepet method is needed here too
        val url = "http://kasimadalan.pe.hu/yemekler/tum_sepet_yemekler.php"
        val istek = StringRequest(Request.Method.GET, url, Response.Listener { cevap ->
            val tempList = ArrayList<Sepettekiler>()

            try{
                val jsonObj = JSONObject(cevap)
                val sepettekiler = jsonObj.getJSONArray("sepet_yemekler")
                Log.e("TEST", sepettekiler.length().toString())

                for(i in 0 until sepettekiler.length()){
                    val k = sepettekiler.getJSONObject(i)
                    val sepet = Sepettekiler(k.getInt("yemek_id")
                        , k.getString("yemek_adi")
                        , k.getString("yemek_resim_adi")
                        ,k.getInt("yemek_fiyat")
                        , k.getInt("yemek_siparis_adet"))

                    tempList.add(sepet)
                }

            }catch(e: Exception){
                e.printStackTrace()
            }
            sepetListe = tempList
            notifyDataSetChanged()
        }, Response.ErrorListener {  })
        Volley.newRequestQueue(mContext).add(istek)
    }
}