package com.myaccountbook.budgettracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BudgettrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BudgettrackerApplication.class, args);
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("MySQL 드라이버 로드 성공"); //성공
		} catch (ClassNotFoundException e) {
			System.out.println("MySQL 드라이버 로드 실패");
			e.printStackTrace();
		}
	}

}
