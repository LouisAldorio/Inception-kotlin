package com.example.inception.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.inception.R
import com.example.inception.data.PhoneContact
import kotlinx.android.synthetic.main.phoe_contact_recycleview_item.view.*

class PhoneContactRecycleViewAdpater(private val contact: List<PhoneContact>): RecyclerView.Adapter<ContactHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        return ContactHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.phoe_contact_recycleview_item,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return contact.size
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bindContact(contact[position])
    }
}

class ContactHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val contactAvatar = view.contact_avatar
    private val contactFirstName = view.first_name
    private val contactLastName = view.last_name
    private val contactEmail = view.email
    private val contactPhoneNumber = view.phone_number

    fun bindContact(contact: PhoneContact) {
        contactFirstName.text = contact.first_name
        contactLastName.text = contact.last_name
        contactEmail.text = contact.email
        contactPhoneNumber.text = contact.phone_number
    }
}