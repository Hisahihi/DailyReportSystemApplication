package com.techacademy.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.AddressBook;
import com.techacademy.entity.Employee;
import com.techacademy.service.AddressBookService;
import com.techacademy.service.EmployeeService;
import com.techacademy.service.UserDetail;

@Controller

public class AddressBookController {

    private final AddressBookService addressBookService;
    private final EmployeeService employeeService;

    @Autowired
    public AddressBookController(AddressBookService addressBookService,EmployeeService employeeService) {
        this.addressBookService = addressBookService;
        this.employeeService = employeeService;
    }

    //住所録一覧画面
    @GetMapping(value ="/employees/{code}/addressBooks")
    public String list(@PathVariable String code, Model model) {
        Employee employee = employeeService.findByCode(code);
        List<AddressBook> addressBookList = addressBookService.findByEmployee(employee);

        //userDetail
        model.addAttribute("addressBookList",addressBookList);
        model.addAttribute("listSize",addressBookList.size());
        return "addressBooks/list";
    }

    //住所録詳細画面
    @GetMapping(value= "/addressBooks/{id}/")
    public String detail(Model model,@PathVariable Integer id) {

        model.addAttribute("addressBook",addressBookService.findById(id));
        return "addressBooks/detail";
    }

    //住所録登録画面
    @GetMapping("/addressBooks/add")
    public String create(@ModelAttribute AddressBook addressBook,@AuthenticationPrincipal UserDetail userDetail,Model model) {
        //usreDetailに入っているemployeeの値を取り出してaddressBookにセットする
        Employee employee = userDetail.getEmployee();
        addressBook.setEmployee(employee);
        model.addAttribute("addressBook",addressBook);
        return "addressBooks/new";
    }

    //住所録登録処理
    @PostMapping("/addressBooks/add")
    public String add(@AuthenticationPrincipal UserDetail userDetail,AddressBook addressBook) {
        Employee employee = userDetail.getEmployee();
        addressBook.setEmployee(employee);
        addressBookService.save(addressBook);
        return "redirect:/addressBooks";
    }

    //住所録更新画面
    @GetMapping("/addressBooks/{id}/update")
    public String edit(@PathVariable Integer id, Model model, @ModelAttribute AddressBook addressBook,@AuthenticationPrincipal UserDetail userDetail) {

        model.addAttribute("addressBook",addressBookService.findById(id));
        return "addressBooks/update";
    }

    //住所録更新処理
    @PostMapping("/addressBooks/{id}/update")
    public String update(@PathVariable Integer id, Model model, @ModelAttribute AddressBook addressBook,@AuthenticationPrincipal UserDetail userDetail) {
        addressBook.setId(id);

        addressBookService.update(addressBook);

        return "redirect:/addressBooks";
    }

    //住所録削除処理
    @PostMapping(value="/addressBooks/{id}/delete")
    public String delete(@PathVariable Integer id,@AuthenticationPrincipal UserDetail userDetail,Model model) {
        ErrorKinds result =addressBookService.delete(id,userDetail);

        return "redirect:/addressBooks";
    }

}
