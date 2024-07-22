package ae.network.migration.pipeline.processExecute

import ae.network.migration.pipeline.models.Job
import scala.concurrent.{Future, ExecutionContext}
import scala.sys.process._

class JobExecution {

  def executeJob(job: Job)(implicit ec: ExecutionContext): Future[Int] = {
    val args = job.args.toList
    println(s"Starting job '${job.name}' with arguments: ${args.mkString(" ")}")

    /**
     * Correctly use the "-jar" option to execute the JAR file
     */
    val result = ProcessExecutor.execute("java", "-jar" :: job.jar :: args)
    result.flatMap { process =>
      Future {
        val exitValue = process.exitValue()
        println(s"Job '${job.name}' finished with exit value: $exitValue")
        exitValue
      }
    }
  }
}
