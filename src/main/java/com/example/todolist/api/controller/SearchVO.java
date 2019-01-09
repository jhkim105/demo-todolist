package com.example.todolist.api.controller;

import lombok.Data;

import java.io.Serializable;

@Data
public class SearchVO implements Serializable {

  private static final long serialVersionUID = 2992264477897364367L;

  private String q;

  private boolean open;

}
