package id.lastare.anabul

import android.app.Application
import id.lastare.anabul.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AnabulApp : Application() {
    override fun onCreate() {
        super.onCreate()

        try {
            startKoin {
                androidLogger()
                androidContext(this@AnabulApp)
                modules(appModule)
            }
        } catch (e: Exception) {
            // Koin already started
        }
    }
}
