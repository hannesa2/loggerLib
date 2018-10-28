package info.logger.fast

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.util.Log
import androidx.annotation.NonNull
import org.jetbrains.annotations.Nullable
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.content.ContextCompat

object L {

    private var filename: String? = null
    private var fileEnable: Boolean = false
    private var application: Application? = null
    private var debug: Boolean = false

    private val DATE_FORMAT = "yyyy-MM-dd HH:mm:ssZ"
    private val CUT_LOGFILE = true
    private val KEEP_DAYS = 7
    private val WRITE_PERMISSION = false
    var isLogToFileEnabled = true
        private set
    private var sInitiated = false
    private var sOut: BufferedWriter? = null
    private var tempList: MutableList<String>? = null

    private enum class MsgType {
        E, W, I
    }

    class Builder(private val debug: Boolean) {
        private var filename: String? = null
        private var fileEnable: Boolean = false
        private var application: Application? = null

        fun build(): Unit {

            L.debug = debug
            L.filename = filename
            L.fileEnable = fileEnable
            L.application = application
        }

        fun changeLogFileName(filename: String): Builder {
            this.filename = filename
            return this
        }

        fun setLogToFileEnabled(fileEnable: Boolean, application: Application): Builder {
            this.fileEnable = fileEnable
            this.application = application
            return this
        }
    }

    @JvmStatic
    val logFile: File?
        get() {
            var gpxfile: File? = null
            val sRoot = Environment.getExternalStorageDirectory()
            if (sRoot != null && sRoot.canWrite()) {
                gpxfile = File(sRoot, L.filename)
            }
            return gpxfile
        }


    /**
     * Log a message on the verbose level
     *
     * @param message
     * @see .v
     */
    @JvmStatic
    fun v(message: String) {
        v(null, message)
    }

    /**
     * Log a message with tag on the verbose level
     *
     * @param tag
     * @param message
     */
    @JvmStatic
    fun v(tag: String?, message: String) {

        val tagAndMessage = formatLogTagAndMessage(tag, message)

        // only log to LogCat if we are in debug mode
        if (debug) {
            Log.v(tagAndMessage[0], tagAndMessage[1])
        }

        // we don't write verbose messages to the log file
    }

    /**
     * Convenience method for formatted log messages. The given object will be stringified unconditionally.
     *
     * @param tag     Tag for the log message.
     * @param message Message to be logged, containing formatting tags.
     * @param args    List of formatting parameters.
     * @see String.format
     */
    @JvmStatic
    fun v(@NonNull tag: String, @NonNull message: String, @Nullable vararg args: Any) {
        v(tag, String.format(message, *args))
    }

    /**
     * Log a message on the debug level
     *
     * @param message
     * @see .d
     */
    @JvmStatic
    fun d(message: String) {
        d(null, message)
    }

    /**
     * Log a message with tag on the debug level
     *
     * @param tag
     * @param message
     */
    @JvmStatic
    fun d(tag: String?, message: String) {

        val tagAndMessage = formatLogTagAndMessage(tag, message)

        // only log to LogCat if we are in debug mode
        if (debug) {
            Log.d(tagAndMessage[0], tagAndMessage[1])
        }

        // we don't write debug messages to the log file
    }

    /**
     * Convenience method for formatted log messages. The given object will be stringified unconditionally.
     *
     * @param tag     Tag for the log message.
     * @param message Message to be logged, containing formatting tags.
     * @param args    List of formatting parameters.
     * @see String.format
     */
    @JvmStatic
    fun d(@NonNull tag: String, @NonNull message: String, @Nullable vararg args: Any) {
        d(tag, String.format(message, *args))
    }

    /**
     * Log a message on the info level
     *
     * @param message
     * @see .i
     */
    @JvmStatic
    fun i(message: String) {
        i(null, message)
    }

    /**
     * Log a message with tag on the info level
     *
     * @param tag
     * @param message
     */
    @JvmStatic
    fun i(tag: String?, message: String) {

        val tagAndMessage = formatLogTagAndMessage(tag, message)

        // only log to LogCat if we are in debug mode
        if (debug) {
            Log.i(tagAndMessage[0], tagAndMessage[1])
        }

        // only executes if LOG_TO_FILE is enabled
        writeLogToSD(L.MsgType.I, tagAndMessage[1])
    }

