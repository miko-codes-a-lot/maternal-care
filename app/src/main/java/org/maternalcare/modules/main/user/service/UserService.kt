package org.maternalcare.modules.main.user.service

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import org.maternalcare.modules.main.user.model.UserDto
import java.util.UUID
import javax.inject.Inject

class UserService @Inject constructor(fdb: FirebaseDatabase) {
    private val userRefs = fdb.getReference("users")

    suspend fun fetch(): List<UserDto> {
        val snapshot = userRefs.get().await()
        return snapshot.children.map { userSnapshot ->
            userSnapshot.getValue(UserDto::class.java)!!
        }
    }

    suspend fun create(user: UserDto): Result<UserDto> {
        return try {
            user.id = UUID.randomUUID().toString()
            userRefs.child(user.id).setValue(user).await()

            Result.success(user)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}