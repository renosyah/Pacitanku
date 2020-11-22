package com.ardian.pacitanku.ui.activity.event;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ardian.pacitanku.BuildConfig;
import com.ardian.pacitanku.R;
import com.ardian.pacitanku.di.component.ActivityComponent;
import com.ardian.pacitanku.di.component.DaggerActivityComponent;
import com.ardian.pacitanku.di.module.ActivityModule;
import com.ardian.pacitanku.model.event.EventModel;
import com.ardian.pacitanku.model.upload.UploadResponse;
import com.ardian.pacitanku.ui.dialog.DialogSimpleEditText;
import com.ardian.pacitanku.ui.util.Loading;
import com.ardian.pacitanku.util.DateFormat;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EventActivity extends AppCompatActivity implements EventActivityContract.View {

    // presenter yang akan diinject
    @Inject
    public EventActivityContract.Presenter presenter;

    // konteks yang dipakai
    private Context context;

    // intent yang dipakai
    private Intent intent;

    // tombole kembali
    private ImageView back;

    // judul
    private TextView title;

    // data event
    private EventModel event;

    // apakah image berubah
    // untuk kebutuhan edit gambar event
    private Boolean changeImage = false;

    // scroll
    private ScrollView eventEditorScrollview;

    // form input untuk data event
    private EditText name,date,time,upload,reminder,description;

    // image event
    private ImageView datePicker,timePicker,uploadPicker,reminderPicker;

    // lokasi layout
    private CardView location;

    // tombol save
    private Button save;

    // code request
    private int PICK_IMAGE = 103;

    // file dalam bentuk byte array
    private byte[] uploadFile;

    // layout loading
    private Loading loading;

    // fungsi yg dipanggil saat activity
    // dibuat
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // panggil fungsi init widget
        initWidget();
    }

    // fungsi kedua untuk menginisialisasi
    // seleurh variabel yg telah dideklarasi
    private void initWidget(){
        this.context = this;

        // intent
        intent = getIntent();

        // memanggil fungsi inject
        injectDependency();

        // ke activity ini
        presenter.attach(this);

        // subscribe presenter
        presenter.subscribe();

        // inisialisasi layout loading
        loading = new Loading(context,findViewById(R.id.loading_layout),getString(R.string.sending));

        // hilangkan
        loading.setVisibility(false);

        // inisialisasi scroll
        eventEditorScrollview = findViewById(R.id.event_editor_scrollview);

        // inisialisasi judul
        title = findViewById(R.id.title_textview);

        // set nama judul
        title.setText(getString(R.string.title_add_event));


        // inisialisasi tombol kembali
        back = findViewById(R.id.back_imageview);

        // saat diklik
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // selesai dan hancurkan activity
                finish();
            }
        });

        // inisialisasi form nama
        name = findViewById(R.id.event_name_edittext);

        // inisialisasi form tanggal
        date = findViewById(R.id.date_edittext);

        // inisialisasi tombol form tanggal
        datePicker = findViewById(R.id.date_picker_imagview);

        // saat diklik
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // buat instance date dari tanggal sekarang
                Calendar now = Calendar.getInstance();

                // buka dialog date picker
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {

                            // saat tanggal dipilih
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

                                // buat instance
                                Calendar cal = Calendar.getInstance();
                                cal.set(Calendar.YEAR, year);
                                cal.set(Calendar.MONTH, monthOfYear);
                                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                // set tanggal di event
                                event.date = cal.getTime().getTime();

                                // tampilkan
                                date.setText(DateFormat.simpleDate(new Date(cal.getTime().getTime())));
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                // berikan warna
                dpd.setAccentColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));

                // tampilkan
                dpd.show(getSupportFragmentManager().beginTransaction(), "Datepickerdialog");

            }
        });

        // inisalisasi form waktu
        time = findViewById(R.id.timer_edittext);

        // inisalisasi tombole form waktu
        timePicker = findViewById(R.id.time_picker_imageview);

        // saat diklik
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // buat instance date dari tanggal sekarang
                Calendar now = Calendar.getInstance();

                // buka dialog time picker
                TimePickerDialog dpd = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {

                    // saat waktu dipilih
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

                        // set waktu pada tanggal event
                        now.setTime(new Date(event.date));
                        now.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        now.set(Calendar.MINUTE,minute);
                        now.set(Calendar.SECOND,0);

                        // set waktu
                        event.date = now.getTime().getTime();

                        // tampilkan tanggal
                        date.setText(DateFormat.simpleDate(new Date(event.date)));

                        // tampilkan waktu
                        time.setText(DateFormat.simpleTime(new Date(event.date)));
                    }

                },true);

                // berikan warna
                dpd.setAccentColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));

                // tampilkan
                dpd.show(getSupportFragmentManager().beginTransaction(), "Datepickerdialog");

            }
        });

        //inisialisasi tombol layout lokasi
        location = findViewById(R.id.location_picker_cardview);

        // saat diklik
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // tampilkan dialog edittext
                // agar user dapat menginputkan lokasi
                new DialogSimpleEditText(context,getString(R.string.location),event.address,new DialogSimpleEditText.OnDialogListener(){

                    @Override
                    public void onOk(String text) {

                        // set ke event data alamat
                        event.address = text;
                    }

                    @Override
                    public void onEmpty() {

                    }

                    @Override
                    public void onCancel() {

                    }
                }).show();

            }
        });

        // inisialisasi form upload
        upload = findViewById(R.id.upload_edittext);

        // inisialisasi tombol upload
        uploadPicker = findViewById(R.id.iupload_picker_imageview);

        // saat diklik
        uploadPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // buka gallery
                openGalery();
            }
        });

        // inisialisasi form reminder
        reminder = findViewById(R.id.reminder_edittext);

        // inisialisasi tombol reminder
        reminderPicker = findViewById(R.id.reminder_picker_imageview);

        // saat diklik
        reminderPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // buat instance date dari tanggal sekarang
                Calendar now = Calendar.getInstance();

                // buka dialog date picker
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {

                            // saat tanggal dipilih
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

                                // buat instance date dari tanggal sekarang
                                Calendar cal = Calendar.getInstance();
                                cal.set(Calendar.YEAR, year);
                                cal.set(Calendar.MONTH, monthOfYear);
                                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                // set ke data event
                                event.reminder = cal.getTime().getTime();

                                // tampilkan
                                reminder.setText(DateFormat.simpleDate(new Date(event.reminder)));
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                // beri warna
                dpd.setAccentColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));

                // tampilkan
                dpd.show(getSupportFragmentManager().beginTransaction(), "Datepickerdialog");

            }
        });

        // inisialisasi form deskripsi
        description = findViewById(R.id.description_edittext);

        // insialisasi tombol simpan
        save = findViewById(R.id.save_button);

        // setlabel untuk tombol simpan
        save.setText(getString(R.string.add_event));

        // simpan
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // validasi form
                if (checkEmpty()){
                    Toast.makeText(context,getString(R.string.empty_event_data),Toast.LENGTH_SHORT).show();
                    return;
                }

                // set nama ke data event
                event.name = name.getText().toString();

                // set deskripsi ke data event
                event.description = description.getText().toString();

                // set tanggal ke data event
                event.dateString = DateFormat.ISO_8601_FORMAT.format(event.date);

                // jika image diubah maka
                if (changeImage && uploadFile != null){

                    // upload terlebih dahulu
                    uploadImage(uploadFile);
                    return;
                }

                // kirimkan data event
                presenter.setEvent(event,true);
            }
        });

        // jika intent memiliki data event maka
        // seluruh form verubah menjadi edit event
        if (intent.hasExtra("event_edit")){

            // ambil data event dari intent
            event = (EventModel) intent.getSerializableExtra("event_edit");

            // ubah label
            title.setText(getString(R.string.title_edit_event)); save.setText(getString(R.string.save_event));

            // set value nama ke form
            name.setText(event.name);

            // set value deskripsi  ke form
            description.setText(event.description);

            // set value reminder ke form
            reminder.setText(DateFormat.simpleDate(new Date(event.reminder)));

            // set value upload ke form
            upload.setText(event.imageUrl);

            // set value tanggal ke form
            date.setText(DateFormat.simpleDate(new Date(event.date)));

            // set value waktu ke form
            time.setText(DateFormat.simpleTime(new Date(event.date)));

        }

        // jika event null
        // artinya form akan menjadi
        // form untuk membuat event baru
        if (event == null){

            // inisialisasi event data
            event = new EventModel();

            // buat uuid baru
            event.id = UUID.randomUUID().toString();
        }
    }

    // fungsi validasi form
    private boolean checkEmpty() {
        return (name.getText().toString().trim().isEmpty() ||
                date.getText().toString().trim().isEmpty() ||
                time.getText().toString().trim().isEmpty() ||
                upload.getText().toString().trim().isEmpty() ||
                reminder.getText().toString().trim().isEmpty()||
                description.getText().toString().trim().isEmpty()
        );
    }

    // fungsi untuk membuak gallery
    private void openGalery(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, PICK_IMAGE);
    }

    // fungsi untuk upload gambar ke server
    private void uploadImage(byte[] content){
        String name = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).format(System.currentTimeMillis()) + ".jpg";
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), content);
        MultipartBody.Part file = MultipartBody.Part.createFormData("file",name, requestFile);
        presenter.upload(file,true);
    }

    // fungsi untuk konvert gambar dari bitmap
    // ke byte array
    private byte[] bmpToByteArray(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    // pada saat loading set event
    @Override
    public void showProgressSetEvent(Boolean show) {
        loading.setVisibility(show);
        eventEditorScrollview.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    // pada saat error set event
    @Override
    public void showErrorSetEvent(String error) {
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }

    // pada saat event berhasil dikirim
    @Override
    public void onSetEvent() {

        // set result ke ok
        setResult(Activity.RESULT_OK);

        // selesai
        finish();
    }


    // pada saat loading upload gambar
    @Override
    public void showProgressUpload(Boolean show) {
        loading.setVisibility(show);
        eventEditorScrollview.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    // pada saat error upload gambar
    @Override
    public void showErrorUpload(String error) {
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }


    // pada saat gambar berhasil dikirim
    @Override
    public void onUploaded(UploadResponse response) {

        // set full url untuk gambar
        event.imageUrl = BuildConfig.HOSTING_URL + response.url;

        // kirim seluruh data event ke firebase
        presenter.setEvent(event,true);
    }

    // saat result request selesai
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // jika hasil ok
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE){

            // file telah dipilih
            upload.setText(R.string.file_choosed);

            // gambar telah diubah
            changeImage = true;

            // jika data tidak kosong
            if (data != null && data.getData() != null){

                // check versi android os
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

                    // decode bmp ke array byte
                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), data.getData());
                    try {
                        Bitmap bmp = ImageDecoder.decodeBitmap(source);
                        uploadFile = bmpToByteArray(bmp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {


                    // decode bmp ke array byte
                    try {
                        Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        uploadFile = bmpToByteArray(bmp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
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