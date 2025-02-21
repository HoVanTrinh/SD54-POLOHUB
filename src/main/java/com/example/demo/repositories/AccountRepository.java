package com.example.demo.repositories;

import com.example.demo.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByEmail(String email);
    @Query(value = "SELECT * FROM Account WHERE role_id = :roleId", nativeQuery = true)
    List<Account> findAccountsByRoleIdNative(@Param("roleId") Long roleId);
    @Query(value = "SELECT * FROM Account WHERE role_id = :roleId", nativeQuery = true)
    List<Account> findAccountById(@Param("roleId") Long roleId);


    @Query(value = "SELECT CONCAT('T', MONTH(a.create_date)) AS month, COUNT(a.id) AS count FROM Account a" +
            " WHERE a.create_date between '2024-01-01' AND '2024-12-31' " +
            "GROUP BY MONTH(create_date)", nativeQuery = true)
    List<Object[]> getMonthlyAccountStatistics(String startDate, String endDate);

    Account findByCustomer_PhoneNumber(String phoneNumber);

    Account findTopByOrderByIdDesc();

    Integer countAllByRole_IdAndIsNonLockedTrue(Integer roleId);
}
