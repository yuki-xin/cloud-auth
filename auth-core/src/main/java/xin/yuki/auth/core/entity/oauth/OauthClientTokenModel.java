package xin.yuki.auth.core.entity.oauth;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author zhang
 */
@Data
@Table(name = "oauth_refresh_token")
public class OauthClientTokenModel implements Serializable {

	private static final long serialVersionUID = -3773914189664212703L;
	@Id
	private String tokenId;

	private byte[] token;

	private String authenticationId;

	private String userName;

	private String clientId;


}
