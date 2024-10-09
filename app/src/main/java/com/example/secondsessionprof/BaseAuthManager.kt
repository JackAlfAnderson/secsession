package com.example.secondsessionprof

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.OtpType
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.providers.builtin.OTP
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.Serializable

class BaseAuthManager(private val supabaseClient: SupabaseClient) {

    suspend fun signUp(fullname: String, phone: String, email: String, password:String){
        supabaseClient.auth.signUpWith(Email){
            this.email = email
            this.password = password
        }
        supabaseClient.postgrest["profiles"].insert(
            Profiles(
                fullname = fullname,
                phone = phone,
                email = email
            )
        )
    }

    suspend fun signIn(email:String, password: String){
        supabaseClient.auth.signInWith(Email){
            this.email = email
            this.password = password
        }
    }

    suspend fun forgotPassword(email: String){
        supabaseClient.auth.signInWith(OTP){
            this.email = email
        }
    }

    suspend fun otpVerification(email: String, otp: String){
        supabaseClient.auth.verifyEmailOtp(OtpType.Email.EMAIL, email, otp)
    }

    suspend fun newPassword(email: String, password: String){
        supabaseClient.auth.modifyUser {
            this.email = email
            this.password = password
        }
    }



}
@Serializable
data class Profiles(
    val id: Int? = null,
    val created_at: String = "",
    val fullname: String,
    val phone: String,
    val email: String,
    val avatar: String = "",
    val balance: Double = 0.0,
    val rider: Boolean = false
)