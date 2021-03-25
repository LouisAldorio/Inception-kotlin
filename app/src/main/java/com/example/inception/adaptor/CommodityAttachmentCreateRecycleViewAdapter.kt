package com.example.inception.adaptor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import com.example.inception.R
import com.example.inception.activity.CreateCommodity
import com.example.inception.constant.UPLOAD_PARAMS
import com.example.inception.data.UploadParams
import com.example.inception.service.UploadImageIntentService
import com.example.inception.utils.ImageZoomer
import com.squareup.picasso.Picasso
import com.vincent.filepicker.Constant
import com.vincent.filepicker.activity.ImagePickActivity
import com.vincent.filepicker.filter.entity.ImageFile
import kotlinx.android.synthetic.main.commodity_create_attachment_layout.view.*
import pub.devrel.easypermissions.EasyPermissions

class CommodityAttachmentCreateRecycleViewAdapter(var mContext: Context,
                                                  private val attachments: List<String>,
                                                  private val callback : () -> Unit,
                                                  private val zoomer : (position: Int, thumbView: View) -> Unit) : RecyclerView.Adapter<AttachmentHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentHolder {
        return AttachmentHolder(LayoutInflater.from(parent.context).inflate(R.layout.commodity_create_attachment_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return attachments.size
    }

    override fun onBindViewHolder(holder: AttachmentHolder, position: Int) {
        Picasso.get().load(attachments[position]).into(holder.attachment)

        if(position == 0){
            holder.attachment.setOnClickListener {
                callback()
            }
        }else {
            holder.attachment.setOnClickListener {
                zoomer(position,holder.attachment)
            }
        }
    }

}


class AttachmentHolder(view: View) : RecyclerView.ViewHolder(view) {
    val attachment = view.attachment_commodity_create
}