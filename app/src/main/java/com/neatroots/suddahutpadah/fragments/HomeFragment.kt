package com.neatroots.suddahutpadah.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.neatroots.suddahutpadah.R
import com.neatroots.suddahutpadah.activities.WelcomeActivity
import com.neatroots.suddahutpadah.adapter.CategoryAdapter
import com.neatroots.suddahutpadah.databinding.DialogExitBinding
import com.neatroots.suddahutpadah.databinding.FragmentHomeBinding
import com.neatroots.suddahutpadah.databinding.NavigationLayoutBinding
import com.neatroots.suddahutpadah.factory.AuthViewModelFactory
import com.neatroots.suddahutpadah.factory.MainViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.neatroots.suddahutpadah.adapter.ServiceAdapter
import com.neatroots.suddahutpadah.model.CategoryModel
import com.neatroots.suddahutpadah.model.ProductModel
import com.neatroots.suddahutpadah.model.UserModel
import com.neatroots.suddahutpadah.repository.AuthRepository
import com.neatroots.suddahutpadah.repository.MainRepository
import com.neatroots.suddahutpadah.utils.SharedPref
import com.neatroots.suddahutpadah.utils.Utils
import com.neatroots.suddahutpadah.utils.Utils.gone
import com.neatroots.suddahutpadah.utils.Utils.visible
import com.neatroots.suddahutpadah.viewmodel.AuthViewModel
import com.neatroots.suddahutpadah.viewmodel.MainViewModel
import java.util.Calendar


class HomeFragment : Fragment(), CategoryAdapter.OnItemClickListener, ServiceAdapter.OnItemClickListener {
    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private lateinit var user: UserModel
    private lateinit var bottomSheet: BottomSheetDialog
    private lateinit var adapter: CategoryAdapter
    private lateinit var productAdapter: ServiceAdapter
    private lateinit var authViewModel: AuthViewModel
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = SharedPref.getUserData(requireContext()) ?: UserModel()
        bottomSheet = BottomSheetDialog(requireContext(), R.style.CustomBottomSheetStyle)

        val repository = AuthRepository(FirebaseAuth.getInstance(),  requireContext())
        val factory = AuthViewModelFactory(repository)
        authViewModel = ViewModelProvider(requireActivity(), factory)[AuthViewModel::class.java]

        val mainRepository = MainRepository(requireContext())
        val factory2 = MainViewModelFactory(mainRepository)
        mainViewModel = ViewModelProvider(requireActivity(), factory2)[MainViewModel::class.java]


        mainViewModel.fetchCategories()


        binding.apply {

            tvWhish.text = getGreeting()

            btMenu.setOnClickListener {
                showMoreOptions()
            }

//            ivProfile.load(user.hsImage1) {
//                placeholder(R.drawable.placeholder)
//                error(R.drawable.placeholder)
//            }

            tvCityName.text = if(user.city == "") "City Name" else user.city
            tvHomestayName.text = if(user.name == "") "New User" else user.name
            adapter = CategoryAdapter(this@HomeFragment)
            productAdapter = ServiceAdapter(this@HomeFragment)

            ivProfile.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
            }

