package com.fitness.gateway;

import com.fitness.gateway.user.RegisterRequest;
import com.fitness.gateway.user.UserService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeycloakUserSyncFilter implements WebFilter {
    private final UserService userService;
    // Java
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain){
        log.info("KeycloakUserSyncFilter triggered for request: " + exchange.getRequest().getURI());
        String userId = exchange.getRequest().getHeaders().getFirst("X-User-ID");
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        RegisterRequest registerRequest = getUserDetails(token);
        log.info("token is here" + token);

        if (userId == null && registerRequest != null) {
            userId = registerRequest.getKeycloakId();
        }

        if (userId != null && token != null) {
            String finalUserId = userId;
            return userService.validateUser(userId)
                    .flatMap(exist -> {
                        if (!exist && registerRequest != null) {
                            return userService.registerUser(registerRequest).then();
                        }
                        log.info("User already exists, Skipping sync");
                        return Mono.empty();
                    })
                    .then(Mono.defer(() -> {
                        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                .header("X-User-ID", finalUserId)
                                .build();
                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
                    }));
        }
        return chain.filter(exchange);
    }

//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain){
//        log.info("KeycloakUserSyncFilter triggered for request: " + exchange.getRequest().getURI());
//        String userId=exchange.getRequest().getHeaders().getFirst("X-User-ID");
//        String token=exchange.getRequest().getHeaders().getFirst("Authorization");
//        RegisterRequest registerRequest=getUserDetails(token);
//        log.info("token is here"+token);
//        if(userId==null){
//            userId=registerRequest.getKeycloakId();
//        }
//
//        if(userId!=null && token!=null){
//            String finalUserId = userId;
//            userService.validateUser(userId).flatMap(exist->{
//                if(!exist){
//
//                    if(registerRequest!=null){
//                        return userService.registerUser(registerRequest)
//                                .then(Mono.empty());
//                    }else{
//                        return Mono.empty();
//                    }
//                }else{
//                    log.info("User already exists, Skipping sync");
//                    return Mono.empty();
//                }
//            }).then(Mono.defer(()->{
//                ServerHttpRequest mutatedRequest=exchange.getRequest().mutate()
//                        .header("X-User-ID", finalUserId)
//                        .build();
//                return chain.filter(exchange.mutate().request(mutatedRequest).build());
//            }));
//        }
//        return chain.filter(exchange);
//    }

    private RegisterRequest getUserDetails(String token) {
        try{
            String tokenWithoutBearer=token.replace("Bearer ","").trim();
            SignedJWT signedJWT=SignedJWT.parse(tokenWithoutBearer);
            JWTClaimsSet claims=signedJWT.getJWTClaimsSet();
            RegisterRequest registerRequest=new RegisterRequest();
            registerRequest.setEmail(claims.getStringClaim("email"));
            registerRequest.setKeycloakId(claims.getStringClaim("sub"));
            registerRequest.setFirstName(claims.getStringClaim("given_name"));
            registerRequest.setPassword("dummy@12344");
            registerRequest.setLastName(claims.getStringClaim("family_name"));
            return registerRequest;

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
