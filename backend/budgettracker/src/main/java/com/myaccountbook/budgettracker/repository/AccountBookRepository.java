package com.myaccountbook.budgettracker.repository;

import com.myaccountbook.budgettracker.entity.AccountBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountBookRepository extends JpaRepository<AccountBook, Integer> {

}
