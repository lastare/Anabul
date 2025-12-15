package id.lastare.anabul.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import id.lastare.anabul.domain.model.User
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AuthRepositoryImplTest {

    private lateinit var authRepository: AuthRepositoryImpl
    private val firebaseAuth: FirebaseAuth = mockk()
    private val taskAuthResult: Task<AuthResult> = mockk()
    private val authResult: AuthResult = mockk()
    private val firebaseUser: FirebaseUser = mockk()

    @Before
    fun setup() {
        // Mock static extension function Task.await()
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        
        authRepository = AuthRepositoryImpl(firebaseAuth)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `signUp should return success when firebase returns user`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val uid = "user123"
        
        every { firebaseAuth.createUserWithEmailAndPassword(email, password) } returns taskAuthResult
        coEvery { taskAuthResult.await() } returns authResult
        every { authResult.user } returns firebaseUser
        every { firebaseUser.uid } returns uid
        every { firebaseUser.email } returns email

        // When
        val result = authRepository.signUp(email, password)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(uid, result.getOrNull()?.id)
        assertEquals(email, result.getOrNull()?.email)
    }

    @Test
    fun `signUp should return failure when firebase fails`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val errorMessage = "Email already in use"
        
        every { firebaseAuth.createUserWithEmailAndPassword(email, password) } returns taskAuthResult
        coEvery { taskAuthResult.await() } throws Exception(errorMessage)

        // When
        val result = authRepository.signUp(email, password)

        // Then
        assertTrue(result.isFailure)
        assertEquals(errorMessage, result.exceptionOrNull()?.message)
    }
    
    @Test
    fun `signIn should return success when firebase returns user`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val uid = "user123"
        
        every { firebaseAuth.signInWithEmailAndPassword(email, password) } returns taskAuthResult
        coEvery { taskAuthResult.await() } returns authResult
        every { authResult.user } returns firebaseUser
        every { firebaseUser.uid } returns uid
        every { firebaseUser.email } returns email

        // When
        val result = authRepository.signIn(email, password)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(uid, result.getOrNull()?.id)
        assertEquals(email, result.getOrNull()?.email)
    }
}
