import java.io.File

fun main(args: Array<String>){

    val fitxer_entrada = File(args[0])
    val inici = args[1].toInt()
    val final = args[2].toInt()

    val linees = fitxer_entrada.readLines()
    var suma = 0.0
    var count = 0

    for( i in inici until final){
        suma += linees[i].toDouble()
        count++ 
    }
    println("$suma, $count")
}