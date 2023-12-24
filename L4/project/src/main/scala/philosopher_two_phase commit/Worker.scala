import org.apache.zookeeper.{CreateMode, WatchedEvent, Watcher, ZooDefs, ZooKeeper}

import java.util.concurrent.TimeUnit
import scala.util.Random

case class Worker(id: Integer, hostPort: String, root: String) extends Watcher {
  val zk = new ZooKeeper(hostPort, 3000, this)
  val workerPath = root + "/worker_" + id.toString

  override def process(event: WatchedEvent): Unit = {
    // Обработчик событий, не требуется для данной задачи
  }

  def run(): Unit = {
    val value = if (Random.nextDouble() > 0.5) "commit" else "abort"

    // Проверка существования корневого узла один раз
    while (zk.exists(root, false) == null) {
      TimeUnit.SECONDS.sleep(5)
    }

    println(s"Node $id vote $value")

    // Создание узла и удаление его при выходе
    zk.create(workerPath, value.getBytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL)
    TimeUnit.SECONDS.sleep(10)
    zk.close()
  }
}