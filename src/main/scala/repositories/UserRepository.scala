package repositories

import cats.effect.Sync
import doobie._
import doobie.implicits._
import doobie.implicits.javasql._
import doobie.util.meta.Meta
import models.users.User

import java.sql.{Date, Timestamp}
import java.time.{LocalDate, LocalDateTime}


trait UserRepositoryAlgebra[F[_]] {
  def createUser(user: User): F[Int]

  def findByUsername(username: String): F[Option[User]]
}

class UserRepository[F[_] : Sync](transactor: Transactor[F]) extends UserRepositoryAlgebra[F] {

  // Meta instance to map between LocalDateTime and Timestamp
  implicit val localDateTimeMeta: Meta[LocalDateTime] =
    Meta[Timestamp].imap(_.toLocalDateTime)(Timestamp.valueOf)

  // Meta instance to map between LocalDate
  implicit val localDateMeta: Meta[LocalDate] =
    Meta[Date].imap(_.toLocalDate)(Date.valueOf)

  def createUser(user: User): F[Int] = {
    sql"""
      INSERT INTO users (
        username, password_hash, name, contact_number, email, role, created_at
      ) VALUES (
        ${user.username}, ${user.password_hash}, ${user.username},
        ${user.contact_number}, ${user.email}, ${user.role}, ${user.created_at}
      )
    """.update.run.transact(transactor)
  }

  def findByUsername(username: String): F[Option[User]] = {
    sql"SELECT * FROM users WHERE username = $username"
      .query[User]
      .option
      .transact(transactor)
  }
}
