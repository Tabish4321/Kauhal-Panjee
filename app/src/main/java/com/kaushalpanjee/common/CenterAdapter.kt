package com.kaushalpanjee.common

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kaushalpanjee.R
import com.kaushalpanjee.common.model.response.Center
import com.kaushalpanjee.core.util.isNull

class CenterAdapter(private val centers: List<Center>) :
    RecyclerView.Adapter<CenterAdapter.CenterViewHolder>() {

    class CenterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val centerImage: ImageView = view.findViewById(R.id.centerImage)
        val centerName: TextView = view.findViewById(R.id.centerName)
        val address: TextView = view.findViewById(R.id.address)
        val contactNo: TextView = view.findViewById(R.id.contactNo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_training_view, parent, false)
        return CenterViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CenterViewHolder, position: Int) {
        val center = centers[position]

        // Bind text data
        if (center.centerName.isNotEmpty()){

            holder.centerName.text = center.centerName
            holder.address.text = center.address
            holder.contactNo.text = center.contactNo
        }
        else  holder.centerName.text = "No center available"



        // Decode Base64 image
        try {
            val bytes: ByteArray = Base64.decode(center.centerImage, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

            // Set image
            if (bitmap.isNull){
                holder.centerImage.setImageResource(R.drawable.no_training)
            }
            else holder.centerImage.setImageBitmap(bitmap)
        } catch (e: Exception) {
            Log.e("CenterAdapter", "Error decoding Base64 image: ${e.message}")
            holder.centerImage.setImageResource(R.drawable.banner) // Default image
        }
    }

    override fun getItemCount(): Int = centers.size
}
