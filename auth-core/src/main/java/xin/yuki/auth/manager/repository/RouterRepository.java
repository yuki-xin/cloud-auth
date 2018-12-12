package xin.yuki.auth.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xin.yuki.auth.manager.entity.RouterEntity;

public interface RouterRepository extends JpaRepository<RouterEntity, Long> {


	boolean existsByName(String name);
}
