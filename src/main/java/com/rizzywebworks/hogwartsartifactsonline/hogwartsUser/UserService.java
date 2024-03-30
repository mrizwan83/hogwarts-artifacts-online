package com.rizzywebworks.hogwartsartifactsonline.hogwartsUser;

import com.rizzywebworks.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public List<HogwartsUser> findAll() {
        return this.userRepository.findAll();
    }

    public HogwartsUser findById(Integer userId) {
        return this.userRepository.findById(userId).orElseThrow(()-> new ObjectNotFoundException("user", userId));
    }

    public HogwartsUser save(HogwartsUser newHogwartsUser) {
        // we need to encode plain text password before saving to db
        newHogwartsUser.setPassword(this.passwordEncoder.encode(newHogwartsUser.getPassword()));
        return this.userRepository.save(newHogwartsUser);
    }

    public HogwartsUser update(Integer userId, HogwartsUser update) {
        HogwartsUser oldUser = this.userRepository.findById(userId).orElseThrow(()-> new ObjectNotFoundException("user", userId));
        oldUser.setUsername(update.getUsername());
        oldUser.setEnabled(update.isEnabled());
        oldUser.setRoles(update.getRoles());
        return this.userRepository.save(oldUser);
    }

    public void delete(Integer userId) {
        this.userRepository.findById(userId).orElseThrow(()-> new ObjectNotFoundException("user", userId));
        this.userRepository.deleteById(userId);
    }


    // this gets called by authentication provider where it passes it to spring security to authenticate
    // we are using dao authentication provider
    // user service bean is injected to authentication provider, so it can load user information

    // we need to return type UserDetails, but our repository returns HogwartsUser type
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)// first we need to find this user from database
                .map(MyUserPrincipal::new) //if found, wrap the returned user instance in a MyUserPrincipal Instance
                .orElseThrow(() -> new UsernameNotFoundException("username " + username + " is not found.")); // otherwise throw exception
    }
}
