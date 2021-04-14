package com.example.inception.activity

import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inception.R
import com.example.inception.adaptor.PhoneContactRecycleViewAdpater
import com.example.inception.data.PhoneContact
import kotlinx.android.synthetic.main.activity_contact_activtity.*

class ContactActivtity : AppCompatActivity() , LoaderManager.LoaderCallbacks<Cursor>{

    var DisplayName = ContactsContract.Contacts.DISPLAY_NAME
    var Number = ContactsContract.CommonDataKinds.Phone.NUMBER
    var ListContact : MutableList<PhoneContact> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_activtity)

        LoaderManager.getInstance(this).initLoader(20123,null,this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        var MyContactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        var MyProjection = arrayOf(DisplayName,Number)

        return CursorLoader(this,MyContactUri,MyProjection,null,null,"${DisplayName} ASC")
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        ListContact.clear()
        if(data != null) {
            data.moveToFirst()
            while (!data.isAfterLast){
                ListContact.add(
                    PhoneContact(
                        data.getString(data.getColumnIndex(DisplayName)),
                        "","","",
                        data.getString(data.getColumnIndex(Number))
                    )
                )
                data.moveToNext()
            }

            contact_recycleview.apply {
                adapter = PhoneContactRecycleViewAdpater(ListContact)
                layoutManager = LinearLayoutManager(this@ContactActivtity)
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        contact_recycleview.adapter?.notifyDataSetChanged()
    }
}