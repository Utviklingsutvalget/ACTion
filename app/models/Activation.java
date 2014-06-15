package models;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import play.db.ebean.Model;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Activation extends Model {

    @EmbeddedId
    public ActivationKey key;

    @ManyToOne
    public Club club;

    @ManyToOne
    public Plugin plugin;

    @Embeddable
    public class ActivationKey {

        public String pluginId;

        public Long clubId;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else {
                return o instanceof ActivationKey &&
                        ((ActivationKey) o).pluginId.equals(this.pluginId) &&
                        ((ActivationKey) o).clubId.equals(this.clubId);
            }
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder()
                    .append(getClass().getName())
                    .toHashCode();
        }

    }
}
