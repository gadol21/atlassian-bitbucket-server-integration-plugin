package com.atlassian.bitbucket.jenkins.internal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import static java.util.Objects.requireNonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BitbucketRepositoryRef {

    private final String displayId;
    private final String id;
    private final String latestCommit;
    private BitbucketRepository repository;

    @JsonCreator
    public BitbucketRepositoryRef(
            @JsonProperty("id") String id,
            @JsonProperty("displayId") String displayId,
            @JsonProperty("repository") BitbucketRepository repository,
            @JsonProperty("latestCommit") String latestCommit) {
        this.id = requireNonNull(id, "id");
        this.displayId = requireNonNull(displayId, "displayId");
        this.repository = requireNonNull(repository, "repository");
        this.latestCommit = requireNonNull(latestCommit, "latestCommit");
    }

    public String getDisplayId() {
        return displayId;
    }

    public String getId() {
        return id;
    }

    public BitbucketRepository getRepository() {
        return repository;
    }

    public String getLatestCommit() {
        return latestCommit;
    }
}
