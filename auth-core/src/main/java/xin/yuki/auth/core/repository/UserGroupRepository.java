package xin.yuki.auth.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xin.yuki.auth.core.entity.UserGroupEntity;

/**
 * @author zhang
 */
public interface UserGroupRepository extends JpaRepository<UserGroupEntity,Long> {
}
