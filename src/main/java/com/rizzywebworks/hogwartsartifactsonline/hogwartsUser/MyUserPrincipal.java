package com.rizzywebworks.hogwartsartifactsonline.hogwartsUser;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class MyUserPrincipal implements UserDetails {

    private HogwartsUser hogwartsUser;

    public MyUserPrincipal(HogwartsUser hogwartsUser) {
        this.hogwartsUser = hogwartsUser;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // convert a user's roles from space-delimited string to a list of SimpleGrantedAuthority objects
        // E.g. John's roles are stored in a string like "admin user moderator", we need to convert it
        // Before Conversion, we need to add this "ROLE_" prefix to each role name.

        // split string to array and map it to new authority for auth provider user details

        return Arrays.stream(StringUtils.tokenizeToStringArray(this.hogwartsUser.getRoles(), " "))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

// we need to provide the correct info to auth provider
    @Override
    public String getPassword() {
        return hogwartsUser.getPassword();
    }

    @Override
    public String getUsername() {
        return hogwartsUser.getUsername();
    }


    // if we need the latter, add to user class
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.hogwartsUser.isEnabled();
    }

    // create a getter so later on we can get the user from principal obj
    public HogwartsUser getHogwartsUser() {
        return hogwartsUser;
    }
}
