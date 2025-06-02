package com.practice.config;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.practice.entities.User;

public class CustomUserDetails implements UserDetails{

	private User user;
	
	public CustomUserDetails(User user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(user.getRole());
		return List.of(simpleGrantedAuthority);
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}
	
	@Override
    public boolean isAccountNonExpired() {
        return true; // You can modify this if your app handles account expiry
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Modify if you support locked accounts
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Modify if passwords expire
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled(); // This should come from your User entity (e.g., a boolean field)
    }
}
