package io.github.amsatrio.middleware;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;

import io.github.amsatrio.annotation.Base64Qualifier;
import io.github.amsatrio.util.encryption.EncryptionProvider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class DecrypterMiddleware implements ContainerRequestFilter {

    @Inject @Base64Qualifier
    private EncryptionProvider encryptionProvider;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        log.info("filter()");
        if (!containerRequestContext.hasEntity()) {
            return;
        }
        InputStream is = containerRequestContext.getEntityStream();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        byte[] requestBytes = os.toByteArray();

        byte[] requestPayloadDecodedBytes = null;
        try {
            requestPayloadDecodedBytes = encryptionProvider.decrypt(requestBytes);
        } catch (Exception e) {
            containerRequestContext.setEntityStream(new ByteArrayInputStream(requestBytes));
            return;
        }
        String requestPayloadDecoded = new String(requestPayloadDecodedBytes, StandardCharsets.UTF_8);
        log.info("request payload: {}", requestPayloadDecoded);

        containerRequestContext
                .setEntityStream(new ByteArrayInputStream(requestPayloadDecoded.getBytes(StandardCharsets.UTF_8)));
        if (!containerRequestContext.getMediaType().isCompatible(MediaType.APPLICATION_JSON_TYPE)) {
            containerRequestContext.getHeaders().putSingle("Content-Type", MediaType.APPLICATION_JSON);
        }
    }

}
