
// @GENERATOR:play-routes-compiler
// @SOURCE:/home/user/workspace/letsplay-master-new/conf/routes
// @DATE:Thu Aug 06 09:41:28 CEST 2015


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
