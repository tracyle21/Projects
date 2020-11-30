package coms309.teambeans.Models;


import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(notes = "Name of Course Major", name = "majorName", required = true)
   private String majorName;

    @ApiModelProperty(notes = "Course Number for a Course", name = "number", required = true)
   private Integer number;

    @ApiModelProperty(notes = "Students currently taking the course", name = "users", required = true)
   @ManyToMany(mappedBy = "courseCurrentList")
   private Collection<User> users;

    @ApiModelProperty(notes = "Professors currently teaching the course", name = "users", required = true)
   @ManyToMany(mappedBy = "coursesTeaching")
   private Collection<User> professors;

    @ApiModelProperty(notes = "Teaching assistants helping with the course", name = "users", required = true)
   @ManyToMany(mappedBy = "coursesAssisting")
   private Collection<User> teachingAssistants;

    public Course() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getMajorName() {
        return majorName;
    }


    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public Integer getNumber() {
        return number;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public Collection<User> getProfessors() {
        return professors;
    }

    public void setProfessors(Collection<User> professors) {
        this.professors = professors;
    }

    public Collection<User> getTeachingAssistants() {
        return teachingAssistants;
    }

    public void setTeachingAssistants(Collection<User> teachingAssistants) {
        this.teachingAssistants = teachingAssistants;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Course(String majorName, Integer number) {
        this.majorName = majorName;
        this.number = number;
    }
}