    /**
     * Convenience method for formatted log messages. The given object will be stringified unconditionally.
     *
     * @param tag     Tag for the log message.
     * @param message Message to be logged, containing formatting tags.
     * @param args    List of formatting parameters.
     * @see String.format
     */
    @JvmStatic
    fun i(@NonNull tag: String, @NonNull message: String, @Nullable vararg args: Any) {
        i(tag, String.format(message, *args))
    }

    /**
     * Log a message on the warning level
     *
     * @param message
     * @see .w
     */
    @JvmStatic
    fun w(message: String) {
        w(null, message)
    }

    /**
     * Log a message with tag on the warning level
     *
     * @param tag
     * @param message
     */
    @JvmStatic
    fun w(tag: String?, message: String) {

        val tagAndMessage = formatLogTagAndMessage(tag, message)

        // only log to LogCat if we are in debug mode
        if (debug) {
            Log.w(tagAndMessage[0], tagAndMessage[1])
        }

        // only executes if LOG_TO_FILE is enabled
        writeLogToSD(L.MsgType.W, tagAndMessage[1])
    }

    /**
     * Convenience method for formatted log messages. The given object will be stringified unconditionally.
     *
     * @param tag     Tag for the log message.
     * @param message Message to be logged, containing formatting tags.
     * @param args    List of formatting parameters.
     * @see String.format
     */
    @JvmStatic
    fun w(@NonNull tag: String, @NonNull message: String, @Nullable vararg args: Any) {
        w(tag, String.format(message, *args))
    }

    /**
     * Log a Throwable on the warning level
     *
     * @param e
     */
    @JvmStatic
    fun w(e: Throwable) {
        if (debug) {
            e.printStackTrace()
        }

        val sw = StringWriter()
        val pw = PrintWriter(sw)
        e.printStackTrace(pw)
        // only executes if LOG_TO_FILE is enabled
        writeLogToSD(L.MsgType.W, sw.toString())
    }

    /**
     * Log a message on the error level
     *
     * @param message
     */
    @JvmStatic
    fun e(message: String) {
        e(null, message)
    }

    /**
     * Log a message with tag on the error level
     *
     * @param tag
     * @param message
     */
    @JvmStatic
    fun e(tag: String?, message: String) {

        val tagAndMessage = formatLogTagAndMessage(tag, message)

        // only log to LogCat if we are in debug mode
        if (debug) {
            Log.e(tagAndMessage[0], tagAndMessage[1])
        }

        // only executes if LOG_TO_FILE is enabled
        writeLogToSD(L.MsgType.E, tagAndMessage[1])
    }

    /**
     * Convenience method for formatted log messages. The given object will be stringified unconditionally.
     *
     * @param tag     Tag for the log message.
     * @param message Message to be logged, containing formatting tags.
     * @param args    List of formatting parameters.
     * @see String.format
     */
    @JvmStatic
    fun e(@NonNull tag: String, @NonNull message: String, @Nullable vararg args: Any) {
        e(tag, String.format(message, *args))
    }

    /**
     * Log a Throwable on the error level
     *
     * @param e
     */
    @JvmStatic
    fun e(e: Throwable) {
        if (debug) {
            e.printStackTrace()
        }

        val sw = StringWriter()
        val pw = PrintWriter(sw)
        e.printStackTrace(pw)
        // only executes if LOG_TO_FILE is enabled
        writeLogToSD(L.MsgType.E, sw.toString())
    }

    /**
     * If BuildConfig is DEBUG throws runtime exception,
     * otherwise just logs error
     *
     * @param tag
     * @param message
     */
    @JvmStatic
    fun throwExceptionOrLogError(tag: String, message: String) {
        if (debug) {
            throw RuntimeException("$tag $message")
        } else {
            e(tag, message)
        }
    }

    /**
     * ***************************************************
     * **************** PRIVATE METHODS ********************
     * ****************************************************
     */

    private fun formatLogTagAndMessage(tag: String?, message: String): Array<String> {

        var stackTraceElement: StackTraceElement? = null

        // search for first element in stacktrace after logging class
        var lastClassStackElement = -1
        val stackTrace = Thread.currentThread().stackTrace
        for (i in stackTrace.indices) {
            if (stackTrace[i].className == L::class.java.name) {
                lastClassStackElement = i
            } else if (lastClassStackElement >= 0) {
                stackTraceElement = stackTrace[i]
                break
            }
        }
        val methodName = "." + stackTraceElement!!.methodName + "()"
        val fullClassName = stackTraceElement.className
        val className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1)
        val lineNumber = stackTraceElement.lineNumber

