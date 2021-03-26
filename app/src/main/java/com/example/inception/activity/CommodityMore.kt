package com.example.inception.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.inception.GetCommodityByCategoryQuery
import com.example.inception.GetCommodityQuery
import com.example.inception.R
import com.example.inception.adaptor.CommodityGridViewAdapter
import com.example.inception.api.apolloClient
import com.example.inception.constant.CATEGORY_ID
import kotlinx.android.synthetic.main.activity_commodity_more.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CommodityMore : AppCompatActivity() {

    var adapter: CommodityGridViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_commodity_more)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val category_id = intent.getStringExtra(CATEGORY_ID)

        lifecycleScope.launch(Dispatchers.Main) {
            val response = try {
                withContext(Dispatchers.IO) {
                    apolloClient(this@CommodityMore).query(GetCommodityByCategoryQuery(category_id!!.toInt()))
                        .await()
                }
            } catch (e: ApolloException) {
                Log.d("CommodityList", "Failure", e)
                null
            }
            val commodities = response!!.data!!.comodities_by_category
            if (commodities != null && !response.hasErrors()){
                commodity_more_progress_bar.visibility = View.GONE
                adapter = CommodityGridViewAdapter(this@CommodityMore,
                    commodities as List<GetCommodityByCategoryQuery.Comodities_by_category>
                )
                commodity_more_GV.adapter = adapter
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}