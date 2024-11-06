

import java.io.IOException
import java.io.File
import kotlin.random.Random

fun main(args: Array<String>) {
   val origen = args[0]
   println("Llegint el fitxer d'entrada")
   try{
      val linees = File(origen).readLines()
      var suma = 0.0
      for( i in 0 until linees.size){
         suma += linees[i].toDouble()
      }
      val mitjana = suma / linees.size
      println("La temperatura mitjana es ${mitjana}")
   }catch(e: Exception){println("Problemas")}
}