package xin.yuki.auth.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xin.yuki.auth.core.entity.RouterEntity;

public interface RouterRepository extends JpaRepository<RouterEntity, Long> {


	boolean existsByName(String name);
}
