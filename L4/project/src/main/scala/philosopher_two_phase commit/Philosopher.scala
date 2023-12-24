import org.apache.zookeeper.{CreateMode, WatchedEvent, Watcher, ZooDefs, ZooKeeper}

import java.util.concurrent.Semaphore
import scala.util.Random

case class Philosopher(id: Integer, serverAddress: String, rootPath: String, leftFork: Semaphore, rightFork: Semaphore) extends Watcher {

  val zk = new ZooKeeper(serverAddress, 3000, this)
  val mutex = new Object()
  val philosopherPath = rootPath + "/philosopher_" + id.toString

  // Функция для обработки событий
  override def process(event: WatchedEvent): Unit = {
    mutex.synchronized {
      mutex.notify()
    }
  }

  // Функция для поедания
  def eat(): Unit = {
    zk.create(philosopherPath, Array.emptyByteArray, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL)
    mutex.synchronized {
      println("Философ #" + id + " голоден")
      rightFork.acquire()
      println("Философ #" + id + " взял правую вилку")
      leftFork.acquire()
      println("Философ #" + id + " взял левую вилку")
      println("Философ #" + id + " начал есть")
      Thread.sleep(1000)
      println("Философ #" + id + " закончил есть")
      rightFork.release()
      println("Философ #" + id + " освободил правую вилку")
      leftFork.release()
      println("Философ #" + id + " освободил левую вилку")
    }
  }

  // Функция для размышления
  def think(): Unit = {
    println("Философ #" + id + " начал размышлять")
    zk.delete(philosopherPath, -1)
    Thread.sleep(1000)
    println("Философ #" + id + " закончил размышлять")
  }
}