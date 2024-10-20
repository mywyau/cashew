package services

import cats.effect.Concurrent
import cats.syntax.all._
import com.github.t3hnar.bcrypt._
import models.users.{User, UserLoginRequest, UserRegistrationRequest}
import repositories.UserRepositoryAlgebra

import java.time.LocalDateTime

class AuthenticationService[F[_] : Concurrent](userRepository: UserRepositoryAlgebra[F]) {

  def registerUser(request: UserRegistrationRequest): F[Either[String, User]] = {
    // Hash the password
    val hashedPassword = request.password.boundedBcrypt

    val user =
      User(
        id = None,
        username = request.username,
        password_hash = hashedPassword,
        first_name = request.first_name,
        last_name = request.last_name,
        contact_number = request.contact_number,
        email = request.email,
        role = request.email,
        created_at = LocalDateTime.now
      )

    // Check if the username already exists
    userRepository.findByUsername(request.username).flatMap {
      case Some(_) => Concurrent[F].pure(Left("Username already exists"))
      case None => userRepository.createUser(user).as(Right(user))
    }
  }

  def loginUser(request: UserLoginRequest): F[Either[String, User]] = {
    userRepository.findByUsername(request.username).flatMap {
      case Some(user) if request.password.isBcryptedBounded(user.password_hash) =>
        Concurrent[F].pure(Right(user)) // Successfully authenticated
      case Some(_) =>
        Concurrent[F].pure(Left("Invalid password")) // Password did not match
      case None =>
        Concurrent[F].pure(Left("Username not found")) // Username not found
    }
  }
}

object AuthenticationService {
  def apply[F[_] : Concurrent](userRepository: UserRepositoryAlgebra[F]): AuthenticationService[F] =
    new AuthenticationService[F](userRepository)
}
