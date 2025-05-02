package com.example.bank_system.repository;

import com.example.bank_system.exception.UserNotFoundException;
import com.example.bank_system.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryUserRepository implements IUserRepository{
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        users.put(user.getUserId(), user);
        return user;
    }

    @Override
    public User getAUser(int id) throws UserNotFoundException {
        if(users.containsKey(id))
            return users.get(id);
        throw new UserNotFoundException("User not found");
    }

    @Override
    public void updateUser(User user){
        users.put(user.getUserId(), user);
    }
}
