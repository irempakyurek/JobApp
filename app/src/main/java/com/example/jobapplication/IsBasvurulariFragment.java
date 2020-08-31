package com.example.jobapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class IsBasvurulariFragment extends Fragment implements View.OnClickListener, AdayIslemleri,
        AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener{

    FloatingActionButton isBasvurusuEkle;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<Aday, AdayViewHolder> adayAdapter;

    Button btnBasvuruGuncelle;
    TextInputLayout adayAd, adaySoyad, adayEmail, adayCep, adayMezunOlduguOkul, adayAciklama;
    EditText adayDogumTarihi;
    DatePickerDialog picker;

    String strIngilizceSeviye;
    Spinner adayEgitimSeviyesi;
    RadioGroup adayIngilizceSeviyesi;
    RadioButton adaySeviyeSecenek;

    public IsBasvurulariFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_is_basvurulari, container, false);
        isBasvurusuEkle = v.findViewById(R.id.fab);

        isBasvurusuEkle.setOnClickListener(this);

        //Firebase init
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("adaylar");

        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        dataYukle();

        return v;
    }

    private void dataYukle()
    {
        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Aday>()
                        .setQuery(databaseReference,Aday.class)
                        .build();

        adayAdapter = new FirebaseRecyclerAdapter<Aday, AdayViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final AdayViewHolder holder, int position, @NonNull Aday model) {

                holder.adayAdi.setText(model.getAd());
                holder.adaySoyadi.setText(model.getSoyad());
                holder.adayCepTel.setText(model.getCepTel());
                holder.adayMezunOkul.setText(model.getOkul());

                DatabaseReference itemRef = getRef(position);
                final String key = itemRef.getKey();

                holder.optionsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //guncelle ve sil optionlarını gormek icin popup menu ekleme
                        PopupMenu popup = new PopupMenu(getContext(), holder.optionsButton);
                        popup.inflate(R.menu.aday_option);
                        //click listener ekleme
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.menu1:
                                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String ad = snapshot.child(key).child("ad").getValue().toString();
                                                adayAd.getEditText().setText(ad);

                                                String soyad = snapshot.child(key).child("soyad").getValue().toString();
                                                adaySoyad.getEditText().setText(soyad);

                                                String eMail = snapshot.child(key).child("email").getValue().toString();
                                                adayEmail.getEditText().setText(eMail);

                                                String cepTel = snapshot.child(key).child("cepTel").getValue().toString();
                                                adayCep.getEditText().setText(cepTel);

                                                String dogumTarihi = snapshot.child(key).child("dogumTarihi").getValue().toString();
                                                adayDogumTarihi.setText(dogumTarihi);

                                                String mezunOkul = snapshot.child(key).child("okul").getValue().toString();
                                                adayMezunOlduguOkul.getEditText().setText(mezunOkul);

                                                String aciklama = snapshot.child(key).child("aciklama").getValue().toString();
                                                adayAciklama.getEditText().setText(aciklama);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        showUpdateDialog(key);
                                        break;
                                    case R.id.menu2:
                                        adaySil(key);
                                        break;
                                    default:
                                }
                                return false;
                            }

                        });

                        popup.show();

                    }
                });

            }

            @NonNull
            @Override
            public AdayViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.liste_aday,viewGroup,false);
                return new AdayViewHolder(view);
            }
        };
        adayAdapter.notifyDataSetChanged();
        adayAdapter.startListening();
        recyclerView.setAdapter(adayAdapter);
    }
    @Override
    public void onClick(View v) {
        startActivity(new Intent(getActivity(),YeniBasvuruEkleActivity.class));
    }

    private void showUpdateDialog(final String adayId) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.basvuru_guncelle_dialog, null);
        dialogBuilder.setView(dialogView);

        adayAd = dialogView.findViewById(R.id.adayAdi);
        adaySoyad = dialogView.findViewById(R.id.adaySoyadi);
        adayEmail = dialogView.findViewById(R.id.adayEmail);
        adayCep = dialogView.findViewById(R.id.adayCep);
        adayDogumTarihi = dialogView.findViewById(R.id.dogumTarihi);
        adayDogumTarihi.setInputType(InputType.TYPE_NULL);

        adayMezunOlduguOkul = dialogView.findViewById(R.id.adayMezunOkul);
        adayAciklama = dialogView.findViewById(R.id.adayAciklama);
        adayIngilizceSeviyesi = dialogView.findViewById(R.id.adayIngilizceSeviye);
        btnBasvuruGuncelle = dialogView.findViewById(R.id.btnBasvuruGuncelle);

        adayIngilizceSeviyesi.setOnCheckedChangeListener(this);

        adayDogumTarihi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                // date picker
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                adayDogumTarihi.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        adayEgitimSeviyesi = dialogView.findViewById(R.id.adayEgitimSeviyesi);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.egitim_seviyesi, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adayEgitimSeviyesi.setAdapter(spinnerAdapter);
        adayEgitimSeviyesi.setOnItemSelectedListener(this);

        final AlertDialog b = dialogBuilder.create();
        b.show();


        btnBasvuruGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strAd = adayAd.getEditText().getText().toString();
                String strSoyad = adaySoyad.getEditText().getText().toString();
                String strEmail = adayEmail.getEditText().getText().toString();
                String strCep = adayCep.getEditText().getText().toString();
                String strDogumTarihi = adayDogumTarihi.getText().toString();
                String strMezunOkul = adayMezunOlduguOkul.getEditText().getText().toString();
                String strAciklama = adayAciklama.getEditText().getText().toString();

                if(TextUtils.isEmpty(strAd)){
                    Toast.makeText(getContext(),"Lütfen adınızı giriniz",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(strSoyad)){
                    Toast.makeText(getContext(),"Lütfen soyadınızı giriniz",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(strEmail)){
                    Toast.makeText(getContext(),"Lütfen email adresinizi giriniz",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(strCep)){
                    Toast.makeText(getContext(),"Lütfen cep telefonunuzu giriniz",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(strMezunOkul)){
                    Toast.makeText(getContext(),"Lütfen mezun olduğunuz okulu giriniz",Toast.LENGTH_SHORT).show();
                    return;
                }

                    adayGuncelle(adayId, strAd, strSoyad, strEmail, strCep, strDogumTarihi, strMezunOkul,
                            adayEgitimSeviyesi.getSelectedItem().toString(), strIngilizceSeviye,strAciklama);
                    b.dismiss();

            }
        });

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
    @Override
    public void adayGuncelle(String strId, String strAd, String strSoyad, String strEmail, String strCep, String strDogumTarihi,
                             String strMezunOkul, String adayEgitimSeviyesi, String strIngilizceSeviye,  String strAciklama) {

        DatabaseReference dbGuncelle = database.getReference().child("adaylar");

        Aday aday = new Aday(strAd, strSoyad, strEmail, strCep, strDogumTarihi, strMezunOkul,
                adayEgitimSeviyesi, strIngilizceSeviye, strAciklama);
        dbGuncelle.child(strId).setValue(aday);
    }

    @Override
    public void adaySil(String strId) {
        DatabaseReference dbSil = database.getReference().child("adaylar");
        dbSil.child(strId).removeValue();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
