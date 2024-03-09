package com.techacademy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techacademy.entity.AddressBook;
import com.techacademy.repository.AddressBookRepository;

@Service
public class AddressBookService {
    private final AddressBookRepository addressBookRepository;

    @Autowired
    public AddressBookService(AddressBookRepository addressBookRepository) {
        this.addressBookRepository = addressBookRepository;
    }

    //住所録保存
    @Transactional
    public AddressBook save(AddressBook addressBook) {
        return addressBookRepository.save(addressBook);
    }
    //住所録更新
    //住所録削除
    //住所録の一覧表示
    public List<AddressBook>findAll(){
        return addressBookRepository.findAll();
    }

    //従業員の情報から住所録を検索
    //1件を検索

}
