package models

import java.util.UUID

import scala.collection.convert.WrapAsScala
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.data.validation.ValidationError
import com.datastax.driver.core.utils.UUIDs
import com.datastax.driver.core.BoundStatement
import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.datastax.driver.core.Row

case class User(id: UUID, lastname: String)

class UsersRepository(client: SimpleClient_User) {

  import Utils._

  def getAll(implicit ctxt: ExecutionContext): Future[List[User]] = {

    import WrapAsScala.iterableAsScalaIterable

    client.getRows.toScalaFuture.map { rows =>
      rows.map(row => user(row)).toList
    }
  }

  private def user(row: Row): User =
    User(row.getUUID("id"), row.getString("lastname"))

  def getById(id: UUID)(implicit ctxt: ExecutionContext): Future[User] = {
    val stmt = new BoundStatement(client.session.prepare("SELECT * FROM gee.users WHERE id = ?;"))
    client.session.executeAsync(stmt.bind(id)).toScalaFuture.map(rs => user(rs.one))
  }

  def insert(lastname: String)(implicit ctxt: ExecutionContext): Future[UUID] = {
    val stmt = new BoundStatement(client.session.prepare("INSERT INTO gee.users (id, lastname) VALUES (?, ?);"))
    val id = UUIDs.timeBased
    client.session.executeAsync(stmt.bind(id, lastname)).toScalaFuture.map(rs => id)
  }
}

object JsonFormats_User {

  /**
   * Deserializer for java.util.UUID, from latest play Reads (was added on 2014-03-01 to master,
   * see https://github.com/playframework/playframework/pull/2428)
   */
  private def uuidReader(checkUuuidValidity: Boolean = false): Reads[java.util.UUID] = new Reads[java.util.UUID] {
    import java.util.UUID
    import scala.util.Try
    def check(s: String)(u: UUID): Boolean = (u != null && s == u.toString())
    def parseUuid(s: String): Option[UUID] = {
      val uncheckedUuid = Try(UUID.fromString(s)).toOption

      if (checkUuuidValidity) {
        uncheckedUuid filter check(s)
      } else {
        uncheckedUuid
      }
    }

    def reads(json: JsValue) = json match {
      case JsString(s) => {
        parseUuid(s).map(JsSuccess(_)).getOrElse(JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.uuid")))))
      }
      case _ => JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.uuid"))))
    }
  }

  private implicit val uuidReads: Reads[java.util.UUID] = uuidReader()
  private implicit val uuidWrites: Writes[UUID] = Writes { uuid => JsString(uuid.toString) }

  implicit val userFormat: Format[User] = Json.format[User]
  implicit val userDataReads = (
    (__ \ 'lastname).read[String] )
}
