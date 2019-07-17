package com.api.p52.service;

import com.api.p52.dao.UserDAO;
import com.api.p52.model.User;
import com.api.p52.model.UserCredentials;
import com.api.p52.model.UserRegister;
import com.api.p52.properties.ApplicationProperties;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.spi.resource.Singleton;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.mindrot.jbcrypt.BCrypt;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Singleton
public class AuthService {
    @InjectParam
    private UserDAO userDAO;
    @InjectParam
    private ApplicationProperties properties;

    public User authenticate(String token){
        try {
            String login = Jwts.parser()
                    .setSigningKey(getKey())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return userDAO.findOneByLogin(login);
        }catch (Exception e){
            return null;
        }
    }
    public String issueToken(String path,String login,Date expire){

        String jwtToken = Jwts.builder()
                .setSubject(login)
                .setIssuer(path)
                .setIssuedAt(new Date())
                .setExpiration(expire)
                .signWith(getAlgorithm(),getKey())
                .compact();
        return jwtToken;
    }
    public User register(UserRegister register){
        User user = new User(register);
        user.setPassCrypt(BCrypt.hashpw(register.getPassword(),BCrypt.gensalt()));
        user.addRole("GUEST");
        user.addRole("MEMBER");
        userDAO.insert(user);
        return user;
    }
    public boolean isAuthenticate(UserCredentials credentials){
        User user = userDAO.findOneByLogin(credentials.getLogin());
        if(user == null){
            return false;
        }
        return BCrypt.checkpw(credentials.getPassword(),user.getPassCrypt());
    }
    private SignatureAlgorithm getAlgorithm(){
        return SignatureAlgorithm.valueOf(properties.getProperty("app.jwt.algorithm"));
    }
    private Key getKey() {
        byte[] decodedKey = Base64.getDecoder().decode(properties.getProperty("app.jwt.key"));
        return new SecretKeySpec(decodedKey,properties.getProperty("app.jwt.algorithm"));
    }

}
