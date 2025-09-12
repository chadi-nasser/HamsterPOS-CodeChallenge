package dev.chadinasser.hamsterpos.service;

import dev.chadinasser.hamsterpos.exception.ResourceAlreadyExistException;
import dev.chadinasser.hamsterpos.model.User;
import dev.chadinasser.hamsterpos.repo.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void addUser(User user) {
        userRepo.findByUsername(user.getUsername()).ifPresent(
                u -> {
                    throw new ResourceAlreadyExistException("User", "username", user.getUsername());
                }
        );
        userRepo.save(user);
    }

    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new UsernameNotFoundException("No authenticated user found");
        }
        return (User) auth.getPrincipal();
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepo.findByUsername(username).orElse(null);
            if (user == null) {
                throw new UsernameNotFoundException("User not found: " + username);
            }
            return user;
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found: " + username, e);
        }
    }
}
