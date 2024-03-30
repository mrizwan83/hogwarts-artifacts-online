package com.rizzywebworks.hogwartsartifactsonline.security;

import com.rizzywebworks.hogwartsartifactsonline.hogwartsUser.HogwartsUser;
import com.rizzywebworks.hogwartsartifactsonline.hogwartsUser.MyUserPrincipal;
import com.rizzywebworks.hogwartsartifactsonline.hogwartsUser.converter.UserToUserDtoConverter;
import com.rizzywebworks.hogwartsartifactsonline.hogwartsUser.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final JwtProvider jwtProvider;

    private final UserToUserDtoConverter userToUserDtoConverter;

    public AuthService(JwtProvider jwtProvider, UserToUserDtoConverter userToUserDtoConverter) {
        this.jwtProvider = jwtProvider;
        this.userToUserDtoConverter = userToUserDtoConverter;
    }

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        // get and create user info
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        // do not send this back to client it has password
        HogwartsUser hogwartsUser = principal.getHogwartsUser();

        // send this back instead
        UserDto userDto = this.userToUserDtoConverter.convert(hogwartsUser);

        //create a JWT.
        String token = this.jwtProvider.createToken(authentication);

        Map<String, Object> loginResultMap = new HashMap<>();

        loginResultMap.put("userInfo", userDto);
        loginResultMap.put("token", token);

        return loginResultMap;

    }
}
