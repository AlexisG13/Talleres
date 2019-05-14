package com.naldana.ejemplo10

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.coin_layout.view.*

class CoinAdapter(val items: List<Coin> , val clickListener : (Coin) -> Unit) : RecyclerView.Adapter<CoinAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Coin , clickListener: (Coin) -> Unit) = with(itemView){
            Glide.with(this).load(item.img).placeholder(R.drawable.ic_launcher_background).into(imgview_coin)
            tv_nombreMoneda.text = item.name
            tv_nombrePais.text = item.country
            tv_valorMonetario.text = item.value
            this.setOnClickListener{
                clickListener(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.coin_layout,parent,false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position] , clickListener)
    }
}