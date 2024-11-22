package com.example.notepadapp.bean;

import java.io.Serializable;

import lombok.Data;

@Data
public class Notepad implements Serializable {
    private int id;
    private String content;

    private String time;

}
