package com.androidtechmanageapp.view

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidtechmanageapp.R
import com.androidtechmanageapp.model.TechAndURL
import com.androidtechmanageapp.viewmodel.TechViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TopFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class TopFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val techViewModel: TechViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //RecyclerView設定
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val navController = findNavController()
        val techAdapter = TechAdapter(navController)
        recyclerView.apply {
            adapter = techAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }

        //変更が通知される度にリスト表示を変更するオブザーバー作成
        val techAndURLObserver = Observer<List<TechAndURL>> { listTechAndURL ->
            listTechAndURL.let {
                techAdapter.submitList(it)
            }
        }

        //メニュー表示とタップ設定
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_top, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                //各メニューをタップした場合、LiveDataを監視
                when (menuItem.itemId) {
                    R.id.menu_all -> {
                        //全検索
                        techViewModel.loadAllTechAndURL().observe(viewLifecycleOwner,techAndURLObserver)
                    }
                    R.id.menu_category1 -> {
                        //基本のカテゴリー別検索
                        techViewModel.loadByCategoryTechAndURL("Basic").observe(viewLifecycleOwner, techAndURLObserver)
                    }
                    R.id.menu_category2 -> {
                        //アーキテクチャのカテゴリー別検索
                        techViewModel.loadByCategoryTechAndURL("Architecture").observe(viewLifecycleOwner, techAndURLObserver)
                    }
                    R.id.menu_category3 -> {
                        //ライブラリのカテゴリー別検索
                        techViewModel.loadByCategoryTechAndURL("Library").observe(viewLifecycleOwner, techAndURLObserver)
                    }
                    R.id.menu_category4 -> {
                        //テストのカテゴリー別検索
                        techViewModel.loadByCategoryTechAndURL("Test").observe(viewLifecycleOwner, techAndURLObserver)
                    }
                    R.id.menu_category5 -> {
                        //その他のカテゴリー別検索
                        techViewModel.loadByCategoryTechAndURL("Else").observe(viewLifecycleOwner, techAndURLObserver)
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        //LiveDataを監視（初回）
        techViewModel.allTechAndURL.observe(viewLifecycleOwner, techAndURLObserver)

        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener{
            findNavController().navigate(R.id.action_topFragment_to_insertFragment)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TopFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TopFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}