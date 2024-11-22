package com.example.notepadapp.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.notepadapp.R;
import com.example.notepadapp.bean.Notepad;
import com.example.notepadapp.databinding.ActivityEditBinding;
import com.example.notepadapp.db.MyDbHelper;
import com.example.notepadapp.utils.TimeUtil;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;

import lombok.ToString;

public class EditActivity extends AppCompatActivity {

    private ActivityEditBinding binding;

    private MyDbHelper myDbHelper;

    private Notepad notepad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Serializable serializable = getIntent().getSerializableExtra("notepad");
        if(serializable == null){
            binding.titleTv.setText("添加记录");
        }else {
            binding.titleTv.setText("修改记录");
            notepad = (Notepad) serializable;
            binding.contentEt.setText(notepad.getContent() );
        }
        binding.backIv.setOnClickListener(v -> finish());

        myDbHelper = new MyDbHelper(this);

        binding.clearIv.setOnClickListener(v -> binding.contentEt.setText(""));

        binding.saveIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = binding.contentEt.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(EditActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                    return;
                }

                //判断是新增还是修改
                if (notepad == null) {
                    notepad = new Notepad();
                    notepad.setContent(content);
                    notepad.setTime(TimeUtil.getTime());
                    myDbHelper.insert(notepad);
                    Toast.makeText(EditActivity.this, "新增成功", Toast.LENGTH_SHORT).show();
                }else {
                    notepad.setContent(content);
                    notepad.setTime(TimeUtil.getTime());
                    myDbHelper.update(notepad);
                    Toast.makeText(EditActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                }

                setResult(RESULT_OK);
                finish();
            }
        });
    }
}