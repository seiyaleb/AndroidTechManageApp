package com.androidtechmanageapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.androidtechmanageapp.R
import com.androidtechmanageapp.model.TechAndURL

class TechAdapter(private val navController: NavController) :ListAdapter<TechAndURL, TechAdapter.TechViewHolder>(TechComparator()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TechViewHolder {
        return TechViewHolder.create(parent)
    }
    override fun onBindViewHolder(holder: TechViewHolder, position: Int) {
        val current = getItem(position)
        val listener = View.OnClickListener {
            //タップしたTechAndURデータと共に詳細画面に遷移
            val action = TopFragmentDirections.actionTopFragmentToDetailFragment(current)
            navController.navigate(action)
        }
        holder.bind(listener,current.tech.title)
    }

    class TechViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var view: View = itemView
        //データ割り当て
        fun bind(listener: View.OnClickListener,text: String) {
            view.findViewById<TextView>(R.id.tv_title).text = text
            view.setOnClickListener(listener)
        }
        //シングルトンでViewHolder生成
        companion object {
            fun create(parent: ViewGroup): TechViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_tech, parent, false)
                return TechViewHolder(view)
            }
        }
    }
    class TechComparator : DiffUtil.ItemCallback<TechAndURL>() {
        override fun areItemsTheSame(oldItem: TechAndURL, newItem: TechAndURL): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: TechAndURL, newItem: TechAndURL): Boolean {
            return oldItem.tech.title == newItem.tech.title
        }
    }
}