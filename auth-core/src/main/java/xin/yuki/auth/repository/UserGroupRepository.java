package xin.yuki.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xin.yuki.auth.entity.UserGroupEntity;

/**
 * @author zhang
 */
public interface UserGroupRepository extends JpaRepository<UserGroupEntity,Long> {
}
