

class SafataForn(){
    var valor: Int = 0
    var disponible: Boolean = false
    val lock = Object()

    fun get(): Int{
        synchronized(lock){
            while(!disponible){
                try {
                    lock.wait()
                }
                catch(e:Exception) {
                }
            }
            disponible = false
            lock.notifyAll()
            return this.valor
        }
    }

    fun set(valor :Int){
        synchronized(lock) {
            while(disponible){
                try {
                    lock.wait()
                }
                catch(e:Exception) {
                }
            }
            this.valor = valor
            disponible = true
            lock.notifyAll()
        }
    }
}

class Forner(val compartit: SafataForn):Runnable{
    override fun run(){
        for (i in 1..4) {
            println("El forner ha fet ${i} pans")
            compartit.set(i)
            try {
                Thread.sleep(1000)
            }
            catch(e:Exception) {
            }
        }
    }
}

class Client(val compartit: SafataForn, val nom:String):Runnable{
    override fun run(){
        println("El client de nom ${nom} ha consumit el pa número ${compartit.get()}")
        
    }
}

fun main(){
    val compartit = SafataForn()
    val forner = Thread(Forner(compartit))
    val clients = arrayOf(Thread(Client(compartit, "aDRIA")),
        Thread(Client(compartit, "aLDREFO")),
        Thread(Client(compartit, "Aitana")))
    forner.start()
    for (client in clients) {
        client.start()
    }
}