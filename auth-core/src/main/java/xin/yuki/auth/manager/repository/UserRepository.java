package xin.yuki.auth.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xin.yuki.auth.manager.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    UserEntity findByUsername(String username);

    void deleteByUsername(String username);

    boolean existsByUsername(String username);

}
