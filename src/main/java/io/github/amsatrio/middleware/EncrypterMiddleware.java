package io.github.amsatrio.middleware;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import io.github.amsatrio.annotation.Base64Qualifier;
import io.github.amsatrio.dto.ResponseDto;
import io.github.amsatrio.util.encryption.EncryptionProvider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class EncrypterMiddleware implements ContainerResponseFilter {

    @Inject @Base64Qualifier
    private EncryptionProvider encryptionProvider;

    @Override
    public void filter(ContainerRequestContext containerRequestContext,
            ContainerResponseContext containerResponseContext) throws IOException {
        log.info("filter()");
        if (!containerResponseContext.hasEntity()) {
            return;
        }

        try {
            Object entity = containerResponseContext.getEntity();
            if (entity instanceof ResponseDto) {
                byte[] responsePayloadEncodedBytes = encryptionProvider.encrypt(((ResponseDto<?>) entity).toJsonString().getBytes(StandardCharsets.UTF_8));
                log.info("response payload: {}", ((ResponseDto<?>) entity).toJsonString());
                containerResponseContext.setEntity(responsePayloadEncodedBytes);
            }
        } catch (Exception e) {
            log.warn("could not log response entity: {}", e.getMessage());
        }
    }

}
