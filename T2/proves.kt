import java.io.*


class Terminal(){
    @Synchronized
    fun writteOnTerminal( br: BufferedReader,  color:String){   
            var line: String?
            while((br.readLine().also { line = it }) != null){
                println(color + line + "\u001b[0m")
                Thread.sleep(1)
        }
    }
}

class Launcher(val ordre:String, val color:String, val terminal: Terminal):Runnable{
    override fun run(){
        try {
            val pb = ProcessBuilder()
            pb.command(ordre.split(" "))
            val p = pb.start()

            val br = BufferedReader(InputStreamReader(p.inputStream))

            terminal.writteOnTerminal(br, color)
        }
        catch(e: Exception) {
            e.printStackTrace()
        }
    }
}

fun main(args:Array<String>){
    try{

        val lock = Terminal()

        val l1 = Launcher("ps aux", "\u001b[32m", lock)
        val l2 = Launcher("ls -l", "\u001b[31m", lock)

        val f1 = Thread(l1)
        val f2 = Thread(l2)
        f1.start()
        f2.start()
        f1.join()
        f2.join()
    }catch(e: Exception){e.printStackTrace()}
}