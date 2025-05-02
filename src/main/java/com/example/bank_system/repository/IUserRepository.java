package com.example.bank_system.repository;

import com.example.bank_system.exception.UserNotFoundException;
import com.example.bank_system.model.User;

import java.util.List;

public interface IUserRepository {
    User createUser(User user);
    User getAUser(int id) throws UserNotFoundException;
    void updateUser(User user) throws UserNotFoundException;
}
