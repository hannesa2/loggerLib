package info.logger.sample

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import info.logger.fast.L
import info.logger.sample.R.id.buttonLog
import info.logger.sample.R.id.textViewObject
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonLog.setOnClickListener {
            run {
                L.d(textViewObject.text.toString())
            }
        }

    }
}
