package UTCN_IMDB.demo.model;

import java.util.List;

public class ActorRoles {
    private Person actor;
    private List<Role> roles;

    public ActorRoles(Person actor, List<Role> roles) {
        this.actor = actor;
        this.roles = roles;
    }

    public Person getActor() {
        return actor;
    }

    public void setActor(Person actor) {
        this.actor = actor;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}