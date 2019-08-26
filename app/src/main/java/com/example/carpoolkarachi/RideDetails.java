package com.example.carpoolkarachi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class RideDetails extends AppCompatActivity {
    private ViewPager mViewPager;
    private LatLng SourceLatLng,DestLatLng;
    Bundle bundle;
    Frag1 f1;


    public LatLng getSourceLatLng() {
        return SourceLatLng;
    }

    public LatLng getDestLatLng() {
        return DestLatLng;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details);
        SourceLatLng=getIntent().getExtras().getParcelable("sourceLatLng");
        DestLatLng=getIntent().getExtras().getParcelable("destLatLng");
        bundle = new Bundle();
        bundle.putParcelable("s1", SourceLatLng);
        bundle.putParcelable("d1", DestLatLng);
f1 = new Frag1();
f1.setArguments(bundle);


        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(f1, "Routes");

        adapter.addFragment(new Frag2(), "Ride Detail");
        viewPager.setAdapter(adapter);
    }

    class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
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
