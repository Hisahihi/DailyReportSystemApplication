package com.techacademy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techacademy.entity.AddressBook;

public interface AddressBookRepository extends JpaRepository<AddressBook,Integer>{//extendsでJpaRepositoryを拡張してCRUD操作できるようにする

}
