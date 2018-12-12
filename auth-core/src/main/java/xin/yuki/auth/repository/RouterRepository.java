package xin.yuki.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xin.yuki.auth.entity.RouterEntity;

public interface RouterRepository extends JpaRepository<RouterEntity, Long> {


	boolean existsByName(String name);
}
