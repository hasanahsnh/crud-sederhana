package com.example.praasessment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.praasessment.helper.Database;
import com.example.praasessment.model.Data;

import java.util.ArrayList;
import java.util.HashMap;

public class ReadActivity extends AppCompatActivity {

    TextView tvNim2, tvNama2, tvTtl, tvJenisKelamin2, tvAlamat2;
    Database db = new Database(this);
    String id, nim, nama, ttl, jenisKelamin, alamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_read);

        tvNim2 = findViewById(R.id.tvNim2);
        tvNama2 = findViewById(R.id.tvNama2);
        tvTtl = findViewById(R.id.tvTtl);
        tvJenisKelamin2 = findViewById(R.id.tvJenisKelamin2);
        tvAlamat2 = findViewById(R.id.tvAlamat2);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        // Ambil data berdasarkan id yang diterima
        getDataById(id);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void getDataById(String id) {
        HashMap<String, String> rowData = db.getDataById(id); // Misal method getDataById ada di kelas Database

        if (rowData != null) {
            // Set data ke TextView
            if (rowData.get("nim") != null) {
                tvNim2.setText(rowData.get("nim"));
            }
            if (rowData.get("nama") != null) {
                tvNama2.setText(rowData.get("nama"));
            }
            if (rowData.get("ttl") != null) {
                tvTtl.setText(rowData.get("ttl"));
            }
            if (rowData.get("jenis_kelamin") != null) {
                tvJenisKelamin2.setText(rowData.get("jenis_kelamin"));
            }
            if (rowData.get("alamat") != null) {
                tvAlamat2.setText(rowData.get("alamat"));
            }
        }
    }
}