package com.example.authapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.authapp.Adapter.AdapterItem;
import com.example.authapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ShowBookingActivity extends AppCompatActivity {
    EditText input_minimal,
            input_maximal;
    Button btn_minimal,
            btn_maximal,
            cari,btn_delete;
    ArrayList<dataUser> list = new ArrayList<>();
    AdapterItem adapterItem;
    RecyclerView recyclerView;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    DatabaseReference rootDatabaseref = FirebaseDatabase.getInstance().getReference();
    AlertDialog builderAlert;
    Context context;
    LayoutInflater layoutInflater;
    View showInput;
    Calendar calendar = Calendar.getInstance();
    Locale id = new Locale("in", "ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy", id);
    Date date_minimal;
    Date date_maximal;



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_booking);

        context = this;
        cari = findViewById(R.id.cari);
        input_minimal = findViewById(R.id.input_minimal);
        input_maximal = findViewById(R.id.input_maximal);
        btn_minimal = findViewById(R.id.btn_minimal);
        btn_maximal = findViewById(R.id.btn_maximal);
        recyclerView = findViewById(R.id.recyclerView);
        btn_delete = findViewById(R.id.btn_delete);

        btn_minimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        input_minimal.setText(simpleDateFormat.format(calendar.getTime()));
                        date_minimal = calendar.getTime();

                        String input1 = input_minimal.getText().toString();
                        String input2 = input_maximal.getText().toString();
                        if (input1.isEmpty() && input2.isEmpty()){
                            cari.setEnabled(false);
                        }else {
                            cari.setEnabled(true);
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        btn_maximal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        input_maximal.setText(simpleDateFormat.format(calendar.getTime()));
                        date_maximal = calendar.getTime();

                        String input1 = input_maximal.getText().toString();
                        String input2 = input_minimal.getText().toString();
                        if (input1.isEmpty() && input2.isEmpty()){
                            cari.setEnabled(false);
                        }else {
                            cari.setEnabled(true);
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });



        cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = database.child("booking").orderByChild("date").startAt(date_minimal.getTime()).endAt(date_maximal.getTime());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        showLisener(snapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });



        showData();
    }





    EditText et_nama, tgl_daftar,
            et_jurusan;
    Button btnDateDaftar,
            simpanData;
    RadioGroup rb_group,rb_group2;
    RadioButton radioButton,radioButton2;
    Date tgl_daftar_date;


    public void funcDelete(View view){
        Button b= (Button) view;
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Isn't deleted", Toast.LENGTH_SHORT).show();
                rootDatabaseref.child("booking").child("date").removeValue();
            }
        });
    }

    private void inputData() {
        builderAlert = new AlertDialog.Builder(context).create();
        layoutInflater = getLayoutInflater();
        showInput = layoutInflater.inflate(R.layout.input_layout, null);
        builderAlert.setView(showInput);

        et_nama = showInput.findViewById(R.id.et_name);
        tgl_daftar = showInput.findViewById(R.id.tgl_date);
        et_jurusan = showInput.findViewById(R.id.et_message);
        btnDateDaftar = showInput.findViewById(R.id.btnDateDaftar);
        simpanData = showInput.findViewById(R.id.booknow_btn);
        rb_group = showInput.findViewById(R.id.rb_group);
        rb_group2 = showInput.findViewById(R.id.rb_group2);
        builderAlert.show();

        simpanData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = et_nama.getText().toString();
                String jurusan = et_jurusan.getText().toString();
                String tgl = tgl_daftar.getText().toString();
                if (nama.isEmpty()) {
                    et_nama.setError("Data tidak boleh kosong");
                    et_nama.requestFocus();
                } else if (jurusan.isEmpty()) {
                    et_jurusan.setError("Data tidak boleh kosong");
                    et_jurusan.requestFocus();
                } else if (tgl.isEmpty()) {
                    tgl_daftar.setError("Data tidak boleh kosong");
                    tgl_daftar.requestFocus();
                } else {
                    int selected = rb_group.getCheckedRadioButtonId();
                    radioButton = showInput.findViewById(selected);
                    int selected2 = rb_group2.getCheckedRadioButtonId();
                    radioButton2 = showInput.findViewById(selected2);

                    database.child("booking").child(nama).setValue(new dataUser(
                            nama,
                            radioButton.getText().toString(),
                            jurusan,
                            tgl_daftar_date.getTime(),
                            radioButton2.getText().toString()
                    )).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                            builderAlert.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            builderAlert.dismiss();
                        }
                    });

                }
            }
        });

        btnDateDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        tgl_daftar.setText(simpleDateFormat.format(calendar.getTime()));
                        tgl_daftar_date = calendar.getTime();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }

        });

    }

    private void showData() {
        database.child("booking").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                showLisener(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showLisener(DataSnapshot snapshot) {
        list.clear();
        for (DataSnapshot item : snapshot.getChildren()) {
            dataUser user = item.getValue(dataUser.class);
            list.add(user);
        }
        adapterItem = new AdapterItem(context, list);
        recyclerView.setAdapter(adapterItem);
    }

}