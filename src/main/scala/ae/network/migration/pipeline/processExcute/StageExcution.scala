package ae.network.migration.pipeline.processExecute

import ae.network.migration.pipeline.models.Stage
import scala.concurrent.{Future, ExecutionContext}

class StageProcessor(jobExecution: JobExecution)(implicit ec: ExecutionContext) {
  def processStage(stage: Stage): Future[List[Int]] = {
    if (stage.isParallel) {

        // a sequence of future will be created to make sure that all the jobs are excuted concurrently
      val futures = stage.jobs.map(job => jobExecution.executeJob(job))

      // here the map will apply a excution for a specific job in side the stage

      Future.sequence(futures)
      // coverting the a list of futures into a single future containing a list of results
    } else {

      // foldLeft is used to process the elements from left to right , so we used it here to excute process sequatialy ,
      stage.jobs.foldLeft(Future.successful(List.empty[Int])) { (acc, job) =>
        acc.flatMap(results => jobExecution.executeJob(job).map(result => results :+ result))
      }
    }
  } 
}
