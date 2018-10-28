package info.logger.sample

import android.app.Application
import android.os.StrictMode
import info.logger.fast.L

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        L.Builder(BuildConfig.DEBUG)
                .logFileName("myfilenameAnywhereInFileSystem.log")
                .logToFile(true, this)
                .build()

        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build())

        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build())
    }

}
