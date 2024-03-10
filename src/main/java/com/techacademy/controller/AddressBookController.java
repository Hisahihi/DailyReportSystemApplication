package com.techacademy.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.entity.AddressBook;
import com.techacademy.entity.Employee;
import com.techacademy.service.AddressBookService;
import com.techacademy.service.UserDetail;

@Controller
@RequestMapping("addressBooks")
public class AddressBookController {

    private final AddressBookService addressBookService;

    @Autowired
    public AddressBookController(AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }

    //住所録一覧画面
    @GetMapping
    public String list(Model model,@AuthenticationPrincipal UserDetail userDetail,AddressBook addressBook) {
        //userDetail
        model.addAttribute("addressBookList",addressBookService.findAll());
        model.addAttribute("listSize", addressBookService.findAll().size());
        return "addressBooks/list";
    }

    //住所録詳細画面
    @GetMapping(value= "/{id}/")
    public String detail(Model model,@PathVariable Integer id) {

        model.addAttribute("addressBook",addressBookService.findById(id));
        return "addressBooks/detail";
    }

    //住所録登録画面
    @GetMapping("/add")
    public String create(@ModelAttribute AddressBook addressBook,@AuthenticationPrincipal UserDetail userDetail,Model model) {
        //usreDetailに入っているemployeeの値を取り出してaddressBookにセットする
        Employee employee = userDetail.getEmployee();
        addressBook.setEmployee(employee);
        model.addAttribute("addressBook",addressBook);
        return "addressBooks/new";
    }

    //住所録登録処理
    @PostMapping("/add")
    public String add(@AuthenticationPrincipal UserDetail userDetail,AddressBook addressBook) {
        Employee employee = userDetail.getEmployee();
        addressBook.setEmployee(employee);
        addressBookService.save(addressBook);
        return "redirect:/addressBooks";
    }

    //住所録更新画面
    @GetMapping("/{id}/update")
    public String edit(@PathVariable Integer id, Model model, @ModelAttribute AddressBook addressBook,@AuthenticationPrincipal UserDetail userDetail) {

        model.addAttribute("addressBook",addressBookService.findById(id));
        return "addressBooks/update";
    }

    //住所録更新処理
    @PostMapping("/{id}/update")
    public String update(@PathVariable Integer id, Model model, @ModelAttribute AddressBook addressBook,@AuthenticationPrincipal UserDetail userDetail) {

        return "redirect:/addressBooks";
    }

}
