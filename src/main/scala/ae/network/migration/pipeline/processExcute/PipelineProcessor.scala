  package ae.network.migration.pipeline.processExecute

  import ae.network.migration.pipeline.models.Pipeline
  import scala.concurrent.{Future, ExecutionContext}

  class PipelineProcessor(stageProcessor: StageProcessor)(implicit ec: ExecutionContext) {
    def processPipeline(pipeline: Pipeline): Future[List[List[Int]]] = {
      val stageResults = pipeline.stages.map(stage => stageProcessor.processStage(stage))
      Future.sequence(stageResults)
    }
  }
