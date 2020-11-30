package coms309.teambeans.Models;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
@Table(name = "ratingProfile")
public class RatingProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(notes = "Teamwork rating for a user", name = "teamwork", required = true)
    private Double teamwork;

    @ApiModelProperty(notes = "Communication rating for a user", name = "communication", required = true)
    private Double communication;

    @ApiModelProperty(notes = "Work Quality rating for a user", name = "workQuality", required = true)
    private Double workQuality;

    @ApiModelProperty(notes = "Attitude rating for a user", name = "attitude", required = true)
    private Double attitude;

    @ApiModelProperty(notes = "overall rating for a user", name = "overall", required = true)
    private Double overall; //overall rating

    @ApiModelProperty(notes = "Total number of people that have rated a user", name = "totalReviewed", required = true)
    private Integer totalReviewed; //how many people have rated this user

    @ApiModelProperty(notes = "User to which the rating profile belongs to", name = "ratingProfile", required = true)
    @OneToOne(mappedBy = "ratingProfile")
    private User user;

    public RatingProfile() {
    }

    public RatingProfile(Double teamwork, Double communication, Double workQuality, Double attitude, Double overall, Integer totalReviewed) {
        this.teamwork = teamwork;
        this.communication = communication;
        this.workQuality = workQuality;
        this.attitude = attitude;
        this.overall = overall;
        this.totalReviewed = totalReviewed;
    }

    public Double getTeamwork() {
        return teamwork;
    }

    public void setTeamwork(Double teamwork) {
        this.teamwork = teamwork;
    }

    public Double getCommunication() {
        return communication;
    }

    public void setCommunication(Double communication) {
        this.communication = communication;
    }

    public Double getWorkQuality() {
        return workQuality;
    }

    public void setWorkQuality(Double workQuality) {
        this.workQuality = workQuality;
    }

    public Double getAttitude() {
        return attitude;
    }

    public void setAttitude(Double attitude) {
        this.attitude = attitude;
    }

    public Double getOverall() {
        return overall;
    }

    public void setOverall(Double overall) {
        this.overall = overall;
    }

    public Integer getTotalReviewed() {
        return totalReviewed;
    }

    public void setTotalReviewed(Integer totalReviewed) {
        this.totalReviewed = totalReviewed;
    }
}
