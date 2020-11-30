package coms309.teambeans.Models;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "language")
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(notes = "Name of Language", name = "name", required = true)
    private String name;

    @ApiModelProperty(notes = "Languages Known by Users", name = "users", required = true)
    @ManyToMany(mappedBy = "languages")
    Collection<User> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Language(String name) {
        this.name = name;
    }

    public Language() {
    }
}
