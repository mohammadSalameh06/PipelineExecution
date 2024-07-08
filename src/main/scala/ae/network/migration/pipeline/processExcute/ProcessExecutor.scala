package ae.network.migration.pipeline.processExecute

import scala.concurrent.{Future, ExecutionContext}
import scala.sys.process._

object ProcessExecutor {
  def execute(command: String, args: List[String])(implicit ec: ExecutionContext): Future[Process] = Future {
    Process(command :: args).run()
  }
}
