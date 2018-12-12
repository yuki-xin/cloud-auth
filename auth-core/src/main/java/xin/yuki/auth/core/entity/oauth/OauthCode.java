package xin.yuki.auth.core.entity.oauth;

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
@Table(name = "oauth_code")
public class OauthCode implements Serializable {

	private static final long serialVersionUID = -2613255690960638229L;
	@Id
	private String code;

	@Lob
	private byte[] authentication;

}
