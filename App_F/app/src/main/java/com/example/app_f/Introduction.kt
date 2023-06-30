package com.example.app_f

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import me.relex.circleindicator.CircleIndicator


class Introduction : AppCompatActivity() {
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var textView: TextView
    private lateinit var circleIndicator: CircleIndicator
    private lateinit var linearLayout: LinearLayout
    private lateinit var viewPager: ViewPager
    private lateinit var relativeLayout: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Kiểm tra nếu đã chạy lần đầu tiên, thì chuyển sang Activity Login luôn
        val isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
            .getBoolean("first", true)

        if (isFirstRun) {
            // Nếu chạy lần đầu tiên, chuyển sang Activity Introduction và lưu trạng thái
            setContentView(R.layout.activity_introduction)

            viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
            textView = findViewById(R.id.skip)
            viewPager = findViewById(R.id.view_pager_intro)
            relativeLayout = findViewById(R.id.layout_bo)
            linearLayout = findViewById(R.id.layout_next)
            circleIndicator = findViewById(R.id.circle_indicator)

            viewPager.adapter = viewPagerAdapter
            circleIndicator.setViewPager(viewPager)

            textView.setOnClickListener{
                val lastPageIndex = viewPagerAdapter.count - 1
                viewPager.setCurrentItem(lastPageIndex, true)
            }
            linearLayout.setOnClickListener {
                val currentPosition = viewPager.currentItem
                val nextPageIndex = currentPosition + 1
                if (nextPageIndex < viewPagerAdapter.count) {
                    viewPager.setCurrentItem(nextPageIndex, true)
                    if (nextPageIndex == viewPagerAdapter.count - 1) {
                        linearLayout.visibility = View.GONE
                    }
                }
            }

            // Lưu trạng thái đã chạy lần đầu tiên
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("first", false).apply()
        } else {
            // Nếu không phải lần đầu tiên, chuyển sang Login
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
