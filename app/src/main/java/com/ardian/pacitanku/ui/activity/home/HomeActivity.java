package com.ardian.pacitanku.ui.activity.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ardian.pacitanku.R;
import com.ardian.pacitanku.di.component.ActivityComponent;
import com.ardian.pacitanku.di.component.DaggerActivityComponent;
import com.ardian.pacitanku.di.module.ActivityModule;
import com.ardian.pacitanku.model.event.EventModel;
import com.ardian.pacitanku.model.userType.UserType;
import com.ardian.pacitanku.ui.activity.detailEvent.DetailEventActivity;
import com.ardian.pacitanku.ui.activity.event.EventActivity;
import com.ardian.pacitanku.ui.dialog.DeleteDialog;
import com.ardian.pacitanku.ui.util.EventLayout;
import com.ardian.pacitanku.ui.util.Intro;
import com.ardian.pacitanku.ui.util.NavMenu;
import com.ardian.pacitanku.util.Unit;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import javax.inject.Inject;

public class HomeActivity extends AppCompatActivity implements HomeActivityContract.View {

    @Inject
    public HomeActivityContract.Presenter presenter;

    private Context context;

    private NavMenu menuLayout;
    private Intro introLayout;
    private EventLayout eventLayout;

    private NestedScrollView scroll;

    private FloatingActionButton addEvent;
    private Boolean allowAddEvent = false;

    private int limit = 5;
    private ArrayList<EventModel> events = new ArrayList<>();

    private int EVENT_ACTIVITY_RESULT = 134;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initWidget();
    }

    private void initWidget(){
        this.context = this;

        injectDependency();
        presenter.attach(this);
        presenter.subscribe();

        addEvent = findViewById(R.id.addevent_button);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, EventActivity.class);
                startActivityForResult(i,EVENT_ACTIVITY_RESULT);
            }
        });

        scroll = findViewById(R.id.scroll);
        scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY >= v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight()) {
                    limit += 5;
                    presenter.getEvents(limit);
                    menuLayout.setMenu(NavMenu.EVENT_MENU);
                }
            }
        });

        this.introLayout = new Intro(context,findViewById(R.id.home_intro));
        introLayout.setContent(
                "https://cdn.idntimes.com/content-images/community/2020/07/img-20200728-142312-cf0cd752dc3016ea2b578284adcd4f37_600x400.jpg",
                "Pacitan",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
        );
        introLayout.setVisibility(View.VISIBLE);

        this.eventLayout = new EventLayout(context,findViewById(R.id.home_event));
        eventLayout.setContent(events, new Unit<EventModel>() {
            @Override
            public void invoke(EventModel o) {
                Intent i = new Intent(context, DetailEventActivity.class);
                i.putExtra("event",o);
                startActivity(i);
            }
        },new Unit<EventModel>() {
            @Override
            public void invoke(EventModel o) {
                Intent i = new Intent(context, EventActivity.class);
                i.putExtra("event_edit",o);
                startActivityForResult(i,EVENT_ACTIVITY_RESULT);
            }
        },new Unit<EventModel>() {
            @Override
            public void invoke(EventModel o) {

                new DeleteDialog(context, o.id, new Unit<String>() {
                    @Override
                    public void invoke(String id) {
                        presenter.deleteEvents(id);
                    }
                });

            }
        });
        eventLayout.setVisibility(View.VISIBLE);
        eventLayout.showSmallTittle();

        this.menuLayout = new NavMenu(context, findViewById(R.id.home_menu), new Unit<Integer>() {
            @Override
            public void invoke(Integer o) {
                switch (o){
                    case NavMenu.HOME_MENU:
                        addEvent.hide();
                        introLayout.setVisibility(View.VISIBLE);
                        eventLayout.showSmallTittle();
                        break;
                    case NavMenu.EVENT_MENU:
                        if (allowAddEvent) addEvent.show();
                        introLayout.setVisibility(View.GONE);
                        eventLayout.showBigTittle();
                        break;
                    default:
                        break;
                }
            }
        });

        menuLayout.setVisibility(View.VISIBLE);
        presenter.getEvents(limit);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) presenter.getUserType(user.getUid());
    }

    @Override
    public void showProgressGetEvents(Boolean show) {

    }

    @Override
    public void showErrorGetEvents(String error) {
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetEvents(@NonNull ArrayList<EventModel> event) {
        events.clear();
        events.addAll(event);
        eventLayout.notifyAdapter();
    }

    @Override
    public void showProgressDeleteEvents(Boolean show) {

    }

    @Override
    public void showErrorDeleteEvents(String error) {
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteEvents() {
        presenter.getEvents(limit);
    }

    @Override
    public void onGetUserType(@NonNull UserType userType) {
        if (userType.type.equals(UserType.ADMIN)) {
            eventLayout.setEnableOpt(true);
            allowAddEvent = true;
            addEvent.hide();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == EVENT_ACTIVITY_RESULT){
            presenter.getEvents(limit);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unsubscribe();
    }


    private void injectDependency(){
        ActivityComponent listcomponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .build();

        listcomponent.inject(this);
    }
}