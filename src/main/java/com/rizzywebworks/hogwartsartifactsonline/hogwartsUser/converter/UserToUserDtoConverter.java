package com.rizzywebworks.hogwartsartifactsonline.hogwartsUser.converter;

import com.rizzywebworks.hogwartsartifactsonline.hogwartsUser.HogwartsUser;
import com.rizzywebworks.hogwartsartifactsonline.hogwartsUser.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<HogwartsUser, UserDto> {
    
    @Override
    public UserDto convert(HogwartsUser source) {
        return new UserDto(source.getId(), source.getUsername(), source.isEnabled(), source.getRoles());
    }
}
