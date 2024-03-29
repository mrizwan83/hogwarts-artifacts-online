package com.rizzywebworks.hogwartsartifactsonline.hogwartsUser;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<HogwartsUser, Integer> {
}
