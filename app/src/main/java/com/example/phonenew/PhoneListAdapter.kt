package com.example.realestateapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.realestateapp.databinding.ItemPhoneBinding
import com.example.realestateapp.models.Phone
import java.net.URL

class PhoneListAdapter(private val phones: List<Phone>, private val clickListener: (Phone) -> Unit) :
    RecyclerView.Adapter<PhoneListAdapter.PhoneViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneViewHolder {
        val binding = ItemPhoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhoneViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhoneViewHolder, position: Int) {
        holder.bind(phones[position], clickListener)
    }

    override fun getItemCount() = phones.size

    class PhoneViewHolder(private val binding: ItemPhoneBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(phone: Phone, clickListener: (Phone) -> Unit) {
            binding.phoneName.text = phone.name
            binding.phonePrice.text = phone.price.toString()
            LoadImageTask(binding.phoneImage).execute(phone.image)
            binding.root.setOnClickListener {
                val bundle = Bundle().apply {
                    putInt("phoneId", phone.id)
                }
                binding.root.findNavController().navigate(R.id.action_phoneListFragment_to_phoneDetailFragment, bundle)
            }
        }

        private class LoadImageTask(val imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {
            override fun doInBackground(vararg urls: String): Bitmap? {
                return try {
                    val input = URL(urls[0]).openStream()
                    BitmapFactory.decodeStream(input)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }

            override fun onPostExecute(result: Bitmap?) {
                imageView.setImageBitmap(result)
            }
        }
    }
}
