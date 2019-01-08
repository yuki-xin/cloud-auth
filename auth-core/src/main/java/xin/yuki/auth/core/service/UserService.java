package xin.yuki.auth.core.service;

import xin.yuki.auth.core.entity.UserModel;

public interface UserService {

	UserModel findByUsername(String username);

}
