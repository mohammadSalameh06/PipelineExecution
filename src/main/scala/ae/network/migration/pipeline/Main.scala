package ae.network.migration.pipeline

import ae.network.migration.pipeline.models.{Pipeline, Stage , Job}

object Main {

  val job1 = Job("Job1", "path/to/jar1", "arg1 arg2", "path/to/log1")
  val job2 = Job("Job2", "path/to/jar2", "arg3 arg4", "path/to/log2")

  val pipeline: Pipeline = Pipeline(List(
    Stage("stage1", false, List(job1, job2)),
    Stage("stage2", false,List(job1))
  ))

  def main(arg: Array[String]): Unit = {
    print(pipeline)
  }
}
