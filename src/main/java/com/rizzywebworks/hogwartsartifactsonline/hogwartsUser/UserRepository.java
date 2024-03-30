package com.rizzywebworks.hogwartsartifactsonline.hogwartsUser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<HogwartsUser, Integer> {

    Optional<HogwartsUser> findByUsername(String username);

//    List<HogwartsUser> findByEnabled(boolean enabled);

    //find by 2 fields example
//    Optional<HogwartsUser> findByUsernameAndPassword(String username, String password);


}
