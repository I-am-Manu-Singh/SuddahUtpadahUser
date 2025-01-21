package com.neatroots.suddahutpadah.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.neatroots.suddahutpadah.adapter.ServiceAdapter
import com.neatroots.suddahutpadah.databinding.FragmentServiceBinding
import com.neatroots.suddahutpadah.factory.MainViewModelFactory
import com.neatroots.suddahutpadah.model.ProductModel
import com.neatroots.suddahutpadah.repository.MainRepository
import com.neatroots.suddahutpadah.utils.Utils
import com.neatroots.suddahutpadah.utils.Utils.gone
import com.neatroots.suddahutpadah.utils.Utils.visible
import com.neatroots.suddahutpadah.viewmodel.MainViewModel

class ServiceFragment : Fragment(), ServiceAdapter.OnItemClickListener {
    private val binding by lazy { FragmentServiceBinding.inflate(layoutInflater) }
    private lateinit var progress: AlertDialog
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: ServiceAdapter
    private val category: ServiceFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        progress = Utils.showLoading(requireContext())
        val repository = MainRepository(requireContext())
        val factory = MainViewModelFactory(repository)
        mainViewModel = ViewModelProvider(this@ServiceFragment, factory) [MainViewModel::class.java]
        adapter = ServiceAdapter(this)

        binding.apply {

            tvCategoryName.text = category.category.categoryName

            mainViewModel.servicesList.observe(viewLifecycleOwner) { list ->
                val dataList = list.filter { it.categoryId == category.category.id }
                if(dataList.isNotEmpty()) {
                    loadingLayout.gone()
                    mainLayout.visible()
                    rv.layoutManager = GridLayoutManager(requireContext(), 2)
                    rv.adapter = adapter
                    adapter.submitList(dataList)
                    search(dataList)
                } else {
                    loadingLayout.visible()
                    mainLayout.gone()
                    tvStatus.gone()
                    animationView3.setAnimation("coming_soon.json")
                    animationView3.playAnimation()
                }

            }

        }

    }


    private fun search(list: List<ProductModel>) {
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(list.isNotEmpty()) {
                    filteredList(newText, list)
                }
                return true
            }

        })

    }

    private fun filteredList(newText: String?, list: List<ProductModel>) {
        val filteredList = ArrayList<ProductModel>()
        for (service in list) {
            if (service.title.contains(newText.orEmpty(), ignoreCase = true) ||
                service.description.contains(newText.orEmpty(), ignoreCase = true) ||
                service.offerPrice.contains(newText.orEmpty(), ignoreCase = true) ||
                service.originalPrice.contains(newText.orEmpty(), ignoreCase = true)
            )
                filteredList.add(service)
        }
        adapter.submitList(filteredList)
        binding.rv.adapter = adapter

    }

    override fun onItemClick(model: ProductModel) {
        val action = ServiceFragmentDirections.actionServiceFragmentToServiceDetailsFragment(model)
        findNavController().navigate(action)
    }

}