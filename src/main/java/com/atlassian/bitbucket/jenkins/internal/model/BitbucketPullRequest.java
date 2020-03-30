package com.atlassian.bitbucket.jenkins.internal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static org.apache.commons.lang3.StringUtils.stripToEmpty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BitbucketPullRequest {

    private final int id;
    private BitbucketRepositoryRef fromRef;
    private BitbucketRepositoryRef toRef;
    private List<BitbucketNamedLink> cloneUrls = new ArrayList<>();
    private String selfLink;

    @JsonCreator
    public BitbucketPullRequest(
            @JsonProperty("id") int id,
            @JsonProperty("fromRef") BitbucketRepositoryRef fromRef,
            @JsonProperty("toRef") BitbucketRepositoryRef toRef,
            @CheckForNull @JsonProperty("links") Map<String, List<BitbucketNamedLink>> links) {
        this.id = id;
        this.fromRef = fromRef;
        this.toRef = toRef;
        if (links != null) {
            setLinks(links);
        }
    }

    public BitbucketRepositoryRef getFromRef() {
        return fromRef;
    }

    public BitbucketRepositoryRef getToRef() {
        return toRef;
    }

    public int getId() {
        return id;
    }

    public List<BitbucketNamedLink> getCloneUrls() {
        return cloneUrls;
    }

    /**
     * The self link on webhook events was only introduced in Bitbucket Server 5.14, so this may be blank
     *
     * @return the self link for the repository if the Bitbucket instance is 5.14 or higher, otherwise {@code ""}
     */
    public String getSelfLink() {
        return stripToEmpty(selfLink);
    }

    private void setLinks(Map<String, List<BitbucketNamedLink>> rawLinks) {
        List<BitbucketNamedLink> clones = rawLinks.get("clone");
        if (clones != null) {
            cloneUrls = unmodifiableList(clones);
        } else {
            cloneUrls = emptyList();
        }
        List<BitbucketNamedLink> link = rawLinks.get("self");
        if (link != null && !link.isEmpty()) { // there should always be exactly one self link.
            selfLink = link.get(0).getHref();
        }
    }
}
