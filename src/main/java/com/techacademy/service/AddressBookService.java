package com.techacademy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techacademy.constants.ErrorKinds;
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
    public ErrorKinds save(AddressBook addressBook) {
        LocalDateTime now = LocalDateTime.now();
        addressBook.setCreatedAt(now);
        addressBook.setUpdatedAt(now);

        addressBookRepository.save(addressBook);
        return ErrorKinds.SUCCESS;
    }
    //住所録更新
    @Transactional
    public ErrorKinds update(AddressBook addressBook) {
        AddressBook info =findById(addressBook.getCode());
        addressBook.setDeleteFlg(false);
        LocalDateTime now = LocalDateTime.now();

        addressBook.setCreatedAt(info.getCreatedAt());
        addressBook.setUpdatedAt(now);

        addressBookRepository.save(addressBook);
        return ErrorKinds.SUCCESS;
    }
    //住所録削除
    //住所録の一覧表示
    public List<AddressBook>findAll(){
        return addressBookRepository.findAll();
    }

    //従業員の情報から住所録を検索
    //データベースから1件を検索する
    public AddressBook findById(Integer code) {
        Optional<AddressBook> option = addressBookRepository.findById(code);
        AddressBook addressBook = option.orElse(null);
        return addressBook;
    }

}
