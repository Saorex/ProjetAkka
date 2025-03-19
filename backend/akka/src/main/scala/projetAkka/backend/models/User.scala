package projetAkka.backend.models

case class User(id: Int, username: String, email: String, passwordHash: String, walletKey: String)
