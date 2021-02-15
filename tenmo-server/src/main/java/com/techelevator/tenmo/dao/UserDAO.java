package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface UserDAO {

    List<User> findAll();

    User findByUsername(String username);
    
    User getUserByAccount(int id);
    
    User findByUserId(int id);

    List<Transfer> getTransfersByUser(int userId);
    
    BigDecimal updateBalance(int userId, BigDecimal balance);
    
    BigDecimal getBalanceById(int userId);
    
    int getAccountByUserId(int id);
    
    int findIdByUsername(String username);

    boolean create(String username, String password);
}
