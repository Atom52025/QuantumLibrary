package quantum.repository.projections;

import quantum.model.Group;

public interface UserGroupProjection {
    Long getId();
    Group getGroup();
    Boolean getAccepted();
    String getVoted();
}