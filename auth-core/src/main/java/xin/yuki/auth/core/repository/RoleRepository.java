package xin.yuki.auth.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xin.yuki.auth.core.entity.RoleEntity;

/**
 * @author zhang
 */
public interface RoleRepository extends JpaRepository<RoleEntity,Long> {
}
