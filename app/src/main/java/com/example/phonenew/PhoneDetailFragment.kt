package com.example.phonenew

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.phonenew.repositories.DataRepository
import com.example.realestateapp.R
import com.example.realestateapp.RetrofitClient
import com.example.realestateapp.database.PhoneDatabase
import com.example.realestateapp.databinding.FragmentPhoneDetailBinding
import com.example.realestateapp.viewmodels.PhoneDetailViewModel
import com.example.realestateapp.viewmodels.PhoneDetailViewModelFactory
import java.net.URL

class PhoneDetailFragment : Fragment() {

    private lateinit var phoneDetailViewModel: PhoneDetailViewModel
    private var phoneId: Int? = null
    private var _binding: FragmentPhoneDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhoneDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        phoneId = arguments?.getInt("phoneId")
        val repository = DataRepository(
            PhoneDatabase.getDatabase(requireContext()).phoneDao(),
            RetrofitClient.instance
        )
        val factory = PhoneDetailViewModelFactory(repository)
        phoneDetailViewModel = ViewModelProvider(this, factory).get(PhoneDetailViewModel::class.java)

        phoneDetailViewModel.phoneDetails.observe(viewLifecycleOwner, Observer { phone ->
            phone?.let {
                LoadImageTask(binding.imageView).execute(phone.image)
                binding.textViewName.text = phone.name
                binding.textViewPrice.text = phone.price.toString()
            }
        })

        binding.fab.setOnClickListener {
            phoneId?.let {
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("info@novaera.cl"))
                    putExtra(Intent.EXTRA_SUBJECT, "Consulta ${binding.textViewName.text} id $it")
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "Hola,\n\nVi la propiedad ${binding.textViewName.text} de código $it y me gustaría que me contactaran a este correo o al siguiente número\n\nQuedo atento."
                    )
                }
                startActivity(emailIntent)
            }
        }

        // OnClickListener para el botón de retroceder
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        phoneId?.let { phoneDetailViewModel.fetchPhoneDetails(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
            if (result != null) {
                imageView.setImageBitmap(result)
            } else {
                imageView.setImageResource(R.drawable.placeholder_image)
            }
        }
    }
}
