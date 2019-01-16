package xin.yuki.auth.core.service;

import xin.yuki.auth.core.entity.UserModel;
import xin.yuki.auth.core.exception.UserException;

import java.util.List;

public interface UserService {

	UserModel findByUsername(String username);

	UserModel findById(Long id);

	List<UserModel> findUserByExample(UserModel user);

	void updateUser(UserModel user) throws UserException;

	void deleteUser(String username) throws UserException;

	void changePassword(String username, String password);

	boolean userExists(String username);

	void enableUser(String username, Boolean enable);

	UserModel createUser(UserModel user);
}
