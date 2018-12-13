package xin.yuki.auth.core.entity.oauth;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author zhang
 */
@Data
@Table(name = "oauth_access_token")
public class OauthAccessTokenModel implements Serializable {

	private static final long serialVersionUID = -1443584002396439635L;

	@Id
	private String tokenId;

	private byte[] token;

	private String authenticationId;

	private String userName;

	private String clientId;

	private byte[] authentication;

	private String refreshToken;


}