            tvHomestayName.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
            }

            mainViewModel.categoryList.observe(viewLifecycleOwner) { list ->
                if(list.isNotEmpty()) {
                    //mainViewModel.fetchCategoryServices(list[0].id)
                    //tvCategoryTitle.text = list[0].categoryName
                    fetchCategoryItems(list)
                    loadingLayout.gone()
                    homeMainLayout.visible()
                    rv.adapter = adapter
                    adapter.submitList(list)

                } else {
                    loadingLayout.visible()
                    homeMainLayout.gone()
                    tvStatus.text = "No Categories Found"
                }

            }



        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(bottomSheet.isShowing) {
                    bottomSheet.dismiss()
                } else {
                    showExitDialog()
                }
            }

        })

    }

    private fun fetchCategoryItems(list: List<CategoryModel>) {

        mainViewModel.servicesList.observe(viewLifecycleOwner) { lists ->
            if(list.isNotEmpty()) {
//                val filteredList = lists.filter { it.categoryId == list[0].id }
                binding.productRv.layoutManager = GridLayoutManager(requireContext(), 2)
                binding.productRv.adapter = productAdapter
                productAdapter.submitList(lists.shuffled())
                binding.tvRvLoadingStatus.gone()
                binding.productRv.visible()
                binding.tvCategoryTitle.visible()
                search(lists)
            } else {
                binding.tvRvLoadingStatus.text = "No Products Found"
            }

        }
    }

    private fun getGreeting(): String {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 0..11 -> "Hi, Good Morning"
            in 12..16 -> "Hi, Good Afternoon"
            else -> "Hi, Good Evening"
        }
    }

    private fun showMoreOptions() {
        bottomSheet = BottomSheetDialog(requireContext())
        val layout = NavigationLayoutBinding.inflate(layoutInflater)
        bottomSheet.setContentView(layout.root)
        bottomSheet.setCanceledOnTouchOutside(true)

        layout.apply {

            navProfile.setOnClickListener {
                bottomSheet.dismiss()
                findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
            }

            navHistory.setOnClickListener {
                bottomSheet.dismiss()
                findNavController().navigate(R.id.action_homeFragment_to_orderHistoryFragment)
            }

            navCart.setOnClickListener {
                bottomSheet.dismiss()
                findNavController().navigate(R.id.action_homeFragment_to_cartFragment)
            }

            navPendingOrders.setOnClickListener {
                bottomSheet.dismiss()
                findNavController().navigate(R.id.action_homeFragment_to_pendingOrdersFragment)
            }

            navShareApp.setOnClickListener {
                bottomSheet.dismiss()
            }

            navAboutUs.setOnClickListener {
                bottomSheet.dismiss()
                findNavController().navigate(R.id.action_homeFragment_to_aboutUsFragment)
            }

            navChatOnWhatsapp.setOnClickListener {
                bottomSheet.dismiss()
                Utils.openWhatsAppChat(requireContext(), "+919876543210")
            }

            navContactUs.setOnClickListener {
                bottomSheet.dismiss()
                Utils.openDialer(requireContext(), "+919876543210")
            }

            navFeedBack.setOnClickListener {
                bottomSheet.dismiss()
                Utils.openEmailClient(requireContext(), "contact@gmail.com")
            }

            navLogout.setOnClickListener {
                if(Firebase.auth.currentUser != null) {
                    bottomSheet.dismiss()
                    authViewModel.signOut()
                    startActivity(Intent(requireActivity(), WelcomeActivity::class.java))
                    requireActivity().finish()
                } else {
                    bottomSheet.dismiss()
                    Utils.showMessage(requireContext(), "Please Login")
                }
            }

            btClose.setOnClickListener {
                bottomSheet.dismiss()
            }

        }


        bottomSheet.show()

    }


    private fun showExitDialog() {
        val bottomSheet = BottomSheetDialog(requireContext())
        val layout = DialogExitBinding.inflate(layoutInflater)
        bottomSheet.setContentView(layout.root)
        bottomSheet.setCanceledOnTouchOutside(true)
        layout.btExit.setOnClickListener {
            bottomSheet.dismiss()
            requireActivity().finish()
        }
        layout.btCancel.setOnClickListener {
            bottomSheet.dismiss()
        }
        bottomSheet.show()
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
        for (model in list) {
            if (model.title.contains(newText.orEmpty(), ignoreCase = true)
                || model.category.contains(newText.orEmpty(), ignoreCase = true)
                || model.offerPrice.contains(newText.orEmpty(), ignoreCase = true)
                )
                filteredList.add(model)
        }
        productAdapter.submitList(filteredList)
        binding.productRv.adapter = productAdapter

    }

    override fun onItemClick(category: CategoryModel) {
        val action = HomeFragmentDirections.actionHomeFragmentToServiceFragment(category)
        findNavController().navigate(action)
    }

    override fun onItemClick(model: ProductModel) {
        val action = HomeFragmentDirections.actionHomeFragmentToServiceDetailsFragment(model)
        findNavController().navigate(action)
    }

}