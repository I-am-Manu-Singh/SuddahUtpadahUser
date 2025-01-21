package com.neatroots.suddahutpadah.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.neatroots.suddahutpadah.adapter.OrderHistoryAdapter
import com.neatroots.suddahutpadah.databinding.FragmentOrderHistoryBinding
import com.neatroots.suddahutpadah.factory.MainViewModelFactory
import com.neatroots.suddahutpadah.repository.MainRepository
import com.neatroots.suddahutpadah.utils.Utils.gone
import com.neatroots.suddahutpadah.utils.Utils.visible
import com.neatroots.suddahutpadah.viewmodel.MainViewModel

class OrderHistoryFragment : Fragment() {
    private val binding by lazy { FragmentOrderHistoryBinding.inflate(layoutInflater) }
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: OrderHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = MainRepository(requireContext())
        val factory = MainViewModelFactory(repository)
        mainViewModel = ViewModelProvider(requireActivity(), factory) [MainViewModel::class.java]
        adapter = OrderHistoryAdapter()

        binding.apply {



            mainViewModel.orderHistory.observe(viewLifecycleOwner) { list ->
                if (list.isNotEmpty()) {
                    loadingLayout.gone()
                    mainLayout.visible()
                    rv.adapter = adapter
                    adapter.submitList(list)
                    //search(list)
                } else {
                    loadingLayout.visible()
                    mainLayout.gone()
                    tvStatus.text = "No History Found"
                }


            }
        }
    }

}