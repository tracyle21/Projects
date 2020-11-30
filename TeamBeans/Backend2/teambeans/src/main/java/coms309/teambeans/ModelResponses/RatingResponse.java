package coms309.teambeans.ModelResponses;

import coms309.teambeans.Models.RatingProfile;

public class RatingResponse {
    RatingProfile ratingProfile;

    public RatingResponse(RatingProfile ratingProfile) {
        this.ratingProfile = ratingProfile;
    }

    public RatingResponse() {
    }

    public RatingProfile getRatingProfile() {
        return ratingProfile;
    }

    public void setRatingProfile(RatingProfile ratingProfile) {
        this.ratingProfile = ratingProfile;
    }
}
