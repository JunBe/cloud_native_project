package com.myaccountbook.budgettracker.repository;

import com.myaccountbook.budgettracker.entity.AccountBook;
import com.myaccountbook.budgettracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountBookRepository extends JpaRepository<AccountBook, Integer> {

    List<AccountBook> findByType(AccountBook.Type type);

    //날짜 기준 오름차순 정렬
    List<AccountBook> findByTypeOrderByDateAsc(AccountBook.Type type);

    //날짜 기준 내림차순 정렬
    List<AccountBook> findByTypeOrderByDateDesc(AccountBook.Type type);

    @Query("SELECT a.category, SUM(a.amount), a.type FROM AccountBook a WHERE a.user = :user AND YEAR(a.date) = :year AND MONTH(a.date) = :month GROUP BY a.category, a.type")
    List<Object[]> findMonthlyStatistics(User user, @Param("year") int year, @Param("month") int month);

    List<AccountBook> findByUser(User user);

    void deleteByUser(User user);
}
