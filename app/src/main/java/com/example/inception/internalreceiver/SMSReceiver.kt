package com.example.inception.internalreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast


//berhubung aplikasi kami tidak mengugnakan internal receiver, maka kami akan mengimplementasikan sms receiver
class SMSReceiver : BroadcastReceiver() {

    //override fungsi onReceive
    override fun onReceive(context: Context, intent: Intent) {
        //cek apakah action yang diterima merupakan action dari broadcast SMS
        if(intent.action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)){

            //ambil format pdunya
            val pdu = (intent.extras!!.get("pdus") as Array<*>).get(0)
            //ambil data
            val Bundle = intent.extras
            // ambil format nya
            val format = Bundle!!.getString("format")


            pdu.let {
                //mengubah byte array menjadi format yang nantinya dapat ktia akases body dan komponen SMS lainya
                val message = SmsMessage.createFromPdu(it as ByteArray,format)
                //ambil pesan
                val pesan = message.displayMessageBody
                //ambil nomor pengirim
                val no_pengirim = message.displayOriginatingAddress

                //lakukan toast untuk melihat siapa pengirim dan isi pesan
                Toast.makeText(context,"Phone : $no_pengirim \n" +
                        "Message : $pesan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}