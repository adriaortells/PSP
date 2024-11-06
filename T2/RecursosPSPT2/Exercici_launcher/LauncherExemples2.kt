import java.io.*

/*

Aquest exemple resol el problema de l'escriptura per la terminal, 
fent ús d'un objecte compartit entre els fils (anomenat lock). 

Quan un fil va a escriure per pantalla, ho fa dins un bloc syncrhonized(lock), 
de manera que bloqueja el monitor d'aquest objecte compartit, i quan
altre fill intenta escriure per la pantalla, queda bloquejat en el monitor.

*/

class Launcher(val ordre: String, val color: String, val lock: Any) : Runnable {
    override fun run() {
        try {
            // Creem el ProcessBuilder i llancem l'ordre
            val pb = ProcessBuilder()
            pb.command(ordre.split(" "))
            val p = pb.start()

            // Creem el bufferedReader per capturar l'eixida del procés
            val br = BufferedReader(InputStreamReader(p.inputStream))

            synchronized(lock) {
                // Llegim de l'stream i escrivim per la pantalla
                var line: String?
                while (br.readLine().also { line = it } != null) {
                    println(this.color + line + "\u001B[0m")
                    Thread.sleep(1)
                }
            }
        } catch (err: IOException) {
            err.printStackTrace()
        }
    }
}

fun main(args: Array<String>) {
    try {
        // Objecte de bloqueig comú
        val lock = Any()

        // Creem els dos Launchers per al ps i el ls amb colors diferents
        val l = Launcher("ps aux", "\u001B[31m", lock)
        val l2 = Launcher("ls -l /", "\u001B[32m", lock)

        // Y els corresponents fils
        val fil1 = Thread(l)
        val fil2 = Thread(l2)

        // Llancem els fils
        fil1.start()
        fil2.start()

        // I els junte amb el fil principal quan acaben
        fil1.join()
        fil2.join()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
