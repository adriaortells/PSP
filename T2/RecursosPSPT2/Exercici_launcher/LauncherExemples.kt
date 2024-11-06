import java.io.*

class Launcher(val ordre: String, val color: String) : Runnable {
    override fun run() {
        try {

            // Creem el ProcessBuilder i llancem l'ordre
            val pb = ProcessBuilder()
            pb.command(ordre.split(" "))
            val p = pb.start()

            // Creem el bufferedReader per capturar l'eixida del procés
            val br = BufferedReader(InputStreamReader(p.inputStream))

            // Llegim de l'stream i escrivim per la pantalla
            var line: String
            while ((br.readLine().also { line = it }) != null) {
                println(this.color + line + "\u001B[0m")
                Thread.sleep(1)
            }
        } catch (err: IOException) {
            err.printStackTrace()
        }
    }
}

fun main(args: Array<String>) {
    try {
        // Creem els dos Launchers per al ps i el ls amb colors diferents
        val l = Launcher("ps aux", "\u001B[31m")
        val l2 = Launcher("ls -l /", "\u001B[32m")

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
