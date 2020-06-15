package com.example.unittest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@EnableAutoConfiguration
public class HomeController {

  @Autowired
  MyService myService;


  @GetMapping(path = "/")
  String home(HttpServletRequest request, Model model) {

    // string
    model.addAttribute("test", "this is test");

    // HashMap<String, String>
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("name", "momotaro");
    map.put("age", "23");
    model.addAttribute("map", map);

    // List<String>
    List<String> list = new ArrayList<String>();
    list.add("list1");
    list.add("list2");
    list.add("list3");
    model.addAttribute("list", list);

    // List<MyData>
    List<MyData> list2 = new ArrayList<MyData>();
    list2.add(new MyData("test1", 111));
    list2.add(new MyData("test2", 222));
    model.addAttribute("list2", list2);

    // home.htmlを指定する
    return "home";
  }

  @GetMapping(path = "/testget")
  String testget(@RequestParam(value = "value", required = false, defaultValue = "0") Integer value, @ModelAttribute("error") String error, Model model) {

    model.addAttribute("strError", error);
    model.addAttribute("intValue", value);

    // home.htmlを指定する
    return "testget";
  }


  @PostMapping(path = "/testpost")
  public ModelAndView testpost(RedirectAttributes redirectAttributes,  @RequestParam(value = "intvalue", required = false, defaultValue = "0") Integer intvalue) {
    ModelAndView modelAndView = new ModelAndView("redirect:/testget");

    // 入力値のチェック
    if (intvalue <= 0) {
      redirectAttributes.addFlashAttribute("error", "intvalueは0より大きい値でなければいけません");
      return modelAndView;
    }
    // データをセット
    modelAndView.addObject("value", intvalue);
    return modelAndView;
  }

  @PostMapping(path = "/testpost2")
  public String testpost2(@RequestParam(value = "intvalue", required = false, defaultValue = "0") Integer intvalue) {

    // MyServiceのtest()をコール
    myService.test(intvalue);

    return "redirect:/";
  }
}
