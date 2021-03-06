package xin.yuki.auth.core.entity.oauth;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "oauth_approvals")
public class OauthApprovalModel implements Serializable {

	private static final long serialVersionUID = -1007648892846148457L;
	@Id
	@Column(name = "userId")
	private String userId;

	@Id
	@Column(name = "clientId")
	private String clientId;

	private String scope;
	private String status;

	@Column(name = "expiresAt")
	private Date expiresAt;

	@Column(name = "lastModifiedAt")
	private Date lastModifiedAt;

}
