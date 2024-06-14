package org.etf.unibl.SecureForum.security;

import org.etf.unibl.SecureForum.model.entities.UserEntity;
import org.etf.unibl.SecureForum.model.enums.UserType;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;


import java.util.List;

public class CustomJwtConverter implements Converter<Jwt, CustomJwt> {

    public CustomJwtConverter(){

    }

    @Override
    public CustomJwt convert(Jwt source) {
        List<GrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority("ROLE_" + UserType.Forumer.getValue()));
        UserEntity newEntity = new UserEntity();
        newEntity.setUsername(source.getClaimAsString("given_name") + source.getClaimAsString("family_name"));

        var customJwt = new CustomJwt(source, grantedAuthorities);
        JWTGenerator jwtGenerator = jwtGenerator();
        String token_value= jwtGenerator.generateToken(newEntity);


        return customJwt;
    }

    @Bean
    public JWTGenerator jwtGenerator(){
        return new JWTGenerator();
    }


}
