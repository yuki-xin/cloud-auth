package xin.yuki.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xin.yuki.auth.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    UserEntity findByUsername(String username);

    void deleteByUsername(String username);

    boolean existsByUsername(String username);

}
