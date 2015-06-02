package com.coded2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.coded2.nabuwatercoach.AddWaterFragment;
import com.coded2.nabuwatercoach.MainWaterCoachFragment;
import com.coded2.nabuwatercoach.R;
import com.coded2.nabuwatercoach.SettingsWaterCoachFragment;
import com.razer.android.nabuopensdk.NabuOpenSDK;


public class MainActivity extends AppCompatActivity {

    private int current_screen;

    private static final int HOME =      1;
    private static final int SETTINGS =  2;
    private static final int ABOUT =     3;
    private static final int EXIT =      4;




    private Toolbar toolbar;
    int TITLES [] = {R.string.home,R.string.settings,R.string.About};
    int ICONS[] = {R.drawable.ic_home,R.drawable.ic_settings,R.drawable.ic_about};
    int DEFAULT_PROFILE = R.drawable.bottle;


    NabuOpenSDK nabuSdk;



    RecyclerView drawerRecyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    DrawerLayout drawer;
    ActionBarDrawerToggle toogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
         drawerRecyclerView = (RecyclerView) findViewById(R.id.menu_drawer_view);
        drawerRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        drawerRecyclerView.setLayoutManager(layoutManager);

        //handling touchs

        final GestureDetector gestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        drawerRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(),e.getY());
                if(child!=null && gestureDetector.onTouchEvent(e)){
                    drawer.closeDrawers();

                    int position = rv.getChildLayoutPosition(child);

                    navigate(position);

                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }
        });


        drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        toogle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open_drawer,R.string.close_drawer){};

        drawer.setDrawerListener(toogle);
        toogle.syncState();


        initAdapter();


        showMainWaterCoachFragment();


}
    //update the fragments
    private void navigate(int position) {

        Fragment fragment;

        switch (position){
            case HOME:
                fragment = new MainWaterCoachFragment();
                break;
            case SETTINGS:
                fragment = new SettingsWaterCoachFragment();
                break;
            case ABOUT:
                fragment = new AboutFragment();
                break;
            case EXIT:
                finish();
                return;
            default:
                fragment = new MainWaterCoachFragment();
        }

        current_screen = position;

        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.content_view,fragment).commit();

    }

    @Override
    public void onBackPressed() {

        if(current_screen!=HOME){
            showMainWaterCoachFragment();
            return;
        }

        super.onBackPressed();
    }

    private void initAdapter() {

        String nickName = PreferenceManager.getDefaultSharedPreferences(this).
                            getString(getString(R.string.pref_key_nickname), "");
        adapter = new DrawerAdapter(TITLES,ICONS, nickName, "",DEFAULT_PROFILE,MainActivity.this);
        drawerRecyclerView.setAdapter(adapter);
        DialogManager.hideLoading();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_drink_water) {
            AddWaterFragment dialog = new AddWaterFragment();
            dialog.show(getFragmentManager(),null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void showMainWaterCoachFragment(){
        MainWaterCoachFragment fragment = new MainWaterCoachFragment();
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.content_view,fragment).commit();
        current_screen = HOME;
    }

}
