package navarez.katia.proyectofinal.data.repository

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    val usuarioActual: FirebaseUser? get() = auth.currentUser

    suspend fun registrar(correo: String, password: String): Result<FirebaseUser> = try {
        val res = auth.createUserWithEmailAndPassword(correo, password).await()
        Result.success(res.user!!)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun login(correo: String, password: String): Result<FirebaseUser> = try {
        val res = auth.signInWithEmailAndPassword(correo, password).await()
        Result.success(res.user!!)
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun cerrarSesion() = auth.signOut()

    suspend fun cambiarPassword(passwordActual: String, passwordNueva: String): Result<Unit> = try {
        val usuario = auth.currentUser ?: throw IllegalStateException("No hay sesión activa")
        val correo = usuario.email ?: throw IllegalStateException("La cuenta no tiene correo asociado")
        val credencial = EmailAuthProvider.getCredential(correo, passwordActual)
        usuario.reauthenticate(credencial).await()
        usuario.updatePassword(passwordNueva).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}