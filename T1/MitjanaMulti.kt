import java.io.File
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

fun main (args: Array<String>){
    val fitxer_entrada = args[0]
    println("Llegint el fitxer de entrada")
    try {
        val numLines = File(fitxer_entrada).readLines().size
        println("Calculant la mitja de les temperatures")
        val mitjana = calculaMitjanaAmbProcessos(fitxer_entrada, numLines)
        println("La temperatura mitjana anual es $mitjana")
    }
    catch(e: Exception) {println("S'ha produit una excepcio")}
}

fun calculaMitjanaAmbProcessos(fitxer_entrada: String, num_lines: Int): Double{
    val num_blocks = Runtime.getRuntime().availableProcessors()
    val block_size = num_lines / num_blocks
    val bufferedReaders = mutableListOf<BufferedReader>()
    var processos = mutableListOf<Process>()
    for(i in 1 until num_blocks){
        val start = i * block_size
        val end = if(i == num_blocks -1) num_lines else (i +1) * block_size

        val p = ProcessBuilder("kotlin", "-cp", ".", "MitjanaWorkerKt",
            fitxer_entrada, start.toString(), end.toString()).start()

        val br = BufferedReader(InputStreamReader(p.inputStream))

        bufferedReaders.add(br)
        processos.add(p)
    }

        processos.forEach{it.waitFor()}

        var sumaTotal = 0.0
        var countTotal = 0
    
        for(reader in bufferedReaders){
            val temp_count = reader.readLine().split(",")
            val temp = temp_count[0].toFloat()
            val count = temp_count[1].toInt()

            sumaTotal += temp
            countTotal += count
        }
        return sumaTotal / countTotal
    
}