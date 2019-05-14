package com.naldana.ejemplo10

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.coin_activity.*

class Coin_Activity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.coin_activity)
        val coinBundle: Coin = intent?.extras?.getParcelable("coin_key")?:Coin()
        showContent(coinBundle)
    }

    fun showContent(coin: Coin){
        Glide.with(this)
            .load(coin.imgBanderaPais)
            .placeholder(R.drawable.ic_launcher_background)
            .into(imgview_country)
        tv_name.text = coin.name.toString()
        tv_country.text= coin.country.toString()
        tv_year.text=coin.year.toString()
        tv_value.text=coin.value.toString()
        tv_isAvailable.text=coin.isAvaliable.toString()
        tv_value_us.text=coin.value_us.toString()
    }


}