package xin.yuki.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xin.yuki.auth.entity.oauth.OauthClient;

public interface ClientRepository extends JpaRepository<OauthClient, String> {


	Boolean existsByClientId(String clientId);
}
