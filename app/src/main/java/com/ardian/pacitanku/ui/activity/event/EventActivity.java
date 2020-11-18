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

    @Inject
    public EventActivityContract.Presenter presenter;

    private Context context;
    private Intent intent;

    private ImageView back;
    private TextView title;

    private EventModel event;
    private Boolean changeImage = false;

    private ScrollView eventEditorScrollview;
    private EditText name,date,time,upload,reminder,description;
    private ImageView datePicker,timePicker,uploadPicker,reminderPicker;
    private CardView location;
    private Button save;

    private int PICK_IMAGE = 103;
    private byte[] uploadFile;

    private Loading loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        initWidget();
    }

    private void initWidget(){
        this.context = this;

        intent = getIntent();

        injectDependency();
        presenter.attach(this);
        presenter.subscribe();

        loading = new Loading(context,findViewById(R.id.loading_layout),getString(R.string.sending));
        loading.setVisibility(false);

        eventEditorScrollview = findViewById(R.id.event_editor_scrollview);

        title = findViewById(R.id.title_textview);
        title.setText(getString(R.string.title_add_event));

        back = findViewById(R.id.back_imageview);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        name = findViewById(R.id.event_name_edittext);

        date = findViewById(R.id.date_edittext);
        datePicker = findViewById(R.id.date_picker_imagview);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar cal = Calendar.getInstance();
                                cal.set(Calendar.YEAR, year);
                                cal.set(Calendar.MONTH, monthOfYear);
                                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                event.date = cal.getTime().getTime();
                                date.setText(DateFormat.simpleDate(new Date(cal.getTime().getTime())));
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setAccentColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
                dpd.show(getSupportFragmentManager().beginTransaction(), "Datepickerdialog");

            }
        });
        time = findViewById(R.id.timer_edittext);
        timePicker = findViewById(R.id.time_picker_imageview);
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                TimePickerDialog dpd = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

                        now.setTime(new Date(event.date));
                        now.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        now.set(Calendar.MINUTE,minute);
                        now.set(Calendar.SECOND,0);

                        event.date = now.getTime().getTime();

                        date.setText(DateFormat.simpleDate(new Date(event.date)));
                        time.setText(DateFormat.simpleTime(new Date(event.date)));
                    }

                },true);
                dpd.setAccentColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
                dpd.show(getSupportFragmentManager().beginTransaction(), "Datepickerdialog");

            }
        });
        location = findViewById(R.id.location_picker_cardview);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DialogSimpleEditText(context,getString(R.string.location),event.address,new DialogSimpleEditText.OnDialogListener(){

                    @Override
                    public void onOk(String text) {
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

        upload = findViewById(R.id.upload_edittext);
        uploadPicker = findViewById(R.id.iupload_picker_imageview);
        uploadPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalery();
            }
        });
        reminder = findViewById(R.id.reminder_edittext);
        reminderPicker = findViewById(R.id.reminder_picker_imageview);
        reminderPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar cal = Calendar.getInstance();
                                cal.set(Calendar.YEAR, year);
                                cal.set(Calendar.MONTH, monthOfYear);
                                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                event.reminder = cal.getTime().getTime();
                                reminder.setText(DateFormat.simpleDate(new Date(event.reminder)));
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setAccentColor(ContextCompat.getColor(context,R.color.colorPrimaryDark));
                dpd.show(getSupportFragmentManager().beginTransaction(), "Datepickerdialog");

            }
        });
        description = findViewById(R.id.description_edittext);
        save = findViewById(R.id.save_button);
        save.setText(getString(R.string.add_event));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEmpty()){
                    Toast.makeText(context,getString(R.string.empty_event_data),Toast.LENGTH_SHORT).show();
                    return;
                }

                event.name = name.getText().toString();
                event.description = description.getText().toString();
                event.dateString = DateFormat.ISO_8601_FORMAT.format(event.date);

                if (changeImage && uploadFile != null){
                    uploadImage(uploadFile);
                    return;
                }
                presenter.setEvent(event,true);
            }
        });

        if (intent.hasExtra("event_edit")){
            event = (EventModel) intent.getSerializableExtra("event_edit");
            title.setText(getString(R.string.title_edit_event));
            save.setText(getString(R.string.save_event));

            name.setText(event.name);
            description.setText(event.description);
            reminder.setText(DateFormat.simpleDate(new Date(event.reminder)));
            upload.setText(event.imageUrl);
            date.setText(DateFormat.simpleDate(new Date(event.date)));
            time.setText(DateFormat.simpleTime(new Date(event.date)));

        }
        if (event == null){
            event = new EventModel();
            event.id = UUID.randomUUID().toString();
        }
    }

    private boolean checkEmpty() {
        return (name.getText().toString().trim().isEmpty() ||
                date.getText().toString().trim().isEmpty() ||
                time.getText().toString().trim().isEmpty() ||
                upload.getText().toString().trim().isEmpty() ||
                reminder.getText().toString().trim().isEmpty()||
                description.getText().toString().trim().isEmpty()
        );
    }

    private void openGalery(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, PICK_IMAGE);
    }

    private void uploadImage(byte[] content){
        String name = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).format(System.currentTimeMillis()) + ".jpg";
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), content);
        MultipartBody.Part file = MultipartBody.Part.createFormData("file",name, requestFile);
        presenter.upload(file,true);
    }

    private byte[] bmpToByteArray(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public void showProgressSetEvent(Boolean show) {
        loading.setVisibility(show);
        eventEditorScrollview.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showErrorSetEvent(String error) {
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSetEvent() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void showProgressUpload(Boolean show) {
        loading.setVisibility(show);
        eventEditorScrollview.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showErrorUpload(String error) {
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUploaded(UploadResponse response) {
        event.imageUrl = BuildConfig.HOSTING_URL + response.url;
        presenter.setEvent(event,true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE){

            upload.setText(R.string.file_choosed);
            changeImage = true;

            if (data != null && data.getData() != null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), data.getData());
                    try {
                        Bitmap bmp = ImageDecoder.decodeBitmap(source);
                        uploadFile = bmpToByteArray(bmp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {

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