package com.example.yemeksepeti

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var yemeklerListe:ArrayList<Yemekler>
    private lateinit var adapter:YemeklerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbarMenu.title = "Menü"
        toolbarMenu.setTitleTextColor(Color.BLACK)
        setSupportActionBar(toolbarMenu)

        menuRv.setHasFixedSize(true)
        menuRv.layoutManager = LinearLayoutManager(this)

        tumYemekler()
        fab.setOnClickListener {
            startActivity(Intent(this@MainActivity,SepetActivity::class.java))
        }
    }
    override fun onBackPressed() {
        val yeniIntent = Intent(Intent.ACTION_MAIN)
        yeniIntent.addCategory(Intent.CATEGORY_HOME)
        yeniIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(yeniIntent)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_arama_menu,menu)

        val item = menu.findItem(R.id.action_ara)
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        Log.e("Gönderilen Arama Sonucu",query)
        aramaYap(query)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        Log.e("Harf Girdikçe Sonuc",newText)
        aramaYap(newText)
        return true
    }
    fun tumYemekler(){
        val url = "http://kasimadalan.pe.hu/yemekler/tum_yemekler.php" //url is added

        val istek = StringRequest(Request.Method.GET, url, Response.Listener { cevap ->

            try{
                yemeklerListe = java.util.ArrayList()
                val jsonObj = JSONObject(cevap) // answer will be given to us as a JSON object by restful
                val yemekler = jsonObj.getJSONArray("yemekler")

                for(i in 0 until yemekler.length()){
                    val k = yemekler.getJSONObject(i)

                    val yemek_id = k.getInt("yemek_id")
                    val yemek_adi = k.getString("yemek_adi")
                    val yemek_resim_adi = k.getString("yemek_resim_adi")
                    val yemek_fiyat = k.getInt("yemek_fiyat")

                    val yemek = Yemekler(yemek_id, yemek_adi, yemek_resim_adi, yemek_fiyat)
                    yemeklerListe.add(yemek) // yemek object needs to be created and added to the array list
                }

                adapter = YemeklerAdapter(this, yemeklerListe) // yemeklerListe is sent to adapter
                menuRv.adapter = adapter

            }catch(e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener{ Log.e("Hata", "Veri okuma")}) // we want to see the error as a log message

        Volley.newRequestQueue(this@MainActivity).add(istek)
    }

    fun aramaYap(aramaKelime:String){
        val url = "http://kasimadalan.pe.hu/yemekler/tum_yemekler_arama.php"
        val istek = object : StringRequest(Request.Method.POST, url, Response.Listener { cevap ->
            try{
                yemeklerListe = java.util.ArrayList()
                val jsonObj = JSONObject(cevap) // answer will be given to us as a JSON object by restful
                val yemekler = jsonObj.getJSONArray("yemekler")

                for(i in 0 until yemekler.length()){
                    val k = yemekler.getJSONObject(i)

                    val yemek_id = k.getInt("yemek_id")
                    val yemek_adi = k.getString("yemek_adi")
                    val yemek_resim_adi = k.getString("yemek_resim_adi") // not entirely sure about this one
                    val yemek_fiyat = k.getInt("yemek_fiyat")

                    val yemek = Yemekler(yemek_id, yemek_adi, yemek_resim_adi, yemek_fiyat)
                    yemeklerListe.add(yemek) // yemek object needs to be created and added to the array list
                }

                adapter = YemeklerAdapter(this, yemeklerListe) // yemeklerListe is sent to adapter

                menuRv.adapter = adapter

            }catch(e: JSONException) {
                e.printStackTrace()
            }
        },Response.ErrorListener{ Log.e("Hata", "Veri okuma")}){ // we want to see the error as a log message
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                // Hostinger web service asks for yemek_adi
                params["yemek_adi"] = aramaKelime
                return params
            }
        }
        Volley.newRequestQueue(this@MainActivity).add(istek)
    }
}