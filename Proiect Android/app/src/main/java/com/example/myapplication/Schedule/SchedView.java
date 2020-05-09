package com.example.myapplication.Schedule;

import android.os.Bundle;

import com.example.myapplication.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class SchedView extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // show calendar_view layout
        setContentView(R.layout.calendar_view);

        // identify resources
        TabLayout weekDaysLayout = findViewById(R.id.weekDays);
        ViewPager viewPager = findViewById(R.id.pagerCalendar);

        // setup tabs
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SchedViewFragment(), "luni");
        adapter.addFragment(new SchedViewFragment(), "marti");
        adapter.addFragment(new SchedViewFragment(), "miercuri");
        adapter.addFragment(new SchedViewFragment(), "joi");
        adapter.addFragment(new SchedViewFragment(), "vineri");
        viewPager.setAdapter(adapter);

        weekDaysLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < weekDaysLayout.getTabCount(); i++) {
            weekDaysLayout.getTabAt(i).setIcon(R.drawable.ic_camera);
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
