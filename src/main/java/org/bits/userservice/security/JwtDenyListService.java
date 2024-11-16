package org.bits.userservice.security;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JwtDenyListService {
    private final Set<String> denyList = new HashSet<>();

    public void addTokenToDenyList(String token) {
        denyList.add(token);
    }

    public boolean isTokenInDenyList(String token) {
        return denyList.contains(token);
    }
}