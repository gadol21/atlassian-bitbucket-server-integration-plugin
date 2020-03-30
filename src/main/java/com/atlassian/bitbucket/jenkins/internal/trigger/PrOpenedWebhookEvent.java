package com.atlassian.bitbucket.jenkins.internal.trigger;

import com.atlassian.bitbucket.jenkins.internal.model.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static java.util.Objects.requireNonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PrOpenedWebhookEvent extends RefsChangedWebhookEvent {

    private final BitbucketPullRequest pullRequest;

    @JsonCreator
    public PrOpenedWebhookEvent(
            @JsonProperty(value = "actor") @Nullable BitbucketUser actor,
            @JsonProperty(value = "eventKey", required = true) String eventKey,
            @JsonProperty(value = "date", required = true) Date date,
            @JsonProperty(value = "pullRequest", required = true) BitbucketPullRequest pullRequest) {
        super(actor, eventKey, date, extractChanges(pullRequest), extractRepository(pullRequest));
        this.pullRequest = pullRequest;
    }

    public static BitbucketRepository extractRepository(BitbucketPullRequest pullRequest) {
        requireNonNull(pullRequest, "pullRequest");
        return pullRequest.getFromRef().getRepository();
    }

    public static List<BitbucketRefChange> extractChanges(BitbucketPullRequest pullRequest) {
        requireNonNull(pullRequest, "pullRequest");

        List<BitbucketRefChange> changes = new LinkedList<BitbucketRefChange>();

        String fromId = pullRequest.getFromRef().getId();
        String fromDisplayId = pullRequest.getFromRef().getDisplayId();
        changes.add(new BitbucketRefChange(new BitbucketRef(fromId, fromDisplayId, BitbucketRefType.BRANCH),
                                           fromId,
                                           pullRequest.getFromRef().getLatestCommit(),
                                           pullRequest.getToRef().getLatestCommit(),
                                           BitbucketRefChangeType.UPDATE));
        return changes;
    }

    public BitbucketPullRequest getPullRequest() {
        return pullRequest;
    }
}
