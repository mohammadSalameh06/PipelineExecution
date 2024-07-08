package ae.network.migration.pipeline.models

case class Stage(name: String, isParallel: Boolean , jobs: List[Job])
