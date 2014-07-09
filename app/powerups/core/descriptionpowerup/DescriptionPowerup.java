package powerups.core.descriptionpowerup;

import models.Club;
import models.PowerupModel;
import play.twirl.api.Html;
import powerups.Powerup;
import powerups.core.descriptionpowerup.html.listdesc;
import powerups.core.descriptionpowerup.html.powerup;
import powerups.models.ClubDescription;

import static play.mvc.Results.ok;

/**
 * Specific implementation of the {@link powerups.Powerup} class. This powerup is used to show information about clubs
 * in the "about us" sections.
 */
public class DescriptionPowerup extends Powerup {

    /**
     * The model used by this powerup to hold information about descriptions.
     * @see powerups.models.ClubDescription
     */
    private final ClubDescription clubDesc;

    public DescriptionPowerup(Club club, PowerupModel model) {
        super(club, model);
        clubDesc = ClubDescription.find.byId(club.id);
    }

    @Override
    public Html render() {
        return powerup.render(clubDesc.description);
    }

    /**
     * Renders the shorter description to be used for the club list view. This is different from normal rendering
     * because we don't want the entire description to be shown in the list, nor do we want to cut off the actual
     * description at an arbitrary point.
     * @return The html to be inserted into the club list view.
     * @see views.html.club.index
     * @see views.html.club.list
     */
    public Html renderList() {
        return listdesc.render(clubDesc.listDescription);
    }
}
