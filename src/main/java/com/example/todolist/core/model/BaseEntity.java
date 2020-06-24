package com.example.todolist.core.model;

import java.io.Serializable;

public abstract class BaseEntity<K extends Serializable> implements Serializable {

  private static final long serialVersionUID = 5534003885658026617L;

  public static final int ID_LENGTH = 50;

  public abstract String toString();

  public abstract K getId();

}
