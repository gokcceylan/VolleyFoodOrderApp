package com.example.yemeksepeti

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.lang.reflect.Method

class YemeklerAdapter(var mContext:Context, var yemekListe:ArrayList<Yemekler>): RecyclerView.Adapter<YemeklerAdapter.CardTasarimTutucu>() {
    inner class CardTasarimTutucu(tasarim:View) : RecyclerView.ViewHolder(tasarim){
        var menu_card : CardView
        var menu_ad : TextView
        var menu_fiyat : TextView
        var menu_resim : ImageView
        var menu_info : ImageView

        init{
            menu_card = tasarim.findViewById(R.id.menu_card)
            menu_ad = tasarim.findViewById(R.id.menu_ad)
            menu_fiyat = tasarim.findViewById(R.id.menu_fiyat)
            menu_resim = tasarim.findViewById(R.id.menu_resim)
            menu_info = tasarim.findViewById(R.id.menu_info)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucu {
        val tasarim = LayoutInflater.from(mContext).inflate(R.layout.menu_card_tasarim, parent, false)
        return CardTasarimTutucu(tasarim)
    }

    override fun getItemCount(): Int {
        return yemekListe.size
    }

    override fun onBindViewHolder(holder: CardTasarimTutucu, position: Int) {
        val yemek = yemekListe.get(position)

        holder.menu_ad.text = yemek.yemek_adi
        holder.menu_fiyat.text = "${yemek.yemek_fiyat} ₺"
        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${yemek.yemek_resim_adi}"
        Picasso.get().load(url).into(holder.menu_resim)

        holder.menu_card.setOnClickListener{
            val intent = Intent(mContext, DetayActivity::class.java)
            intent.putExtra("nesne", yemek)
            mContext.startActivity(intent)

        }
    }
}