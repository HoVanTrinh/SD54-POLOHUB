package com.example.demo.controllers.admin;

import com.example.demo.entities.Account;
import com.example.demo.repositories.AccountRepository;
import com.example.demo.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AccountMngController {
    private final AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;

    public AccountMngController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/admin-only/account-management")
    public String viewAccountManagementPage(Model model) {
        List<Account> accountList = accountService.findAllAccount();
        model.addAttribute("accountList", accountList);
        return "/admin/account";
    }
    @GetMapping("/admin-only/account-employee")
    public String viewAccount(Model model) {
        Long roleId = 2L; // Giá trị cố định
        List<Account> accountList = accountRepository.findAccountsByRoleIdNative(roleId);
        model.addAttribute("accountList", accountList);
        return "/admin/accountemployee";
    }
    @GetMapping("/admin-only/account-user")
    public String viewAccount1(Model model) {
        Long roleId = 3L; // Giá trị cố định
        List<Account> accountList = accountRepository.findAccountById(roleId);
        model.addAttribute("accountList", accountList);
        return "/admin/accountuser";
    }
}
