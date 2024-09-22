package com.example.praasessment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.praasessment.adapter.Adapter;
import com.example.praasessment.helper.Database;
import com.example.praasessment.model.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    FloatingActionButton btAdd;
    AlertDialog.Builder dialog;
    List<Data> lists = new ArrayList<>();
    Adapter adapter;
    Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        db = new Database(getApplicationContext());
        btAdd = findViewById(R.id.btAdd);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        listView = findViewById(R.id.listView);
        adapter = new Adapter(MainActivity.this, lists);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                final String id = lists.get(position).getId();
                final String nim  = lists.get(position).getNim();
                final String nama = lists.get(position).getNama();
                final String ttl = lists.get(position).getTtl();
                final String jenis_kelamin = lists.get(position).getJenis_kelamin();
                final String alamat = lists.get(position).getAlamat();
                final CharSequence[] dialogItem = {"Lihat Data", "Edit Data", "Hapus Data"};
                dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intentRead = new Intent(MainActivity.this, ReadActivity.class);
                                intentRead.putExtra("id", id);
                                intentRead.putExtra("nim", nim);
                                intentRead.putExtra("nama", nama);
                                intentRead.putExtra("ttl", ttl);
                                intentRead.putExtra("jenis_kelamin", jenis_kelamin);
                                intentRead.putExtra("alamat", alamat);
                                startActivity(intentRead);
                                break;

                            case 1:
                                Intent intentUpdate = new Intent(MainActivity.this, EditorActivity.class);
                                intentUpdate.putExtra("id", id);
                                intentUpdate.putExtra("nim", nim);
                                intentUpdate.putExtra("nama", nama);
                                intentUpdate.putExtra("ttl", ttl);
                                intentUpdate.putExtra("jenis_kelamin", jenis_kelamin);
                                intentUpdate.putExtra("alamat", alamat);
                                startActivity(intentUpdate);
                                break;

                            case 2:
                                db.delete(Integer.parseInt(id));
                                lists.clear();
                                getData();
                                break;
                        }
                    }
                }).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getData();
    }

    private void getData() {
        ArrayList<HashMap<String, String>> rows = db.getAll();
        for(int i = 0; i < rows.size(); i++) {
            String id = rows.get(i).get("id");
            String nim = rows.get(i).get("nim");
            String nama = rows.get(i).get("nama");
            String ttl = rows.get(i).get("ttl");
            String jenis_kelamin = rows.get(i).get("jenis Kelamin");
            String alamat = rows.get(i).get("alamat");

            Data data = new Data();
            data.setId(id);
            data.setNim(nim);
            data.setNama(nama);
            data.setTtl(ttl);
            data.setJenis_kelamin(jenis_kelamin);
            data.setAlamat(alamat);
            lists.add(data);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        lists.clear();
        getData();
    }
}