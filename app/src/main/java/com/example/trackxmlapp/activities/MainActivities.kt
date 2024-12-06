package com.example.trackxmlapp.activities

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import com.example.trackxmlapp.R
import com.example.trackxmlapp.databinding.ActivityMainActivitiesBinding
import com.example.trackxmlapp.services.FloatingWidgetService

class MainActivities : BaseActivity() {
    private var binding: ActivityMainActivitiesBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainActivitiesBinding.inflate(layoutInflater)

        setContentView(binding?.root)

//        val seekBarHarga = findViewById<SeekBar>(R.id.seek_bar_harga)
        val etHarga = findViewById<EditText>(R.id.et_min_price)
        val startButton = findViewById<Button>(R.id.startButton)

        binding?.tvMemberName?.text = "Fadhol"
        binding?.tvMemberEmail?.text = "fadhol@mail.com"

//        seekBarHarga.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                etHarga.setText(progress.toString())
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar?) {
//                // Bisa menambahkan aksi lain ketika pengguna mulai menggeser
//            }
//
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                // Bisa menambahkan aksi lain ketika pengguna selesai menggeser
//            }
//        })

        startButton.setOnClickListener {
            if (Settings.canDrawOverlays(this)) {
                startFloatingWidgetService()
            } else {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                startActivity(intent)
            }
        }
    }

    private fun startFloatingWidgetService() {
        startService(Intent(this, FloatingWidgetService::class.java))
        showToast("Floating widget telah dimulai.")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

