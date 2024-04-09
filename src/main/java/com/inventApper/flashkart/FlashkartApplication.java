package com.inventApper.flashkart;

import com.inventApper.flashkart.entities.Role;
import com.inventApper.flashkart.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class FlashkartApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(FlashkartApplication.class, args);
	}

	@Autowired
	private RoleRepository roleRepository;

	@Value("${admin.role.id}")
	private String role_admin_id;

	@Value("${normal.role.id}")
	private String role_normal_id;

	@Override
	public void run(String... args) throws Exception {
		try {
			Role role_admin = Role.builder().roleId(role_admin_id).roleName("ROLE_ADMIN").build();
			Role role_normal = Role.builder().roleId(role_normal_id).roleName("ROLE_NORMAL").build();
			roleRepository.saveAll(Arrays.asList(role_admin, role_normal));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
