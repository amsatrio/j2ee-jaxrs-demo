package io.github.amsatrio.middleware;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import io.github.amsatrio.annotation.DecryptPayload;
import io.github.amsatrio.annotation.EncryptPayload;


@Provider
@ApplicationScoped
public class MiddlewareDynamicFeature implements DynamicFeature {

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        if (resourceInfo.getResourceMethod().isAnnotationPresent(DecryptPayload.class)) {
            DecrypterMiddleware decrypterMiddleware = CDI.current().select(DecrypterMiddleware.class).get();
            context.register(decrypterMiddleware);
        }
        if (resourceInfo.getResourceMethod().isAnnotationPresent(EncryptPayload.class)) {
            EncrypterMiddleware encrypterMiddleware = CDI.current().select(EncrypterMiddleware.class).get();
            context.register(encrypterMiddleware);
        }
    }
}