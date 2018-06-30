/*
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mycompany.ecoserverweb.controllers;

// [START import_libraries]
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionScopes;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.common.collect.ImmutableList;
import com.mycompany.ecoserverpersistence.models.Transaction;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
// [END import_libraries]

/**
 * A sample application that uses the Vision API to label an image.
 */
@SuppressWarnings("serial")
public class LabelApp {

    /**
     * Be sure to specify the name of your application. If the application name
     * is {@code null} or blank, the application will log a warning. Suggested
     * format is "MyCompany-ProductName/1.0".
     */
    private static final String CREDENTIALS_JSON_PATH = "src/main/resources/ECO-AI-ca7381b3b373.json";

    private static final String APPLICATION_NAME = "EcoServer";//"eco-ai";//= "Google-VisionLabelSample/1.0";

    private static final int MAX_LABELS = 300;

    private static final List<String> materials = Arrays.asList("plastic", "paper", "aluminium", "glass");

    private static final Long pointsPerMaterial = 30L;

    private static final Map<String, String> badMaterials = new HashMap<String, String>() {
        {
            put("battery", "Battery is a dangerous item. DO NOT RECYCLE. Find a batery recycle point on your municipality.");
            put("food", "Food is not recyclable.");
        }
    };

    //private static final Logger logger = LoggerFactory.getLogger(this.getClass());
    // [START run_application]
    /**
     * Annotates an image using the Vision API.
     */
    public static void googleApiProcess(Transaction transaction) throws IOException, GeneralSecurityException, URISyntaxException {

        Path imagePath = Paths.get(transaction.getPictureUrl());
//    Path imagePath = Paths.get(new URL(pathPhoto).toURI());

        LabelApp app = new LabelApp(getVisionService());
        printLabels(transaction, System.out, imagePath, app.labelImage(imagePath, MAX_LABELS));
    }

    /**
     * Prints the labels received from the Vision API.
     */
    private static void printLabels(Transaction transaction, PrintStream out, Path imagePath, List<EntityAnnotation> labels) {
        out.printf("Labels for image " + imagePath);
        boolean materialFoundFlag = false;
        List<String> badMaterialsDescriptionFound = new ArrayList<>();
        if (labels.isEmpty()) {
            out.println("\tNo labels found.");
            transaction.setEcoPoints(0L);
            transaction.setDescription("empty");
            return;
        }
        for (EntityAnnotation label : labels) {
            out.printf(
                    "\t%s (score: %.3f)\n",
                    label.getDescription(),
                    label.getScore());
            if (!materialFoundFlag) {
                if (materials.contains(label.getDescription().toLowerCase())) {
                    materialFoundFlag = true;
                }
            }
            for (String bad : badMaterials.keySet()) {
                if (label.getDescription().toLowerCase().contains(bad)) {
                    badMaterialsDescriptionFound.add(badMaterials.get(bad));
                    break;
                }
            }
        }
        if (materialFoundFlag && badMaterialsDescriptionFound.isEmpty()) {
            transaction.setEcoPoints(pointsPerMaterial);
            transaction.setDescription("Thank you !");
        } else {
            transaction.setEcoPoints(0L);
            transaction.setDescription(String.join(", ", badMaterialsDescriptionFound));
        }
    }
    // [END run_application]

    // [START authenticate]
    /**
     * Connects to the Vision API using Application Default Credentials.
     */
    public static Vision getVisionService() throws IOException, GeneralSecurityException {
        /*GoogleCredential credential =
        GoogleCredential.getApplicationDefault().createScoped(VisionScopes.all());*/
        GoogleCredential credential = GoogleCredential.fromStream(
                Files.newInputStream(Paths.get(CREDENTIALS_JSON_PATH)))
                .createScoped(VisionScopes.all());
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        return new Vision.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    // [END authenticate]

    private final Vision vision;

    /**
     * Constructs a {@link LabelApp} which connects to the Vision API.
     */
    public LabelApp(Vision vision) {
        this.vision = vision;
    }

    /**
     * Gets up to {@code maxResults} labels for an image stored at {@code path}.
     */
    public List<EntityAnnotation> labelImage(Path path, int maxResults) throws IOException {
        // [START construct_request]
        byte[] data = Files.readAllBytes(path);

        AnnotateImageRequest request
                = new AnnotateImageRequest()
                        .setImage(new Image().encodeContent(data))
                        .setFeatures(ImmutableList.of(
                                new Feature()
                                        .setType("LABEL_DETECTION")
                                        .setMaxResults(maxResults)));
        Vision.Images.Annotate annotate
                = vision.images()
                        .annotate(new BatchAnnotateImagesRequest().setRequests(ImmutableList.of(request)));
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotate.setDisableGZipContent(true);
        // [END construct_request]

        // [START parse_response]
        BatchAnnotateImagesResponse batchResponse = annotate.execute();
        assert batchResponse.getResponses().size() == 1;
        AnnotateImageResponse response = batchResponse.getResponses().get(0);
        if (response.getLabelAnnotations() == null) {
            throw new IOException(
                    response.getError() != null
                    ? response.getError().getMessage()
                    : "Unknown error getting image annotations");
        }
        return response.getLabelAnnotations();
        // [END parse_response]
    }

}
