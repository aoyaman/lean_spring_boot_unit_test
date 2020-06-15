package com.example.unittest;

public class MyData {
  private String strData;
  private int intData;

  public MyData(String strData, int intData) {
    this.strData = strData;
    this.intData = intData;
  }

  public void setStrData(String strData) {
    this.strData = strData;
  }
  public String getStrData() {
    return this.strData;
  }

  public void setIntData(int intData) {
    this.intData = intData;
  }
  public int getIntData() {
    return this.intData;
  }

}
