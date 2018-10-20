package info.logger.sample

import android.app.Application
import android.os.StrictMode
import info.logger.fast.L


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        L.changeLogFileName("myfilenameAnywhereInFileSystem.log")
        L.setLogToFileEnabled(true, this)

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
