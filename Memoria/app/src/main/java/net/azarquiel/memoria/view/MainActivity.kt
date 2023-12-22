package net.azarquiel.memoria.view

import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.size
import kotlinx.android.synthetic.main.content_main.*
import net.azarquiel.memoria.R
import net.azarquiel.memoria.model.Ficha
import org.jetbrains.anko.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var ficha: Ficha?=null
    private var ficha2: Ficha?=null
    private var ivmatriz = Array(6) { arrayOfNulls<ImageView>(5)}
    private var vector=ArrayList<Int>()
    private var vector2=ArrayList<Int>()
    private var click=0
    private var puntos=0
    private var fallos=0
    private var parejas=0
    private var bloquear=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        makeTablero()
        bloquear = true
        tapartodo()
    }

    private fun makeTablero() {
        for(i in 0 until 60){
            vector2.add(i)
        }
        vector2.shuffle()

        for (i in 0 until 2)
            for (j in 0 until 15)
                vector.add(vector2[j])
        vector.shuffle()
        var cont = 0
        var id: Int

        for (i in 0 until lv.size){
            val lh = lv.getChildAt(i) as LinearLayout
            for (j in 0 until lh.size) {
                ivmatriz[i][j] = lh.getChildAt(j) as ImageView
                ivmatriz[i][j]!!.tag = Ficha(i,j,vector[cont])
                id = resources.getIdentifier("pokemon${vector[cont]}","drawable",packageName)
                ivmatriz[i][j]!!.setBackgroundResource(id)
                //opcion sin tapar 5 s ivmatriz[i][j]!!.setImageResource(R.drawable.tapa) android:src="@drawable/tapa" para ponerlos en diseño
                ivmatriz[i][j]!!.setOnClickListener(this)
                cont++
            }
        }
    }
    override fun onClick(v: View?) {
        if(bloquear){//solo te bloquara si es true que sera solo un segundo, si le pongo ! cambio bloquear a true y lo hara siempre
            return//y nunca te dejara pulsar porque siempre se saldra
        }
        val imagenPulsada = v as ImageView
        if(click==0){
            //if(ficha!=null){  Esto hacia que con dos click vieses las cartas y al darle un 3 se tapaban esas dos y empezaba una nueva pareja
            //    puntuacion()
            //}
            ficha = imagenPulsada.tag as Ficha
            control(0)
            click++
        }else if(click==1){
            ficha2 = imagenPulsada.tag as Ficha
            control(1)
            puntuacion()
            click=0
        }
    }

    private fun puntuacion(){
        if(ficha!!.pokemon!=ficha2!!.pokemon){
            tapa()
            control(2)
            puntos-=30
            fallos+=1
            toast("Mala suerte: ${puntos} puntos y ${fallos} fallos")
            //Toast.makeText(this, "Mala suerte:${puntos}puntos y ${fallos} fallos", Toast.LENGTH_LONG).show()
        }else{
            puntos+=100
            parejas+=1
            if(parejas==15){
                fin()
            }else{
                toast("Muy bien: ${puntos} puntos")
            }
        }
    }
    private fun tapa(){
        bloquear=true
        doAsync {
            SystemClock.sleep(1000)
            uiThread {
                ivmatriz[ficha!!.i][ficha!!.j]!!.setImageResource(R.drawable.tapa)
                ivmatriz[ficha2!!.i][ficha2!!.j]!!.setImageResource(R.drawable.tapa)
                bloquear=false
            }
        }
    }
    private fun control(x:Int){
        if(x==0){//ficha
            ivmatriz[ficha!!.i][ficha!!.j]!!.setImageResource(android.R.color.transparent)//o con visivility
            ivmatriz[ficha!!.i][ficha!!.j]!!.isEnabled=false
        }else if(x==1){//ficha2
            ivmatriz[ficha2!!.i][ficha2!!.j]!!.setImageResource(android.R.color.transparent)
            ivmatriz[ficha2!!.i][ficha2!!.j]!!.isEnabled=false
        }else{//nuevas fichas
            ivmatriz[ficha!!.i][ficha!!.j]!!.isEnabled=true
            ivmatriz[ficha2!!.i][ficha2!!.j]!!.isEnabled=true
        }
    }
    private fun fin(){
        alert("Has conseguido ${puntos} puntos con ${fallos} fallos ") {
            title=("Enhorabuena")
            yesButton {  }
        }.show()
    }
    private fun tapartodo(){
        toast("Tienes 5s para memorizar")
        doAsync {
            SystemClock.sleep(5000)
            uiThread {
                ivmatriz.forEach {
                    it.forEach {
                        it!!.setImageResource(R.drawable.tapa)
                    }
                }
                bloquear=false
            }
        }

    }
}



