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
@Table(name = "oauth_refresh_token")
public class OauthClientToken implements Serializable {

	private static final long serialVersionUID = -3773914189664212703L;
	@Id
	private String tokenId;

	@Lob
	private byte[] token;

	private String authenticationId;

	private String userName;

	private String clientId;


}