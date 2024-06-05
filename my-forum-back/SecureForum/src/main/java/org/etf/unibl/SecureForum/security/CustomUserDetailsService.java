package org.etf.unibl.SecureForum.security;

import org.etf.unibl.SecureForum.model.entities.UserEntity;
import org.etf.unibl.SecureForum.model.enums.UserType;
import org.etf.unibl.SecureForum.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity foundUserEntity = userRepository.findByUsernameIs(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        ArrayList<UserType> typeOfUserListContainer = new ArrayList<>();
        typeOfUserListContainer.add(foundUserEntity.getType());
        return new User(foundUserEntity.getUsername(), foundUserEntity.getPassword(), mapRolesToAuthority(typeOfUserListContainer));
    }

    private Collection<GrantedAuthority> mapRolesToAuthority(List<UserType> types){
        ArrayList<UserType> containerForType = new ArrayList<>(types);
        return containerForType.stream().map(type -> new SimpleGrantedAuthority(type.getValue())).collect(Collectors.toList());
    }
}
