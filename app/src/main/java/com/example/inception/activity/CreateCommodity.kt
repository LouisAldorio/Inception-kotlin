package com.example.inception.activity

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.api.toInput
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.inception.CreateCommodityMutation
import com.example.inception.GetCategoryQuery
import com.example.inception.GetCommodityQuery
import com.example.inception.R
import com.example.inception.adaptor.CommodityAttachmentCreateRecycleViewAdapter
import com.example.inception.adaptor.CommodityCategorySpinnerAdapter
import com.example.inception.api.apolloClient
import com.example.inception.constant.*
import com.example.inception.data.Commodity
import com.example.inception.data.CommodityUser
import com.example.inception.data.UploadParams
import com.example.inception.service.UploadImageIntentService
import com.example.inception.utils.ImageZoomer
import com.vincent.filepicker.Constant
import com.vincent.filepicker.activity.ImagePickActivity
import com.vincent.filepicker.filter.entity.ImageFile
import kotlinx.android.synthetic.main.activity_create_commodity.*
import kotlinx.android.synthetic.main.fragment_commodity_detail.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pub.devrel.easypermissions.EasyPermissions

class CreateCommodity : AppCompatActivity() {
    var Attachments : MutableList<String> = arrayListOf("http://128.199.174.165:8081/photo/DSCF2979-067277033.jpg")
    var AttachmentAdapter : CommodityAttachmentCreateRecycleViewAdapter? = null

    val zoomer = ImageZoomer()

    private val finishUploadReceiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            if(p1?.getStringExtra(UPLOAD_CONTEXT) == "create_commodity_attachment") {
                val uploadedURL = p1?.getStringExtra(UPLOADED_FILE_URL)
                Attachments.add(uploadedURL!!)
                AttachmentAdapter?.notifyDataSetChanged()

                btn_create_commodity.isEnabled = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        zoomer.shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_create_commodity)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val adapter = this.let { ArrayAdapter.createFromResource(it,R.array.spinner_unit_type,android.R.layout.simple_spinner_dropdown_item) }
        spinner_unit_type.adapter = adapter

        lifecycleScope.launch(Dispatchers.Main) {
            val response = try {
                withContext(Dispatchers.IO) {
                    apolloClient(this@CreateCommodity).query(GetCategoryQuery())
                        .await()
                }
            } catch (e: ApolloException) {
                Log.d("Category", "Failure", e)
                null
            }

            if(response?.data?.category_list != null && !response.hasErrors()) {
                progress_bar_commodity_create.visibility = View.GONE
                create_form.visibility = View.VISIBLE

                this@CreateCommodity.let {
                    var adapter: SpinnerAdapter = CommodityCategorySpinnerAdapter(this@CreateCommodity,
                        response?.data?.category_list as List<GetCategoryQuery.Category_list>
                    );

                    spinner_category.adapter = adapter
                }
            }
        }

        var filterUpload = IntentFilter(ACTION_UPLOAD_ATTACHMENT)
        this.registerReceiver(finishUploadReceiver,filterUpload)

        btn_create_commodity.setOnClickListener {
            it.hideKeyboard()

            //collect all data and mutate
            val commodity_name = commodity_name.text.toString()
            if(commodity_name.trim() == ""){
                ToastInvalidInput("Commodity Name must not be empty")
                return@setOnClickListener
            }

            val price = price.text.toString()
            if(price.trim() == ""){
                ToastInvalidInput("Price must not be empty!")
                return@setOnClickListener
            }

            val min_purchase = min_purchase.text.toString()
            if(min_purchase.trim() == ""){
                ToastInvalidInput("Minimum Purchase Number must not be empty!")
                return@setOnClickListener
            }


            val description = description.text.toString()
            if(description.trim() == ""){
                ToastInvalidInput("Description is Needed!")
                return@setOnClickListener
            }

            val unit_type = spinner_unit_type.selectedItem.toString()
            val category_id = spinner_category.selectedItemId

            if (Attachments.size < 2){
                ToastInvalidInput("You need to upload One Photo at least!")
                return@setOnClickListener
            }

            progress_bar_commodity_create.visibility = View.VISIBLE
            create_form.visibility = View.GONE
            CreateCommodity(this,commodity_name,price,min_purchase,unit_type,category_id,description,Attachments)
        }

        //attachment
        val rvAttachments = recycler_view_attachment
        val layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        rvAttachments.layoutManager = layoutManager
        AttachmentAdapter = CommodityAttachmentCreateRecycleViewAdapter(this,Attachments,{

            if(EasyPermissions.hasPermissions(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                val i = Intent(this@CreateCommodity, ImagePickActivity::class.java)
                i.putExtra(Constant.MAX_NUMBER,1)
                startActivityForResult(i, Constant.REQUEST_CODE_PICK_IMAGE)
            }else{
                EasyPermissions.requestPermissions(this,"This application need your permission to access photo gallery.",991,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }

        },{ position : Int, imageView : View ->
            this?.let { ac ->
                zoomer.zoomImageFromThumb(
                    ac,
                    imageView,
                    Attachments[position]!!,
                    container,
                    expanded_image,
                    null
                )
            }
        })
        rvAttachments.adapter = AttachmentAdapter
    }

    private fun CreateCommodity(ctx : Context,commodity_name: String,price: String,min_purchase: String,unit_type: String,category_id: Long, description: String,attachments : MutableList<String>) {
        lifecycleScope.launch(Dispatchers.Main) {
            val response = try {
                withContext(Dispatchers.IO) {
                    apolloClient(ctx).mutate(CreateCommodityMutation(
                        name = commodity_name,
                        min_purchase = min_purchase.toInt(),
                        unit_price = price.toDouble(),
                        unit_type = unit_type,
                        description = description.toInput(),
                        category_id = category_id.toString(),
                        images = attachments.drop(1)
                    )).await()
                }
            } catch (e: ApolloException) {
                Log.d("CommodityList", "Failure", e)
                null
            }

            val createdCommodity = response?.data?.commodity?.create
            if (createdCommodity != null && !response.hasErrors()){
                val returnIntent = Intent()
                returnIntent.putExtra(CREATED_COMMODITY_DATA,Commodity(
                    createdCommodity.name,createdCommodity.image,createdCommodity.unit_price.toString(),createdCommodity.unit_type,createdCommodity.min_purchase.toString(),
                    CommodityUser(createdCommodity.user.username,createdCommodity.user.email,createdCommodity.user.whatsapp,createdCommodity.user.image.link),createdCommodity.description
                ))
                setResult(Activity.RESULT_OK,returnIntent)
                finish()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == Constant.REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null){
            val pickedImg = data?.getParcelableArrayListExtra<ImageFile>(Constant.RESULT_PICK_IMAGE)?.get(0)?.path

            var UploadService = Intent(this, UploadImageIntentService::class.java)
            val temp = UploadParams("DO",pickedImg!!)
            UploadService.putExtra(UPLOAD_CONTEXT, "create_commodity_attachment")
            UploadService.putExtra(UPLOAD_PARAMS,temp)
            UploadImageIntentService.enqueueWork(this,UploadService)

            btn_create_commodity.isEnabled = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.unregisterReceiver(finishUploadReceiver)
    }

    fun ToastInvalidInput(text: String){
        var toast = Toast.makeText(this, text, Toast.LENGTH_SHORT)
        toast.show()
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