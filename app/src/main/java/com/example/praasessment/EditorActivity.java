package com.example.praasessment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterViewAnimator;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.praasessment.helper.Database;

import java.util.Calendar;
import java.util.HashMap;

public class EditorActivity extends AppCompatActivity {

    EditText etNim, etNama, etAlamat;
    TextView tvTtl;
    Button btSimpan, btTtl;
    AutoCompleteTextView acJenisKelamin;
    Database db = new Database(this);
    String id, nim, nama, ttl, jenisKelamin, alamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editor);

        etNim = findViewById(R.id.etNim);
        etNama = findViewById(R.id.etNama);
        etAlamat = findViewById(R.id.etAlamat);
        btSimpan = findViewById(R.id.btSimpan);
        btTtl = findViewById(R.id.btTtl);
        acJenisKelamin = findViewById(R.id.acJenisKelamin);
        tvTtl = findViewById(R.id.tvTtl);

        String[] jenisKelaminList = {"Laki-laki", "Perempuan"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, jenisKelaminList);
        acJenisKelamin.setAdapter(adapter);

        acJenisKelamin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }
        });

        btTtl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        if (id != null) {
            getDataById(id);
        }

        btSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(id == null || id.equals("")) {
                        save();
                    } else {
                        edit();
                    }
                } catch (Exception e) {
                    Log.e("Saving", e.getMessage());
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void save() {
        nim = etNim.getText().toString();
        nama = etNama.getText().toString();
        ttl = tvTtl.getText().toString();
        jenisKelamin = acJenisKelamin.getText().toString();
        alamat = etAlamat.getText().toString();

        if (nim.isEmpty() || nama.isEmpty() || ttl.isEmpty() || jenisKelamin.isEmpty() || alamat.isEmpty()) {
            Toast.makeText(this, "Isi semua kolom", Toast.LENGTH_SHORT).show();
        } else {
            boolean isInserted = db.insert(nim, nama, ttl, jenisKelamin, alamat);

            if (isInserted) {
                Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                returnToMainActivity();
            } else {
                Toast.makeText(this, "NIM telah terdaftar", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void edit() {
        nim = etNim.getText().toString();
        nama = etNama.getText().toString();
        ttl = tvTtl.getText().toString();
        jenisKelamin = acJenisKelamin.getText().toString();
        alamat = etAlamat.getText().toString();

        // Check if any field is empty
        if (nim.isEmpty() || nama.isEmpty() || ttl.isEmpty() || jenisKelamin.isEmpty() || alamat.isEmpty()) {
            Toast.makeText(this, "Isi semua kolom", Toast.LENGTH_SHORT).show();
        } else {
            boolean isUpdated = db.update(Integer.parseInt(id), nim, nama, ttl, jenisKelamin, alamat);

            if (isUpdated) {
                Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Data gagal disimpan", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void returnToMainActivity() {
        Intent intent = new Intent(EditorActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Untuk menghapus semua activity di atas
        startActivity(intent);
        finish();
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        tvTtl.setText(selectedDate);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void getDataById(String id) {
        HashMap<String, String> rowData = db.getDataById(id);

        if (rowData != null) {
            // Set data ke TextView
            if (rowData.get("nim") != null) {
                etNim.setText(rowData.get("nim"));
            }
            if (rowData.get("nama") != null) {
                etNama.setText(rowData.get("nama"));
            }
            if (rowData.get("ttl") != null) {
                tvTtl.setText(rowData.get("ttl"));
            }
            if (rowData.get("jenis_kelamin") != null) {
                String jenisKelamin = rowData.get("jenis_kelamin");
                acJenisKelamin.setText(jenisKelamin, false);
            }
            if (rowData.get("alamat") != null) {
                etAlamat.setText(rowData.get("alamat"));
            }
        }
    }
}