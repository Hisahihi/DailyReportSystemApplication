package com.techacademy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techacademy.entity.AddressBook;
import com.techacademy.entity.Employee;

public interface AddressBookRepository extends JpaRepository<AddressBook,Integer>{
    //extendsでJpaRepositoryを拡張してCRUD操作できるようにする
    List<AddressBook> findByEmployee(Employee employee);

}
