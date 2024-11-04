package com.myaccountbook.budgettracker.repository;

import com.myaccountbook.budgettracker.entity.AccountBook;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountBookRepositoryTest {

    @Autowired
    private AccountBookRepository accountBookRepository;

    @Test
    public void testSaveAndFind() {
        //새로운 AccountBook 엔터티 생성
        AccountBook account = new AccountBook();
        account.setDate(new Date());
        account.setAmount(BigDecimal.valueOf(10000));
        account.setCategory("테스트 카테고리");
        account.setDescription("테스트 항목");

        //저장
        accountBookRepository.save(account);

        //저장된 데이터 가져오기
        AccountBook found = accountBookRepository.findById(account.getId()).orElse(null);

        //데이터가 제대로 저장되었는지 확인
        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(found.getAmount()).isEqualTo(BigDecimal.valueOf(10000));
        Assertions.assertThat(found.getCategory()).isEqualTo("테스트 카테고리");

    }

}