package com.example.inception.fragment

import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.inception.R
import com.example.inception.utils.DownloadImageAndSaveToInternalStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_schedule.view.*
import java.io.File

class ScheduleFragment : Fragment() {

    var url = "https://fileserver.louisaldorio.site/photo/IMG_5034-555187739.JPG"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var objectView = inflater.inflate(R.layout.fragment_schedule, container, false)

        objectView.download_button.setOnClickListener {
            DownloadImageAndSaveToInternalStorage(requireContext()).execute(url)
        }

        objectView.view_button.setOnClickListener {
            readFileFromInternalStorage()
        }

        return objectView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Picasso.get().load(url).into(view.temp)
    }

    private fun readFileFromInternalStorage() {

        var mypath = File(requireActivity().filesDir, "Images")
//        mypath = File(mypath,"img.jpg")

        if (mypath.listFiles().size != 0){
            for(file in mypath.listFiles()) {
                Log.i("Files",file.toString())
            }
        }

//        val imageView: ImageView = requireView().findViewById(R.id.temp) as ImageView
//        Log.i("Images",mypath.toString())
//        imageView.setImageDrawable(Drawable.createFromPath(mypath.toString()))
    }



}