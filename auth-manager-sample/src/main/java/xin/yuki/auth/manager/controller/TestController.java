package xin.yuki.auth.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xin.yuki.auth.core.service.UserService;

@RestController
public class TestController {

	@Autowired
	private UserService userService;

	@GetMapping("/test")
	public String test() {
		return "test";
	}
}
