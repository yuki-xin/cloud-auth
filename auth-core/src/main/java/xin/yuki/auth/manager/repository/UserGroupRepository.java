package xin.yuki.auth.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xin.yuki.auth.manager.entity.UserGroupEntity;

/**
 * @author zhang
 */
public interface UserGroupRepository extends JpaRepository<UserGroupEntity,Long> {
}
