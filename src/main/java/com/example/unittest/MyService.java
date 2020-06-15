package com.example.unittest;

import org.springframework.stereotype.Service;

@Service
public class MyService {
  public void test(int value) {
    System.out.println("MyService.test()..." + value);
  }
}
