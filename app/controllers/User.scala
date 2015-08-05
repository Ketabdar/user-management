package controllers

import play.api._
import play.api.mvc._
import models.SimpleClient_User
import models.UsersRepository
import play.api.libs.json.Json
import com.datastax.driver.core.utils.UUIDs
import models.User
import play.api.libs.json.JsError
import scala.concurrent.Future
import java.util.UUID
import scala.util.Try

//def getRepo{
//var cassandra: SimpleClient = cassandra = new SimpleClient("127.0.0.1")
//cassandra = new SimpleClient(app.configuration.getString("cassandra.node")
//  controller = new controllers.Application(new SongsRepository(cassandra))
//}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import models.JsonFormats_User._



//songsRepo: SongsRepository

class User extends Controller {

  def index2 = Action {
      //Ok(views.html.index("Your new application is ready."))
      Ok("Alpha")
  }

  def index3 = Action.async {
    var usersRepo: UsersRepository = new UsersRepository(new SimpleClient_User("127.0.0.1"))
    usersRepo.getAll.map(users => Ok(Json.toJson(users)))
  }

}
