package com.example.unittest;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = HomeController.class)
@AutoConfigureMockMvc
public class HomeControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private MyService myService;

  @Test
  void home画面() throws Exception {

    HashMap<String, String> map = new HashMap<String, String>();
    map.put("name", "momotaro");
    map.put("age", "23");

    List<String> list = new ArrayList<String>();
    list.add("list1");
    list.add("list2");
    list.add("list3");

    List<MyData> list2 = new ArrayList<MyData>();
    list2.add(new MyData("test1", 111));
    list2.add(new MyData("test2", 222));


    // テスト
    this.mockMvc.perform(get("/")).andDo(print())
        // ステータス「200 OK」を返すかどうか
        .andExpect(status().isOk())
        .andExpect(status().is(200))

        // テンプレート「home」を返すかどうか
        .andExpect(view().name("home"))

        // Modelのテスト。

        // string型
        .andExpect(model().attribute("test", "this is test"))

        // HashMap型
        .andExpect(model().attribute("map", map))

        // List<String>型
        .andExpect(model().attribute("list", hasSize(3)))   // リストのサイズ
        .andExpect(model().attribute("list", hasItem("list1")))   // list1が含まれているか
        .andExpect(model().attribute("list", hasItem("list2")))   // list2が含まれているか
        .andExpect(model().attribute("list", hasItem("list3")))   // list3が含まれているか
        .andExpect(model().attribute("list", contains("list1", "list2", "list3")))  // list1, list2, list3の順で含まれているか
        .andExpect(model().attribute("list", is(list)))  // listと一致しているかどうか

        // List<MyData>型
        .andExpect(model().attribute("list2", hasSize(2)))   // リストのサイズ
        .andExpect(model().attribute("list2",
          hasItem(allOf(hasProperty("strData", is("test1")), hasProperty("intData", is(111))))
        ))   // この組み合わせのデータが含まれているか
        .andExpect(model().attribute("list2",
          hasItem(allOf(hasProperty("strData", is("test2")), hasProperty("intData", is(222))))
        ))   // この組み合わせのデータが含まれているか
        //.andExpect(model().attribute("list2", is(list2)))  // list2と一致しているかどうか -> この書き方は出来ない。エラーになる。

        ;
  }

  @Test
  void testget() throws Exception {
    this.mockMvc.perform(get("/testget?value=5&error=SuperError")).andDo(print())
        // ステータス「200 OK」を返すかどうか
        .andExpect(status().isOk())

        // テンプレート「testget」を返すかどうか
        .andExpect(view().name("testget"))

        // Modelのテスト。
        .andExpect(model().attribute("intValue", 5))
        .andExpect(model().attribute("strError", "SuperError"))
        ;
  }


  @Test
  void testpost() throws Exception {
    // パラメータを渡さなかったらチェックに引っかかる
    this.mockMvc.perform(post("/testpost")).andExpect(redirectedUrl("/testget"))
        .andExpect(flash().attribute("error", "intvalueは0より大きい値でなければいけません"));

    // パラメータを渡したらチェックを通る
    this.mockMvc.perform(post("/testpost").param("intvalue", "5"))
        .andExpect(redirectedUrl("/testget?value=5"));
  }


  @Test
  void testpos2() throws Exception {
    // redirectUrlチェック
    this.mockMvc.perform(post("/testpost2").param("intvalue", "5")).andExpect(redirectedUrl("/"));

    // MyServiceのtest()メソッドが引数の値5でコールされたかどうかをテスト
    verify(this.myService, times(1)).test(5);
  }
}
