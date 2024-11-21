package com.example.phonenew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.phonenew.repositories.DataRepository
import com.example.phonenew.viewmodels.PhoneListViewModel
import com.example.realestateapp.PhoneListAdapter
import com.example.realestateapp.R
import com.example.realestateapp.RetrofitClient
import com.example.realestateapp.database.PhoneDatabase
import com.example.realestateapp.databinding.FragmentPhoneListBinding
import com.example.realestateapp.viewmodels.PhoneListViewModelFactory

class PhoneListFragment : Fragment() {
    private lateinit var phoneListViewModel: PhoneListViewModel
    private lateinit var adapter: PhoneListAdapter
    private var _binding: FragmentPhoneListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhoneListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = DataRepository(
            PhoneDatabase.getDatabase(requireContext()).phoneDao(),
            RetrofitClient.instance
        )
        val factory = PhoneListViewModelFactory(repository)
        phoneListViewModel = ViewModelProvider(this, factory).get(PhoneListViewModel::class.java)

        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)

        adapter = PhoneListAdapter(emptyList()) { phone ->
            val bundle = Bundle().apply {
                putInt("phoneId", phone.id)
            }
            findNavController().navigate(R.id.action_phoneListFragment_to_phoneDetailFragment, bundle)
        }
        binding.recyclerView.adapter = adapter

        phoneListViewModel.allPhones.observe(viewLifecycleOwner, Observer { phones ->
            phones?.let {
                adapter = PhoneListAdapter(it) { phone ->
                    val bundle = Bundle().apply {
                        putInt("phoneId", phone.id)
                    }
                    findNavController().navigate(R.id.action_phoneListFragment_to_phoneDetailFragment, bundle)
                }
                binding.recyclerView.adapter = adapter
            }
        })

        phoneListViewModel.refreshPhones()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
