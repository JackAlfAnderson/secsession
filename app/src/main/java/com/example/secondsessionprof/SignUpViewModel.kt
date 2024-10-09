package com.example.secondsessionprof

import androidx.lifecycle.ViewModel
import com.example.secondsessionprof.data.emailSave.EmailManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpViewModel(private val baseAuthManager: BaseAuthManager, private val emailManager: EmailManager): ViewModel() {
    val scope = CoroutineScope(Dispatchers.IO)
    fun signUp(fullName:String, phone: String, email: String, password: String){
        scope.launch {
            baseAuthManager.signUp(fullname = fullName, phone = phone, email = email, password = password)
        }
    }
    fun set(email: String){
        emailManager.set(email)
    }
}