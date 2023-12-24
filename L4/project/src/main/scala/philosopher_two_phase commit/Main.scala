import java.util.concurrent.Semaphore

object Main {
  def main(args: Array[String]): Unit = {
    philosophers()
    //commit()
  }

  def philosophers(): Unit = {
    println("------Philosophers lunch------")
    val zookeeperHostPort = "localhost:2181"
    val zookeeperRoot = "/ph"
    val numSeats = 5
    val forks = new Array[Semaphore](numSeats)
    for (i <- 0 until numSeats) {
      forks(i) = new Semaphore(1)
    }
    val philosophers = new Array[Thread](numSeats)
    for (i <- 0 until numSeats) {
      philosophers(i) = new Thread(
        () => {
          val philosopher = Philosopher(i, zookeeperHostPort, zookeeperRoot, forks(i), forks((i + 1) % numSeats))
          while (true) {
            philosopher.eat()
            philosopher.think()
          }
        }
      )
      philosophers(i).start()
    }
  }

  def commit(): Unit = {
    println("------Commit------")
    val zookeeperHostPort = "localhost:2181"
    val zookeeperRoot = "/commit"
    val numWorkers = 5
    val workers = new Array[Thread](numWorkers)

    val coordinator = Coordinator(zookeeperHostPort, zookeeperRoot, numWorkers)

    val coordinatorThread = new Thread(
      () => {
        coordinator.run()
      }
    )
    coordinatorThread.start()

    for (i <- 0 until numWorkers) {
      workers(i) = new Thread(
        () => {
          val worker = Worker(i, zookeeperHostPort, coordinator.coordinatorPath)
          worker.run()
        }
      )
      workers(i).start()
    }
  }

}