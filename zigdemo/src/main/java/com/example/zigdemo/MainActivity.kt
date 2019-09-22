package com.example.zigdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.casanube.medical.ICallResult
import com.casanube.medical.ZigbeeSdk
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val instance = ZigbeeSdk.getInstance()
        bt_start.setOnClickListener {
            instance.startSdk(object : ICallResult {
                override fun getResult(isSuc: Boolean) {
                    runOnUiThread {
                        if (isSuc) {
                            Toast.makeText(this@MainActivity, "测试成功", Toast.LENGTH_SHORT).show()
                            Log.i("MainActivity","测试成功");
                        }else{
                            Toast.makeText(this@MainActivity, "测试失败", Toast.LENGTH_SHORT).show()
                            Log.i("MainActivity","测试失败");
                        }
                    }
                }
            })
        }

        bt_stop.setOnClickListener {
            instance.destory()
        }

    }


}
