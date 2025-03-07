package com.onx.hamburgerpage.configs;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Configuration
public class FirestoreConfig {
    private static final String FIREBASE_INITIALIZATION_ERROR = "Failed to initialize Firestore connection";

    @Autowired
    private FirebaseProperties firebaseProperties;

    @Value("${app.urldatabase}")
    private String urlDatabase;

    @Bean
    public Firestore initializeFirebaseApp() {
        try {
            InputStream serviceAccount = createServiceAccountInputStream();
            FirestoreOptions.Builder firestoreOptionsBuilder = FirestoreOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount));

            // Solo establece el Database ID si no es "default"
            if (!"default".equalsIgnoreCase(urlDatabase)) {
                firestoreOptionsBuilder.setDatabaseId(urlDatabase);
            }

            FirestoreOptions firestoreOptions = firestoreOptionsBuilder.build();
            return firestoreOptions.getService();
        } catch (IOException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, FIREBASE_INITIALIZATION_ERROR, e);
        }
    }
    private InputStream createServiceAccountInputStream() {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("type", firebaseProperties.getType());
        jsonObject.addProperty("project_id", firebaseProperties.getProjectId());
        jsonObject.addProperty("private_key_id", firebaseProperties.getPrivateKeyId());
        jsonObject.addProperty("private_key", firebaseProperties.getPrivateKey().replace("\\n", "\n"));
        jsonObject.addProperty("client_email", firebaseProperties.getClientEmail());
        jsonObject.addProperty("client_id", firebaseProperties.getClientId());
        jsonObject.addProperty("auth_uri", firebaseProperties.getAuthUri());
        jsonObject.addProperty("token_uri", firebaseProperties.getTokenUri());
        jsonObject.addProperty("auth_provider_x509_cert_url", firebaseProperties.getAuthProviderX509CertUrl());
        jsonObject.addProperty("client_x509_cert_url", firebaseProperties.getClientX509CertUrl());
        jsonObject.addProperty("universe_domain", firebaseProperties.getUniverseDomain());

        return new ByteArrayInputStream(gson.toJson(jsonObject).getBytes());
    }
}