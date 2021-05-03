package com.ijsbss.rollover.mainFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ijsbss.rollover.R
import com.ijsbss.rollover.data.db.AppDatabase
import com.ijsbss.rollover.data.db.CategoryRepository
import com.ijsbss.rollover.databinding.FragmentMainBinding
import com.ijsbss.rollover.recyclerViews.MyRecyclerViewAdapter

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment(), View.OnClickListener  {

    private lateinit var mainFragmentViewModel: MainFragmentViewModel //by viewModels()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        val dao = AppDatabase.getInstance(requireActivity().application).categoryDao()
        val repository = CategoryRepository(dao)
        val factory = MainFragmentViewModelFactory(repository)
        mainFragmentViewModel = ViewModelProvider(this, factory).get(MainFragmentViewModel::class.java)
        binding.myViewModel = mainFragmentViewModel
        binding.lifecycleOwner = this
        initRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.add_category_button).setOnClickListener {
            findNavController().navigate(R.id.action_MainFragment_to_AddCategoryFragment)
        }

        view.findViewById<LinearLayout>(R.id.main_linear_layout).setOnClickListener(this)
    }

    private fun initRecyclerView(){
        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(this.context)
        displayCategoriesList()
    }

    private fun displayCategoriesList(){
        mainFragmentViewModel.categories.observe(binding.lifecycleOwner!!, {
            Log.i("MYTAG", it.toString())
            binding.categoryRecyclerView.adapter = MyRecyclerViewAdapter(it, binding.myViewModel!!)
        })
    }


    override fun onClick(v: View?) {
        Toast.makeText(v?.context,binding.categoryRecyclerView.adapter?.itemCount.toString() , Toast.LENGTH_SHORT).show()
        var i = 0
        while(i < binding.categoryRecyclerView.adapter!!.itemCount  ) {
            binding.categoryRecyclerView[i].findViewById<LinearLayout>(R.id.edit_and_delete_layout).visibility = GONE
            i++
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}