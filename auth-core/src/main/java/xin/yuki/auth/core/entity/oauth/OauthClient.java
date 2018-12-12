package xin.yuki.auth.core.entity.oauth;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "oauth_client_details")
public class OauthClient implements Serializable {


    private static final long serialVersionUID = 5179734325149744146L;
    @Id
    private String clientId;

    private String clientSecret;

    private String scope;

    private String authorities;

    private String resourceIds;

    
    private String authorizedGrantTypes;

    @Column(columnDefinition = "varchar(2048)")
    private String webServerRedirectUri;

    private String autoApproveScopes;


    private Integer accessTokenValidity;

    private Integer refreshTokenValidity;

    @Column(columnDefinition = "varchar(4096)")
    private String additionalInformation;

    private String autoapprove;

}
