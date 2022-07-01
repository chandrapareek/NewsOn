 package com.example.newson

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest


 class MainActivity : AppCompatActivity(), NewsItemClicked {
     private lateinit var mAdapter: NewsOnAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rcview = findViewById<RecyclerView>(R.id.RecyclerView)
        rcview.layoutManager = LinearLayoutManager(this)
        fetchdata()
        mAdapter = NewsOnAdapter(this)
        rcview.adapter = mAdapter
    }

     private fun fetchdata() {
         val url = "https://newsdata.io/api/1/news?apikey=pub_8544b06640f25ff9a4af8b863dbdc4e6b956&country=in,us"
         val jsonObjectRequest = JsonObjectRequest(
             Request.Method.GET,
             url, null,
             {
                val newsJsonarray = it.getJSONArray("results")
                 val newsarray = ArrayList<News>()
                 for(i in 0 until newsJsonarray.length()){
                     val newsJsonObject = newsJsonarray.getJSONObject(i)
                     val news = News(
                         newsJsonObject.getString("title"),
                         newsJsonObject.getString("link"),
                         newsJsonObject.getString("creator"),
                         newsJsonObject.getString("image_url"),
                     )

                     newsarray.add(news)
                 }
                    mAdapter.updateNews(newsarray)
             },
             {

             }

         )

         MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)


     }

     override fun onItemClicked(item: News) {
         val builder = CustomTabsIntent.Builder()
         val customTabsIntent = builder.build()
         customTabsIntent.launchUrl(this, Uri.parse(item.link))
     }
 }