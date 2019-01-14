package xin.yuki.auth.core.service;

import xin.yuki.auth.core.entity.UserModel;

import java.util.List;

public interface UserService {

	UserModel findByUsername(String username);

	List<UserModel> findUserByExample(UserModel user);

	void updateUser(UserModel user);

	void deleteUser(String username);

	void changePassword(String oldPassword, String newPassword);

	boolean userExists(String username);
}
