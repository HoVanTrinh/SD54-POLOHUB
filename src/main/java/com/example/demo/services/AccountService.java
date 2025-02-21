package com.example.demo.services;

import com.example.demo.dto.Account.AccountDto;
import com.example.demo.dto.Account.ChangePasswordDto;
import com.example.demo.entities.Account;

import java.util.List;

public interface AccountService {
    Account findByEmail(String email);

    List<Account> findAllAccount();
    Account save(Account account);

//    List<UserStatistic> getUserStatistics(String startDate, String endDate);

    Account blockAccount(Long id);
    Integer countAllByRole_IdAndIsNonLockedTrue(Integer roleId);
    Account openAccount(Long id);

    Account changeRole(String email, Long roleId);

    AccountDto getAccountLogin();

    AccountDto updateProfile(AccountDto accountDto);

    void changePassword(ChangePasswordDto changePasswordDto);

    void resetPassword(Account account, String newPassword);
}
