package com.example.app_f


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter



class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }


    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> Introduction0Fragment()
            1 -> Introduction1Fragment()
            2 -> Introduction2Fragment()
            else -> Introduction0Fragment() // Giá trị mặc định
        }
    }
}