package com.af.aasthafincorp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.af.aasthafincorp.databinding.ActivityMainBinding;
import com.af.aasthafincorp.ui.Accounts.ViewAccountsFragment;
import com.af.aasthafincorp.ui.CasualMeetings.ViewCasualMeetingsFragment;
import com.af.aasthafincorp.ui.DashboardFragment;
import com.af.aasthafincorp.ui.Leads.ViewLeadsFragment;
import com.af.aasthafincorp.ui.Meetings.ViewMeetingsFragment;
import com.af.aasthafincorp.ui.Task.TasksFragment;
import com.af.aasthafincorp.ui.Todo.TodoFragment;
import com.af.aasthafincorp.ui.sales.ViewSalesFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    public NavController navController;
    ExpandableListView expandableListView;
    CustomExpandableListAdapter expandableListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        View header = binding.navView.getHeaderView(0);


        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        navigationView.setItemIconTintList(null);



        expandableListView = findViewById(R.id.expandableListView);

        // Prepare data
        List<String> parentList = new ArrayList<>();
        HashMap<String, List<String>> childList = new HashMap<>();

        // Add non-expandable items
        parentList.add("Dashboard");
        parentList.add("Meeting Calendar");
        parentList.add("Todo");
        // Tasks Dropdown
        parentList.add("Tasks");
        List<String> tasks = new ArrayList<>();
        tasks.add("Create Task");
        tasks.add("View Tasks");
        childList.put("Tasks", tasks);
        // Meetings Dropdown
        parentList.add("Meetings");
        List<String> meetings = new ArrayList<>();
        meetings.add("Create Meeting");
        meetings.add("View Meetings");
        childList.put("Meetings", meetings);
        // Casual Meetings Dropdown
        parentList.add("Casual Meetings");
        List<String> casualmeetings = new ArrayList<>();
        casualmeetings.add("Create Casual Meeting");
        casualmeetings.add("View Casual Meetings");
        childList.put("Casual Meetings", casualmeetings);

        // Add non-expandable items
        parentList.add("View Leads");
        parentList.add("View Accounts");

        // business Achievements Dropdown
        parentList.add("Business Achievements");
        List<String> ba = new ArrayList<>();
        ba.add("Create Sale");
        ba.add("View Sales");
        childList.put("Business Achievements", ba);
        // Contacts Dropdown
        parentList.add("Contacts");
        List<String> contacts = new ArrayList<>();
        contacts.add("Pending");
        contacts.add("Not Responding");
        contacts.add("Complete");
        contacts.add("Switch Off");
        contacts.add("Not Responding");
        childList.put("Contacts", contacts);
        // Reference Dropdown
        parentList.add("Reference");
        List<String> reference = new ArrayList<>();
        reference.add("Add New");
        reference.add("View All");
        childList.put("Reference", reference);
        // Reference Dropdown
        parentList.add("Leaves");
        List<String> leaves = new ArrayList<>();
        leaves.add("Apply For Leave");
        leaves.add("View All Leaves");
        childList.put("Leaves", leaves);
        // Reports Dropdown
        parentList.add("Reports");
        List<String> reports = new ArrayList<>();
        reports.add("Meeting");
        reports.add("Casual Meeting");
        reports.add("Lead");
        reports.add("Account");
        childList.put("Reports", reports);
        // Reports Dropdown
        parentList.add("Announcements");
        List<String> announcements = new ArrayList<>();
        announcements.add("Personal Notice");
        announcements.add("Salary Slip");
        announcements.add("KRA");
        announcements.add("Circular");
        childList.put("Announcements", announcements);
        // Reports Dropdown
        parentList.add("Training Files");
        List<String>  training = new ArrayList<>();
        training.add("Product Training");
        training.add("Operational Training");
        childList.put("Training Files", training);


        // Set adapter
        expandableListAdapter = new CustomExpandableListAdapter(this, parentList, childList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (groupPosition == 3) {  // Tasks
                    if (childPosition == 0) {
                        //openFragment();
                        drawer.closeDrawer(GravityCompat.START);
                    } else if (childPosition == 1) {
                        openFragment(new TasksFragment(),"View Tasks");
                        drawer.closeDrawer(GravityCompat.START);
                    }
                }
                if (groupPosition == 4) {  // Meetings
                    if (childPosition == 0) {
                        //openFragment();
                        drawer.closeDrawer(GravityCompat.START);
                    } else if (childPosition == 1) {
                        openFragment(new ViewMeetingsFragment(),"View All Meetings");
                        drawer.closeDrawer(GravityCompat.START);
                    }
                }
                if (groupPosition == 5) {  // Casual Meetings
                    if (childPosition == 0) {
                        //openFragment();
                        drawer.closeDrawer(GravityCompat.START);
                    } else if (childPosition == 1) {
                        openFragment(new ViewCasualMeetingsFragment(),"View All Casual Meetings");
                        drawer.closeDrawer(GravityCompat.START);
                    }
                }
                if (groupPosition == 8) {  // Business Achievements
                    if (childPosition == 0) {
                        //openFragment();
                        drawer.closeDrawer(GravityCompat.START);
                    } else if (childPosition == 1) {
                        openFragment(new ViewSalesFragment(),"View All Sales");
                        drawer.closeDrawer(GravityCompat.START);
                    }
                }
                if (groupPosition == 9) {  // Contacts

                }
                if (groupPosition == 10) {  // References

                }
                if (groupPosition == 11) {  // Leaves

                }
                if (groupPosition == 12) {  // Reports

                }
                if (groupPosition == 13) {  // Announcements

                }
                if (groupPosition == 14) {  // Training Files

                }
                return true;
            }
        });
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener(){
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (groupPosition == 0) {  // Dashboard
                    openFragment(new DashboardFragment(),"Dashboard");
                    drawer.closeDrawer(GravityCompat.START);
                }else if (groupPosition == 1) {  // Meeting Calendar
                    openFragment(new TodoFragment(),"Meeting Calendar");
                    drawer.closeDrawer(GravityCompat.START);
                }else if (groupPosition == 2) {  // Todo
                    openFragment(new TodoFragment(),"Todo");
                    drawer.closeDrawer(GravityCompat.START);
                }else if (groupPosition == 6) {  // Leads
                    openFragment(new ViewLeadsFragment(),"All Leads");
                    drawer.closeDrawer(GravityCompat.START);
                }else if (groupPosition == 7) {  // Accounts
                    openFragment(new ViewAccountsFragment(),"Accounts");
                    drawer.closeDrawer(GravityCompat.START);
                }

                return false;
            }
        });





        // Initialize the NavigationController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_view_task,R.id.nav_todo,R.id.nav_view_meeting,R.id.nav_view_casual_meeting,R.id.nav_view_leads,R.id.nav_view_accounts)
                .setDrawerLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void openFragment(Fragment fragment, String title) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment_content_main, fragment);
        transaction.addToBackStack(null); // Optional, adds the fragment to the back stack
        transaction.commit();
        // Update the ActionBar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

}