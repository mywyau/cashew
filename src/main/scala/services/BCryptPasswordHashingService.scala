//package services
//
//import cats.effect.Sync
//
//trait PasswordHashingService[F[_]] {
//  def hashPassword(plainTextPassword: String): F[String]
//
//  def checkPassword(plainTextPassword: String, hashedPassword: String): F[Boolean]
//}
//
//class BCryptPasswordHashingService[F[_] : Sync] extends PasswordHashingService[F] {
//
//  override def hashPassword(plainTextPassword: String): F[String] =
//    Sync[F].delay {
//      plainTextPassword.bcrypt
//    }
//
//  override def checkPassword(plainTextPassword: String, hashedPassword: String): F[Boolean] =
//    Sync[F].delay {
//      plainTextPassword.isBcryptedBounded(hashedPassword)
//    }
//}
//
//object PasswordHashingService {
//  def apply[F[_] : Sync]: PasswordHashingService[F] = new BCryptPasswordHashingService[F]
//}
