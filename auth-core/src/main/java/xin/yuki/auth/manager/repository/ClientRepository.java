package xin.yuki.auth.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xin.yuki.auth.manager.entity.oauth.OauthClient;

public interface ClientRepository extends JpaRepository<OauthClient, String> {


	Boolean existsByClientId(String clientId);
}
