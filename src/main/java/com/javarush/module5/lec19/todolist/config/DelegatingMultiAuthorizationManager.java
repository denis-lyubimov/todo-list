package com.javarush.module5.lec19.todolist.config;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class DelegatingMultiAuthorizationManager<T> implements AuthorizationManager<T> {
    private final List<AuthorizationManager<T>> authorizationManagers;

    public DelegatingMultiAuthorizationManager(List<AuthorizationManager<T>> authorizationManagers) {
        this.authorizationManagers = authorizationManagers;
    }

    public static <T> DelegatingMultiAuthorizationManager<T> hasAll(AuthorizationManager<T>... authorizationManagers) {
        Assert.notEmpty(authorizationManagers, "authorizationManagers cannot be empty");
        Assert.noNullElements(authorizationManagers, "authorizationManagers cannot contain null values");
        return new DelegatingMultiAuthorizationManager(Arrays.asList(authorizationManagers));
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, T object) {
        for (AuthorizationManager<T> authorizationManager : authorizationManagers) {
            AuthorizationDecision decision = authorizationManager.check(authentication, object);
            if (decision == null || !decision.isGranted()) {
                return new AuthorizationDecision(false);
            }
        }

        return new AuthorizationDecision(true);
    }
}
