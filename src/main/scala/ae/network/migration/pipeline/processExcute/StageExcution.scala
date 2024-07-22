package ae.network.migration.pipeline.processExecute

import ae.network.migration.pipeline.models.Stage
import scala.concurrent.{Future, ExecutionContext}
/**
 * Class responsible for processing stages in a job execution pipeline.
 *
 * @param jobExecution An instance of JobExecution to execute individual jobs.
 * @param ec An implicit ExecutionContext for managing asynchronous operations.
 */
class StageProcessor(jobExecution: JobExecution)(implicit ec: ExecutionContext) {
  /**
   * Processes a given stage by executing its jobs either in parallel or sequentially
   * based on the stage's configuration.
   *
   * @param stage The stage to be processed.
   * @return A Future containing a list of results from the executed jobs.
   */
  def processStage(stage: Stage): Future[List[Int]] = {
    if (stage.isParallel) {
      /**
       * Creates a sequence of futures to ensure all jobs are executed concurrently.
       */
      val futures = stage.jobs.map(job => jobExecution.executeJob(job))

      /**
       * Converts a list of futures into a single future containing a list of results.
       */

      Future.sequence(futures)
    } else {
      /**
       * Processes the jobs sequentially using foldLeft, executing each job one by one.
       *
       * @return A Future containing a list of results from the executed jobs.
       */
      stage.jobs.foldLeft(Future.successful(List.empty[Int])) { (acc, job) =>
        acc.flatMap(results => jobExecution.executeJob(job).map(result => results :+ result))
      }
    }
  }
}
