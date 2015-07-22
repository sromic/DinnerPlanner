package environment

object Environment {
  val PROD = "prod"
  val DEV = "dev"

  val environmentMode = scala.util.Properties.envOrElse("runMode", "dev")
}
