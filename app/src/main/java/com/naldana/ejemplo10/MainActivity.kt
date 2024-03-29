package com.naldana.ejemplo10

import android.content.ContentValues
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.BaseColumns
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.naldana.ejemplo10.Database.Database
import com.naldana.ejemplo10.Database.DatabaseContract
import com.naldana.ejemplo10.utilities.NetworkUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.IOException
import java.net.URL

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var dbHelper = Database(this)



    var twoPane =  false
    private lateinit var viewAdapter : CoinAdapter
    private lateinit var viewManager : LinearLayoutManager
    private var listaMonedas : ArrayList<Coin> = ArrayList<Coin>()

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // TODO (9) Se asigna a la actividad la barra personalizada
        setSupportActionBar(toolbar)


        // TODO (10) Click Listener para el boton flotante
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }


        // TODO (11) Permite administrar el DrawerLayout y el ActionBar
        // TODO (11.1) Implementa las caracteristicas recomendas
        // TODO (11.2) Un DrawerLayout (drawer_layout)
        // TODO (11.3) Un lugar donde dibujar el indicador de apertura (la toolbar)
        // TODO (11.4) Una String que describe el estado de apertura
        // TODO (11.5) Una String que describe el estado cierre
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        // TODO (12) Con el Listener Creado se asigna al  DrawerLayout
        drawer_layout.addDrawerListener(toggle)


        // TODO(13) Se sincroniza el estado del menu con el LISTENER
        toggle.syncState()

        // TODO (14) Se configura el listener del menu que aparece en la barra lateral
        // TODO (14.1) Es necesario implementar la inteface {{@NavigationView.OnNavigationItemSelectedListener}}
        nav_view.setNavigationItemSelectedListener(this)

        // TODO (20) Para saber si estamos en modo dos paneles
        if (fragment_content != null ){
            twoPane =  true
        }

        FetchCoins().execute()

        /*
         * TODO (Instrucciones)Luego de leer todos los comentarios añada la implementación de RecyclerViewAdapter
         * Y la obtencion de datos para el API de Monedas
         */

        //Escribiendo en la base de datos

        /*
        val db = dbHelper.writableDatabase

        val values=ContentValues().apply {
            put(DatabaseContract.Monedas.COLUMN_NAME,name)
            put(DatabaseContract.Monedas.COLUMN_COUNTRY,country)
            put(DatabaseContract.Monedas.COLUMN_ISAVALIABLE,isavaliable)
            put(DatabaseContract.Monedas.COLUMN_VALUE,value)
            put(DatabaseContract.Monedas.COLUMN_YEAR,year)
            put(DatabaseContract.Monedas.COLUMN_VALUE_US,valueUS)
            put(DatabaseContract.Monedas.COLUMN_IMGBANDERAPAIS,banda)
            put(DatabaseContract.Monedas.COLUMN_IMG,img)


        }

        val newRowId = db?.insert(DatabaseContract.Monedas.TABLE_NAME, null, values)




        fun readCoins():List<Coin>{
            val db = dbHelper.readableDatabase

            val projection = arrayOf(
                BaseColumns._ID,
                DatabaseContract.Monedas.COLUMN_NAME,
                DatabaseContract.Monedas.COLUMN_COUNTRY,
                DatabaseContract.Monedas.COLUMN_ISAVALIABLE,
                DatabaseContract.Monedas.COLUMN_VALUE,
                DatabaseContract.Monedas.COLUMN_YEAR,
                DatabaseContract.Monedas.COLUMN_VALUE_US,
                DatabaseContract.Monedas.COLUMN_IMGBANDERAPAIS,
                DatabaseContract.Monedas.COLUMN_IMG

            )

            val sortOrder = "${DatabaseContract.Monedas.COLUMN_NAME} DESC"

            val cursor = db.query(
                DatabaseContract.Monedas.TABLE_NAME, // nombre de la tabla
                projection, // columnas que se devolverán
                null, // Columns where clausule
                null, // values Where clausule
                null, // Do not group rows
                null, // do not filter by row
                sortOrder // sort order
            )

            var lista = mutableListOf<Coin>()

            with(cursor) {
                while (moveToNext()) {
                    var moneditas = Coin(
                        getInt(getColumnIndexOrThrow(BaseColumns._ID)).toString(),
                        getString(getColumnIndexOrThrow(DatabaseContract.Monedas.COLUMN_NAME)),
                        getString(getColumnIndexOrThrow(DatabaseContract.Monedas.COLUMN_COUNTRY)),
                        getString(getColumnIndexOrThrow(DatabaseContract.Monedas.COLUMN_IMG)),
                        getString(getColumnIndexOrThrow(DatabaseContract.Monedas.COLUMN_ISAVALIABLE)).toBoolean(),
                        getString(getColumnIndexOrThrow(DatabaseContract.Monedas.COLUMN_REVIEW)),
                        getString(getColumnIndexOrThrow(DatabaseContract.Monedas.COLUMN_YEAR))

                    )

                    lista.add(moneditas)
                }
            }

            return lista

        }

         */


    }

    fun initRecycler(coins : ArrayList<Coin>) {
        viewManager = LinearLayoutManager(this)
        viewAdapter = CoinAdapter(coins, { item: Coin -> coinEvent(item) })
        recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    fun coinEvent(element:Coin){
        val infoCoin = Bundle()
        infoCoin.putParcelable("coin_key",element)
        startActivity(Intent(this,Coin_Activity::class.java).putExtras(infoCoin))
    }

    private inner class FetchCoins() : AsyncTask<String, Void, String>(){
        override fun doInBackground(vararg params: String?): String {
            val url : URL = NetworkUtils.buildUrl();
            try{
                var response : String = NetworkUtils.getResponseFromHttpUrl(url)
                var gson : Gson = Gson()
                var coins : AllCoins = gson.fromJson(response,AllCoins::class.java)
                for(i in 0 .. (coins.datos.size-1)){
                    var moneda : Coin = Coin(coins.datos.get(i).value.toString(),coins.datos.get(i).value_us.toString(),coins.datos.get(i).year.toString(),
                        coins.datos.get(i).review,coins.datos.get(i).isAvaliable,coins.datos.get(i).img,coins.datos.get(i)._id,
                        coins.datos.get(i).name,coins.datos.get(i).country,coins.datos.get(i).__v,coins.datos.get(i).imgBanderaPais)
                    listaMonedas.add(moneda)
                }
                return response
            }
            catch(e: IOException){
                e.printStackTrace()
                return ""
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            initRecycler(listaMonedas)
        }


    }


    // TODO (16) Para poder tener un comportamiento Predecible
    // TODO (16.1) Cuando se presione el boton back y el menu este abierto cerralo
    // TODO (16.2) De lo contrario hacer la accion predeterminada
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    // TODO (17) LLena el menu que esta en la barra. El de tres puntos a la derecha
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    // TODO (18) Atiende el click del menu de la barra
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun searchByCountry(country : String){
        var countries : ArrayList<Coin> = ArrayList<Coin>()
        for (i in 0 .. (listaMonedas.size-1)){
            if(listaMonedas.get(i).country.equals(country)){
                countries.add(listaMonedas.get(i))
            }
            initRecycler(countries)
        }
    }

    // TODO (14.2) Funcion que recibe el ID del elemento tocado
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            // TODO (14.3) Los Id solo los que estan escritos en el archivo de MENU
            R.id.nav_countries -> {
                    initRecycler(listaMonedas)
            }
            R.id.nav_sv -> {
                    searchByCountry("El Salvador")
            }
            R.id.nav_eu -> {
                    searchByCountry("Union Europea")
            }
            R.id.nav_fr -> {
                    searchByCountry("Francia")
            }
            R.id.nav_mx -> {
                    searchByCountry("Mexico")
            }
            R.id.nav_rus -> {
                    searchByCountry("Rusia")
            }
            R.id.nav_pn ->{
                    searchByCountry("Panama")
            }
        }

        // TODO (15) Cuando se da click a un opcion del menu se cierra de manera automatica
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
