package com.techacademy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.AddressBook;
import com.techacademy.entity.Employee;
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

        AddressBook info =findById(addressBook.getId());
        addressBook.setEmployee(info.getEmployee());


        addressBook.setDeleteFlg(info.isDeleteFlg());
        LocalDateTime now = LocalDateTime.now();

        addressBook.setCreatedAt(info.getCreatedAt());
        addressBook.setUpdatedAt(now);

        addressBookRepository.save(addressBook);
        return ErrorKinds.SUCCESS;
    }
    //住所録削除
    @Transactional
    public ErrorKinds delete(Integer id, UserDetail userDetail) {
        // 自分を削除しようとした場合はエラーメッセージを表示
        if (id.equals(userDetail.getEmployee().getCode())) {
            return ErrorKinds.LOGINCHECK_ERROR;
        }
        AddressBook addressBook = findById(id);
        LocalDateTime now = LocalDateTime.now();
        addressBook.setUpdatedAt(now);
        addressBook.setDeleteFlg(true);

        return ErrorKinds.SUCCESS;
    }

    //住所録の一覧表示
    public List<AddressBook> findAll(){
        return addressBookRepository.findAll();
    }

    public List<AddressBook> findByEmployee(Employee employee){
        return addressBookRepository.findByEmployee(employee);
    }

    //従業員の情報から住所録を検索
    //データベースから1件を検索する
    public AddressBook findById(Integer code) {
        Optional<AddressBook> option = addressBookRepository.findById(code);
        AddressBook addressBook = option.orElse(null);
        return addressBook;
    }

}
