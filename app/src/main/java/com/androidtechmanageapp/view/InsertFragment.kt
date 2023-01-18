package com.androidtechmanageapp.view

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.androidtechmanageapp.R
import com.androidtechmanageapp.model.Tech
import com.androidtechmanageapp.viewmodel.TechViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InsertFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class InsertFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_insert, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etTech: EditText = view.findViewById(R.id.et_tech)
        val etDetail: EditText = view.findViewById(R.id.et_detail)
        val etUrl1: EditText = view.findViewById(R.id.et_url1)
        val etUrl2: EditText = view.findViewById(R.id.et_url2)
        val etUrl3: EditText = view.findViewById(R.id.et_url3)

        //プルダウン表示
        val spinner: Spinner = view.findViewById(R.id.spinner)
        ArrayAdapter.createFromResource(
            requireActivity(), R.array.category_array, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        view.findViewById<Button>(R.id.btn_insert).setOnClickListener{
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
                val tech = Tech(title = title, detail = detail ?: "", category = category)
                //データ追加
                techViewModel.insertTechAndURL(tech,url1,url2,url3)
                Snackbar.make(view, getString(R.string.snackbar_insert), Snackbar.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InsertFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InsertFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}