package xin.yuki.auth.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xin.yuki.auth.manager.entity.RoleEntity;

/**
 * @author zhang
 */
public interface RoleRepository extends JpaRepository<RoleEntity,Long> {
}
