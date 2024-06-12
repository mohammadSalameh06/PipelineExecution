package ae.network.migration.pipeline.models

case class Stage(name: String, isParallel: Boolean , job: List[Job])
