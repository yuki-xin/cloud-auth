package xin.yuki.auth.core.entity.oauth;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author zhang
 */
@Data
@Table(name = "oauth_refresh_token")
public class OauthRefreshTokenModel implements Serializable {

	private static final long serialVersionUID = -3015131890754970395L;
	@Id
	private String tokenId;

	@Lob
	private byte[] token;

	@Lob
	private byte[] authentication;


}
