package com.monapp.dao;

import com.monapp.model.Admin;

public interface AdminDao {
    Admin findByCredentials(String username,String password);
}
