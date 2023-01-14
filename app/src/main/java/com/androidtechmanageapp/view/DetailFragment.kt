package com.androidtechmanageapp.view

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.androidtechmanageapp.R
import com.androidtechmanageapp.TechApplication
import com.androidtechmanageapp.viewmodel.TechViewModel
import com.androidtechmanageapp.viewmodel.TechViewModelFactory
import com.google.android.material.snackbar.Snackbar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val args: DetailFragmentArgs by navArgs()

    private val techViewModel: TechViewModel by viewModels {
        TechViewModelFactory((requireActivity().application as TechApplication).repository)
    }

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
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //トップ画面から送られたTechAndURデータを取得（TechAdapter経由）
        val techAndURL = args.current
        val tech = techAndURL.tech
        val urlList = techAndURL.urls

        //入力欄にデータ表示
        val etTech: EditText = view.findViewById(R.id.et_tech)
        etTech.setText(tech.title)
        val etDetail: EditText = view.findViewById(R.id.et_detail)
        etDetail.setText(tech.detail)
        val etUrl1: EditText = view.findViewById(R.id.et_url1)
        Log.i("ATMP","DetailFragment.onViewCreated.url1:${urlList[0].url}")
        etUrl1.setText(urlList[0].url)
        val etUrl2: EditText = view.findViewById(R.id.et_url2)
        etUrl2.setText(urlList[1].url)
        val etUrl3: EditText = view.findViewById(R.id.et_url3)
        etUrl3.setText(urlList[2].url)

        //プルダウンにデータ表示
        val spinner: Spinner = view.findViewById(R.id.spinner)
        ArrayAdapter.createFromResource(
            requireActivity(), R.array.category_array, android.R.layout.simple_spinner_item
        ).also { _adapter ->
            _adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.apply {
                adapter = _adapter
                setSelection(getPositionFromStringInSpinner(tech.category))
            }
        }

        //メニュー表示と設定
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_detail, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_delete -> {
                        //削除を示すダイアログ表示
                        AlertDialog.Builder(requireActivity())
                            .setTitle(R.string.dialog_delete_title)
                            .setPositiveButton(R.string.dialog_delete_yes) { _, _ ->
                                //データ削除
                                techViewModel.deleteTech(tech)
                                Snackbar.make(view, getString(R.string.snackbar_delete), Snackbar.LENGTH_SHORT).show()
                                findNavController().popBackStack()
                            }
                            .setNeutralButton(R.string.dialog_delete_no) { _, _ ->
                            }
                            .show()
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        view.findViewById<Button>(R.id.btn_update).setOnClickListener{
            //入力値を取得
            val title = etTech.text.toString()
            val category = spinner.selectedItem as String
            val detail :String? = etDetail.text.toString()
            val url1 :String? = etUrl1.text.toString()
            val url2 :String? = etUrl2.text.toString()
            val url3 :String? = etUrl3.text.toString()

            //技術名が入力されていない場合、スナックバー表示
            if (TextUtils.isEmpty(title)) {
                Snackbar.make(view, getString(R.string.snackbar_input_warning), Snackbar.LENGTH_SHORT).show()
            } else {
                //データ更新
                tech.title = title
                tech.category = category
                tech.detail = detail ?: ""
                techViewModel.updateTechAndURL(tech,urlList,url1,url2,url3)
                Snackbar.make(view, getString(R.string.snackbar_update), Snackbar.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }

    //Spinnerで選択した文字列から位置を取得
    private fun getPositionFromStringInSpinner(st:String):Int {
        return when (st) {
            "Basic" -> 0
            "Architecture" -> 1
            "Library" -> 2
            "Test" -> 3
            "Else" -> 4
            else -> 0
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}