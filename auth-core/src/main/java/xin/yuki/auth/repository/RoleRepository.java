package xin.yuki.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xin.yuki.auth.entity.RoleEntity;

/**
 * @author zhang
 */
public interface RoleRepository extends JpaRepository<RoleEntity,Long> {
}
