package com.example.notepadapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.notepadapp.adaper.NotepadListAdapter;
import com.example.notepadapp.bean.Notepad;
import com.example.notepadapp.databinding.ActivityMainBinding;
import com.example.notepadapp.db.MyDbHelper;
import com.example.notepadapp.ui.EditActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    NotepadListAdapter notepadListAdapter;
    public MyDbHelper myDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myDbHelper = new MyDbHelper(this);
        binding.addIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, EditActivity.class));
                startActivityForResult(new Intent(MainActivity.this, EditActivity.class),100);
            }
        });


        notepadListAdapter = new NotepadListAdapter();
        binding.listRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.listRv.setAdapter(notepadListAdapter);

        binding.listRv.setLayoutManager(new LinearLayoutManager(this));
        findAll();
    }

    public void findAll() {
        List<Notepad> nodepadlist = myDbHelper.findAll();
        notepadListAdapter.update(nodepadlist);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findAll();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK) {
            findAll();
        }
    }
}