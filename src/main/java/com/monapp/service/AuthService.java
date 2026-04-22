package com.monapp.service;

import com.monapp.model.Admin;

public interface AuthService {
    Admin login(String username, String password);
}
