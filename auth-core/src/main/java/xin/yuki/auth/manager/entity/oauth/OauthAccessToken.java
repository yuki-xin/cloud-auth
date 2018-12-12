package xin.yuki.auth.manager.entity.oauth;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author zhang
 */
@Entity
@Data
@Table(name = "oauth_access_token")
public class OauthAccessToken implements Serializable {

	private static final long serialVersionUID = -1443584002396439635L;
	@Id
	private String tokenId;

	@Lob
	private byte[] token;

	private String authenticationId;

	private String userName;

	private String clientId;

	@Lob
	private byte[] authentication;

	private String refreshToken;


}
