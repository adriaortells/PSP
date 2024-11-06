import java.io.*

/*

Aquest exemple resol el problema de l'escriptura per la terminal,
fent ús d'un objecte compartir Terminal, que ofereix el mètode
sincronitzat WriteOnTerminal, fent ús de l'anotació @Synchronized

*/

class Terminal {

    @Synchronized
    fun writeOnTerminal(br: BufferedReader, color: String) {
        var line: String?
        while (br.readLine().also { line = it } != null) {
            println(color + line + "\u001B[0m")
            Thread.sleep(1)
        }
    }
}

class Launcher(val ordre: String, val color: String, val terminal: Terminal) : Runnable {
    override fun run() {
        try {
            // Creem el ProcessBuilder i llancem l'ordre
            val pb = ProcessBuilder()
            pb.command(ordre.split(" "))
            val p = pb.start()

            // Creem el bufferedReader per capturar l'eixida del procés
            val br = BufferedReader(InputStreamReader(p.inputStream))

            // I invoquem al mètode de la  terminal per escriure per pantalla
            terminal.writeOnTerminal(br, color)
        } catch (err: IOException) {
            err.printStackTrace()
        }
    }
}

fun main(args: Array<String>) {
    try {
        // Objecte de bloqueig comú
        val lock = Terminal()

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
