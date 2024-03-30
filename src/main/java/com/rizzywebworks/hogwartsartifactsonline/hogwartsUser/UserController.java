package com.rizzywebworks.hogwartsartifactsonline.hogwartsUser;

import com.rizzywebworks.hogwartsartifactsonline.hogwartsUser.converter.UserDtoToUserConverter;
import com.rizzywebworks.hogwartsartifactsonline.hogwartsUser.converter.UserToUserDtoConverter;
import com.rizzywebworks.hogwartsartifactsonline.hogwartsUser.dto.UserDto;
import com.rizzywebworks.hogwartsartifactsonline.system.Result;
import com.rizzywebworks.hogwartsartifactsonline.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class UserController {

    private final UserService userService;

    private final UserDtoToUserConverter userDtoToUserConverter;

    private final UserToUserDtoConverter userToUserDtoConverter;


    public UserController(UserService userService, UserDtoToUserConverter userDtoToUserConverter, UserToUserDtoConverter userToUserDtoConverter) {
        this.userService = userService;
        this.userDtoToUserConverter = userDtoToUserConverter;
        this.userToUserDtoConverter = userToUserDtoConverter;
    }

    @GetMapping
    public Result findallUsers() {
        List<HogwartsUser> foundHogwartsUsers = this.userService.findAll();

        List<UserDto> userDtos = foundHogwartsUsers.stream().map(this.userToUserDtoConverter::convert).toList();

        // note that user dto does not contain password field
        return new Result(true, StatusCode.SUCCESS, "Find All Success", userDtos);
    }

    @GetMapping("/{userId}")
    public Result findUserById(@PathVariable Integer userId) {
        HogwartsUser foundHogwartsUser = this.userService.findById(userId);

        UserDto userDto = this.userToUserDtoConverter.convert(foundHogwartsUser);

        // note that user dto does not contain password field
        return new Result(true, StatusCode.SUCCESS, "Find One Success", userDto);
    }

    @PostMapping
    public Result addUser(@Valid @RequestBody HogwartsUser newHogwartsUser) {
        HogwartsUser savedUser = this.userService.save(newHogwartsUser);

        UserDto userDto = this.userToUserDtoConverter.convert(savedUser);

        // note that user dto does not contain password field
        return new Result(true, StatusCode.SUCCESS, "Add Success", userDto);
    }

    // we are not using this to update password, need another changePassword method

    @PutMapping("/{userId}")
    public Result updateUser(@PathVariable Integer userId,@Valid @RequestBody UserDto userDto) {
        HogwartsUser update = this.userDtoToUserConverter.convert(userDto);
        HogwartsUser updatedHogwartsUser = this.userService.update(userId, update);
        UserDto updatedUserDto = this.userToUserDtoConverter.convert(updatedHogwartsUser);

        // note that user dto does not contain password field
        return new Result(true, StatusCode.SUCCESS, "Update Success", updatedUserDto);
    }


    @DeleteMapping("/{userId}")
    public Result deleteUser(@PathVariable Integer userId) {
        this.userService.delete(userId);

        // note that user dto does not contain password field
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }




}
