package com.example.secondsessionprof

import android.app.Application
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest

class App: Application() {
    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    private val supabaseClient by lazy {
        createSupabaseClient("https://gxxabkkfpcjfysrjtnzp.supabase.co", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imd4eGFia2tmcGNqZnlzcmp0bnpwIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjYzMjA1NjgsImV4cCI6MjA0MTg5NjU2OH0.6QUUP-pJMG_oQNG77yb3Drhwuty34NYXlKwlQQeSzXs"){
            install(Postgrest)
            install(Auth)
        }
    }
    val baseAuthManager by lazy {
        BaseAuthManager(supabaseClient)
    }
}