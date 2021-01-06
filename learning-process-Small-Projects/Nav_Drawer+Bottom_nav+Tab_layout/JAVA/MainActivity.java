package com.example.barapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TabLayout tabLayout;
    TabItem fi,si,ti;
    ViewPager viewPager;
    PagerAdapter adapter;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_home_id:
                        myFragSelector(new HomeFragment());
                        break;
                    case R.id.bottom_explorer_id:
                        myFragSelector(new ExplorerFragment());
                        break;
                    case R.id.bottom_add_id:
                        myFragSelector(new AddFragment());
                        break;
                    case R.id.bottom_subscription_id:
                        myFragSelector(new SubscriptionFragment());
                        break;
                    case R.id.bottom_libraries_id:
                        myFragSelector(new LibrariesFragment());
                        break;
                    default: return;
                }
            }
        });
        viewPager=findViewById(R.id.view_pager);
        tabLayout=findViewById(R.id.tab_layout);
        fi=findViewById(R.id.fi_item);
        si=findViewById(R.id.si_item);
        ti=findViewById(R.id.ti_item);
        adapter=new PagerAdapter(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        navigationView=findViewById(R.id.nav_view);
        drawerLayout=findViewById(R.id.drawer_layout);
        // Ham Barger
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.start,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // end-of Ham Barger
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_new_group:
                        myFragSelector(new NewGroupFragment());
                        break;
                    case R.id.nav_secrect_chat:
                        myFragSelector(new NewSecrectChatFragment());
                        break;
                    case R.id.nav_new_channels:
                        myFragSelector(new NewChanelFragment());
                        break;
                    case R.id.nav_contacts:
                        myFragSelector(new ContactsFragment());
                        break;
                    case R.id.nav_calls:
                        myFragSelector(new CallFragment());
                        break;
                    case R.id.nav_saved_message:
                        myFragSelector(new SavedMessageFragment());
                        break;
                    case R.id.nav_settings:
                        myFragSelector(new SettingsFragment());
                        break;
                    case R.id.nav_invites_friends:
                        myFragSelector(new InvitesFriendsFragment());
                        break;
                    case R.id.nav_faq:
                        myFragSelector(new FAQFragment());
                        break;
                    default: return false;
                }
                return false;
            }
        });
    }
    // Ham Barger icon Clickable:
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }
    // end-of Ham Barger icon Clickable:

    void myFragSelector(Fragment fragment){
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
    }
    class PagerAdapter extends FragmentPagerAdapter {
        int tabsCount;
        public PagerAdapter(@NonNull FragmentManager fm, int behavior, int tabs) {
            super(fm, behavior);
            tabsCount=tabs;
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new CHATFragment();
                case 1:
                    return new STATUSFragment();
                case 2:
                    return new CALLSFragment();
                default:return null;
            }
        }
        @Override
        public int getCount() {
            return tabsCount;
        }
    }

}