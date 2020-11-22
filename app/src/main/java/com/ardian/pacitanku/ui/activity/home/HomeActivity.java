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


// aktivitas menu home
public class HomeActivity extends AppCompatActivity implements HomeActivityContract.View {

    // presenter yang akan diinject
    @Inject
    public HomeActivityContract.Presenter presenter;

    // konteks yang dipakai
    private Context context;

    // menu navigasi yang digunakan
    private NavMenu menuLayout;

    // menu intro yang digunakan
    private Intro introLayout;

    // menu event list yang digunakan
    private EventLayout eventLayout;

    // scroll yang digunakan
    private NestedScrollView scroll;

    // tombol event
    private FloatingActionButton addEvent;

    // yang akan menentukan apakah user dapat memodifikasi
    // data data event
    private Boolean allowAddEvent = false;

    // limit data yang direquest
    private int limit = 5;

    // data array event
    private ArrayList<EventModel> events = new ArrayList<>();

    // code request activity saat edit dan tambah event
    // lalu kembali ke activity ini
    private int EVENT_ACTIVITY_RESULT = 134;

    // fungsi yg dipanggil saat activity
    // dibuat
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // panggil fungsi init widget
        initWidget();
    }

    // fungsi kedua untuk menginisialisasi
    // seleurh variabel yg telah dideklarasi
    private void initWidget(){
        this.context = this;

        // memanggil fungsi inject
        injectDependency();

        // ke activity ini
        presenter.attach(this);

        // subscribe presenter
        presenter.subscribe();

        // inisialisasi tombole tambah event
        addEvent = findViewById(R.id.addevent_button);

        // saat diklik
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // buka aktivitas tambah event
                Intent i = new Intent(context, EventActivity.class);
                startActivityForResult(i,EVENT_ACTIVITY_RESULT);
            }
        });

        // inisialisasi scroll layout
        scroll = findViewById(R.id.scroll);

        // saat di scroll
        scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                // jika scroll mentok kebawah maka
                if (scrollY >= v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight()) {

                    // tambahkan limit data
                    limit += 5;

                    // ambil data event
                    presenter.getEvents(limit);

                    // set menu layout ke event
                    menuLayout.setMenu(NavMenu.EVENT_MENU, new Unit<Integer>() {
                        @Override
                        public void invoke(Integer o) {

                            // tampilkan tombol tambah event
                            if (allowAddEvent) addEvent.show();
                        }
                    });

                    // jika scroll mentok ke atas maka
                } else if (scrollY == 0) {

                    // set menu layout ke home
                    menuLayout.setMenu(NavMenu.HOME_MENU, new Unit<Integer>() {
                        @Override
                        public void invoke(Integer o) {

                            // sembunyikan tombol tambah event
                            addEvent.hide();
                        }
                    });
                }
            }
        });

        // inisialisasi layout intro
        this.introLayout = new Intro(context,findViewById(R.id.home_intro));

        // set kontent layout
        introLayout.setContent(
                "https://cdn.idntimes.com/content-images/community/2020/07/img-20200728-142312-cf0cd752dc3016ea2b578284adcd4f37_600x400.jpg",
                "Pacitan",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
        );

        // tampilkan
        introLayout.setVisibility(View.VISIBLE);

        // inisialisasi event layou
        this.eventLayout = new EventLayout(context,findViewById(R.id.home_event));

        // set kontent
        eventLayout.setContent(events, new Unit<EventModel>() {

            // jika diklik
            @Override
            public void invoke(EventModel o) {
                Intent i = new Intent(context, DetailEventActivity.class);
                i.putExtra("event",o);
                startActivity(i);
            }

            // jika di klik edit
        },new Unit<EventModel>() {
            @Override
            public void invoke(EventModel o) {
                Intent i = new Intent(context, EventActivity.class);
                i.putExtra("event_edit",o);
                startActivityForResult(i,EVENT_ACTIVITY_RESULT);
            }

            // jika diklik hapus
        },new Unit<EventModel>() {
            @Override
            public void invoke(EventModel o) {

                // tampilkan dialog hapus
                new DeleteDialog(context, o.id, new Unit<String>() {
                    @Override
                    public void invoke(String id) {

                        // jika opsi ya dipilih maka
                        // kirim perintah hapus
                        presenter.deleteEvents(id);
                    }
                });

            }
        });

        // tampilkan
        eventLayout.setVisibility(View.VISIBLE);

        // tampilkan judul kecil
        eventLayout.showSmallTittle();

        // inisialisasi layout menu
        this.menuLayout = new NavMenu(context, findViewById(R.id.home_menu), new Unit<Integer>() {
            @Override
            public void invoke(Integer o) {

                // check menu apa yang dipilih
                switch (o){

                    // jika menu home maka
                    case NavMenu.HOME_MENU:

                        // sembunyikan tombol tambah event
                        addEvent.hide();

                        // tampilkan intro layout
                        introLayout.setVisibility(View.VISIBLE);

                        // tampilkan judul kecil
                        eventLayout.showSmallTittle();
                        break;

                    // jika menu event maka
                    case NavMenu.EVENT_MENU:

                        // tampilkan tombol tambah event
                        if (allowAddEvent) addEvent.show();

                        // sembuyikan intro layout
                        introLayout.setVisibility(View.GONE);

                        // tampilkan judul besar
                        eventLayout.showBigTittle();
                        break;
                    default:
                        break;
                }
            }
        });

        // tampilkan layout
        menuLayout.setVisibility(View.VISIBLE);

        // kirimkan perintah ambil event
        presenter.getEvents(limit);

        // check user type apakah admin atau bukan
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) presenter.getUserType(user.getUid());
    }

    // loaing saat event akan didapat
    @Override
    public void showProgressGetEvents(Boolean show) {

    }

    // pesan error saat event didapat
    @Override
    public void showErrorGetEvents(String error) {
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }

    // saat event didapatkan
    // maka
    @Override
    public void onGetEvents(@NonNull ArrayList<EventModel> event) {

        // clear data event yang ada
        events.clear();

        // tambahkan databaru
        events.addAll(event);

        // beritahu bahwa data berubah
        eventLayout.notifyAdapter();
    }

    // progress event dihapus
    @Override
    public void showProgressDeleteEvents(Boolean show) {

    }

    // pesan error saat event dihapus
    @Override
    public void showErrorDeleteEvents(String error) {
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }

    // saat event berhasil dihapus
    @Override
    public void onDeleteEvents() {

        // reload data baru
        presenter.getEvents(limit);
    }

    // saat type user didapat maka
    @Override
    public void onGetUserType(@NonNull UserType userType) {

        // jika adamin
        if (userType.type.equals(UserType.ADMIN)) {

            // opsi edit dierbolehkan
            eventLayout.setEnableOpt(true); allowAddEvent = true;

            // tombol tambah event ditampilkan
            addEvent.hide();
        }
    }


    // saat event berhasil diedit atau ditambah maka
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // validasi data yg datang
        if (resultCode == Activity.RESULT_OK && requestCode == EVENT_ACTIVITY_RESULT){

            // reload event layout dengan data baru
            presenter.getEvents(limit);
        }
    }

    // fungsi yg akan dipanggil saat activity
    // dihancurkan
    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unsubscribe();
    }

    // pemanggilan register
    // dependensi injeksi untuk aktivity ini
    private void injectDependency(){
        ActivityComponent listcomponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .build();

        listcomponent.inject(this);
    }
}