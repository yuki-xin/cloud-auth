package xin.yuki.auth.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xin.yuki.auth.core.entity.oauth.OauthClient;

public interface ClientRepository extends JpaRepository<OauthClient, String> {

	Boolean existsByClientId(String clientId);
}
