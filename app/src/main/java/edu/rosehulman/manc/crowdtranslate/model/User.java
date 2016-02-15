package edu.rosehulman.manc.crowdtranslate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by manc on 1/17/2016.
 */
public class User {

    @JsonIgnore
    private String key;
    private String username;
    private List<String> tagArray; // not really used

    // Set version of tag, for faster membership queries
    @JsonIgnore
    private Set<String> tagSet;

    public User(){}

    public String getKey() {
        return key;
    }

    public String getUsername() {
        return username;
    }

    public Set<String> getTagSet() {
        return tagSet;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTagArray(List<String> tagArray) {
        this.tagArray = tagArray;
        this.tagSet = new HashSet<>(tagArray);
    }
}