        // if no tag is provided we use class name
        val logTag = tag ?: "-"

        // if tag already contains class name we omit it in log msg
        val logMsg = "$className$methodName, line:$lineNumber; $message"

        return arrayOf(logTag, logMsg)
    }

    /**
     * init logfile, but first clean up old entries. During this time, new lines are cached in an array.
     * After cleanup, cache is stored to file
     */
    private fun initFileLogging() {
        if (L.fileEnable) {
            if (!L.sInitiated) {
                L.sInitiated = true
                //tempList is needed during cleanup of Logfile, after cleanup info is transferred to file
                if (L.tempList == null) {
                    L.tempList = ArrayList()
                }
                try {
                    //first make a copy
                    val gpxfile = L.logFile
                    if (gpxfile != null) {
                        if (L.CUT_LOGFILE) {

                            val gpxfileTmp = File(gpxfile.absoluteFile.toString() + ".tmp")
                            gpxfile.renameTo(gpxfileTmp.absoluteFile)

                            // cut old log entries
                            cutLogFile(gpxfile, gpxfileTmp, L.KEEP_DAYS)
                            gpxfileTmp.delete()
                        }


                        val sGpxWriter = FileWriter(gpxfile, true)
                        L.sOut = BufferedWriter(sGpxWriter)

                        if (L.CUT_LOGFILE) {
                            // write during now cached lines to file
                            for (line in L.tempList!!) {
                                L.sOut!!.write(line)
                            }
                            L.tempList!!.clear()
                        }
                        L.sOut!!.flush()
                    } else {
                        Log.e("SD_LOGGER", "no loggerfile")
                    }
                } catch (e: IOException) {
                    Log.e("SD_LOGGER", "Could not write file " + e.message)
                }

            }
        }
    }

    private fun cutLogFile(toLogFile: File, toLogFileTmp: File, days: Int) {
        var skip = 0
        var write = 0
        val start = System.currentTimeMillis()
        val inStream: InputStream
        val sdf = SimpleDateFormat(L.DATE_FORMAT, Locale.getDefault())
        try {
            inStream = FileInputStream(toLogFileTmp)

            // if file the available for reading
            // out
            val sGpxWriter = FileWriter(toLogFile, true)
            val sOut = BufferedWriter(sGpxWriter)

            // prepare the file for reading
            val inputStreamReader = InputStreamReader(inStream)
            val buffReader = BufferedReader(inputStreamReader)

            var line: String?
            var inTimeRange = false
            // read every line of the file into the line-variable
            do {
                line = buffReader.readLine()
                if (!inTimeRange) {
                    var date: Long = 0
                    try {
                        date = sdf.parse(line!!.substring(0, line.indexOf("|"))).time
                    } catch (ignored: Exception) {
                    }

                    if (System.currentTimeMillis() < date + days * 1000 * 3600 * 24) {
                        inTimeRange = true
                    } else {
                        skip++
                        continue // test next line
                    }
                }
                // with the line to smaller file
                if (line != null) {
                    write++
                    sOut.write(line + "\n")
                }
            } while (line != null)
            sOut.flush()
            sOut.close()
            inStream.close()
            i("Write keep max " + days + "days (lines:" + write + ") in " + (System.currentTimeMillis() - start) + "ms")
        } catch (ex: Exception) {
            e("skip:$skip write:$write")
            e(ex)
        }

    }

    private fun hasAppWritePermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun writeLogToSD(kind: L.MsgType, log: String) {
        if (L.isLogToFileEnabled && WRITE_PERMISSION) {
            try {
                initFileLogging()
                val sdf = SimpleDateFormat.getInstance() as SimpleDateFormat
                sdf.applyPattern(L.DATE_FORMAT)
                val now = Date(System.currentTimeMillis())

                val logInfo = sdf.format(now) + "|" + kind.name + ":" + log + "\n"
                if (L.sOut != null) {
                    L.sOut!!.write(logInfo)
                    L.sOut!!.flush()
                } else {
                    L.tempList!!.add(logInfo)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

}