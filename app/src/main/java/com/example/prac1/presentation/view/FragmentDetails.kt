package com.example.prac1.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.prac1.R
import com.example.prac1.app.MyApplication
import com.example.prac1.databinding.FragmentDetailsBinding
import com.example.prac1.domain.model.Flower
import com.example.prac1.presentation.viewmodel.DetailsViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class FragmentDetails : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var detailsViewModel: DetailsViewModel
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity().application as MyApplication).appComponent.inject(this)
        detailsViewModel = ViewModelProvider(this, viewModelFactory)[DetailsViewModel::class.java]

        val itemId = arguments?.getString("itemId")
        if (itemId != null) detailsViewModel.loadItemById(itemId)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailsViewModel.selectedItem.collect { item ->
                    binding.tvFlowerName.text = item?.name
                    binding.tvFlowerPrice.text = item?.price.toString()
                    binding.tvFlowerDescription.text = item?.description
                }
            }
        }

        binding.btnAddToCart.setOnClickListener {
            detailsViewModel.addItemToCart(detailsViewModel.selectedItem.value as Flower)
            findNavController().navigate(R.id.action_details_to_cart)
        }
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}