package com.example.jobapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class YeniBasvuruEkleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnClickListener,
RadioGroup.OnCheckedChangeListener{

    Button btnYeniBasvuruEkle;
    TextInputLayout adayAd, adaySoyad, adayEmail, adayCep, adayMezunOlduguOkul, adayAciklama;
    EditText adayDogumTarih;
    String strIngilizceSeviye;
    Spinner adayEgitimSeviyesi;
    RadioGroup adayIngilizceSeviyesi;
    RadioButton adaySeviyeSecenek;
    DatePickerDialog picker;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yeni_basvuru_ekle);

        btnYeniBasvuruEkle = findViewById(R.id.btnBasvuruEkle);
        btnYeniBasvuruEkle.setOnClickListener(this);

        adayAd = findViewById(R.id.adayAdi);
        adaySoyad = findViewById(R.id.adaySoyadi);
        adayEmail = findViewById(R.id.adayEmail);
        adayCep = findViewById(R.id.adayCep);
        adayDogumTarih = findViewById(R.id.dogumTarihi);
        adayDogumTarih.setInputType(InputType.TYPE_NULL);

        adayMezunOlduguOkul = findViewById(R.id.adayMezunOkul);
        adayAciklama = findViewById(R.id.adayAciklama);
        adayIngilizceSeviyesi = findViewById(R.id.adayIngilizceSeviye);

        adayIngilizceSeviyesi.setOnCheckedChangeListener(this);

        adayEgitimSeviyesi = findViewById(R.id.adayEgitimSeviyesi);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.egitim_seviyesi, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adayEgitimSeviyesi.setAdapter(spinnerAdapter);
        adayEgitimSeviyesi.setOnItemSelectedListener(this);

        adayDogumTarih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                // date picker
                picker = new DatePickerDialog(YeniBasvuruEkleActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                adayDogumTarih.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("adaylar");

        // Tüm değerleri getirme
        String strId = databaseReference.push().getKey();
        String strAd = adayAd.getEditText().getText().toString();
        String strSoyad = adaySoyad.getEditText().getText().toString();
        String strEmail = adayEmail.getEditText().getText().toString();
        String strCep = adayCep.getEditText().getText().toString();
        String strDogumTarihi = adayDogumTarih.getText().toString();
        String strMezunOkul = adayMezunOlduguOkul.getEditText().getText().toString();
        String strAciklama = adayAciklama.getEditText().getText().toString();

        if(TextUtils.isEmpty(strAd)){
            Toast.makeText(getApplicationContext(),"Lütfen adınızı giriniz",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(strSoyad)){
            Toast.makeText(getApplicationContext(),"Lütfen soyadınızı giriniz",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(strEmail)){
            Toast.makeText(getApplicationContext(),"Lütfen email adresinizi giriniz",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(strCep)){
            Toast.makeText(getApplicationContext(),"Lütfen cep telefonunuzu giriniz",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(strMezunOkul)){
            Toast.makeText(getApplicationContext(),"Lütfen mezun olduğunuz okulu giriniz",Toast.LENGTH_SHORT).show();
            return;
        }

        Aday aday = new Aday(strAd, strSoyad, strEmail, strCep, strDogumTarihi, strMezunOkul,
                adayEgitimSeviyesi.getSelectedItem().toString(), strIngilizceSeviye,strAciklama);

        databaseReference.child(strId).setValue(aday);

        YeniBasvuruEkleActivity.this.finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        adaySeviyeSecenek = adayIngilizceSeviyesi.findViewById(checkedId);

        switch (checkedId){
            case R.id.seviyeIyi:
                strIngilizceSeviye = adaySeviyeSecenek.getText().toString();
                break;
            case R.id.seviyeOrta:
                strIngilizceSeviye = adaySeviyeSecenek.getText().toString();
                break;
            case R.id.seviyeKotu:
                strIngilizceSeviye = adaySeviyeSecenek.getText().toString();
                break;
            default:
        }
    }
}
