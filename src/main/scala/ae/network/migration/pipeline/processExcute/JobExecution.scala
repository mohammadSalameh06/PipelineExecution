package ae.network.migration.pipeline.processExecute

import ae.network.migration.pipeline.models.Job
import scala.concurrent.{Future, ExecutionContext}


class JobExecution {

  def executeJob(job: Job)(implicit ec: ExecutionContext): Future[Int] = {
    val args = job.args.toList // converting to list becasue the Scala process accept List not array
    println(s"Starting job '${job.name}' with arguments: ${args.mkString(" ")}")

    val result = ProcessExecutor.execute("java", job.jar :: args) // Concatenation ==>(command + jar + args ) the command and args
    result.flatMap { process =>
      Future {
        val exitValue = process.exitValue()
        println(s"Job '${job.name}' finished with exit value: $exitValue")
        exitValue
        
      }
    }
  }
}
