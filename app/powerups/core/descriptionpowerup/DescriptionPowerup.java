package powerups.core.descriptionpowerup;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.Club;
import models.PowerupModel;
import play.Logger;
import play.mvc.Result;
import play.twirl.api.Html;
import powerups.Powerup;
import powerups.core.descriptionpowerup.html.listdesc;
import powerups.core.descriptionpowerup.html.powerup;
import powerups.models.ClubDescription;
import utils.MembershipLevel;

import static play.mvc.Results.badRequest;
import static play.mvc.Results.internalServerError;
import static play.mvc.Results.ok;

/**
 * Specific implementation of the {@link powerups.Powerup} class. This powerup is used to show information about clubs
 * in the "about us" sections.
 */
public class DescriptionPowerup extends Powerup {

    public static final String FIELD_STRING = "description";
    /**
     * The model used by this powerup to hold information about descriptions.
     * @see powerups.models.ClubDescription
     */
    private final ClubDescription clubDesc;
    private final boolean editable;

    public DescriptionPowerup(Club club, PowerupModel model) {
        super(club, model);
        clubDesc = ClubDescription.find.byId(club.id);

        editable = this.getContext().getMemberLevel().getLevel() >= MembershipLevel.BOARD.getLevel();
    }

    @Override
    public Html render() {
        return powerup.render(clubDesc.description, editable, FIELD_STRING);
    }

    @Override
    public void activate() {
        ClubDescription clubDesc = new ClubDescription();
        clubDesc.clubId = this.getClub().id;
        Ebean.save(clubDesc);
    }

    @Override
    public Result update(JsonNode updateContent) {
        if(!updateContent.has(FIELD_STRING)) {
            return internalServerError("En feil har oppstått");
        } else if(updateContent.get(FIELD_STRING).asText().equals(clubDesc.description)) {
            return ok("Ingen endringer å lagre");
        } else if(this.editable) {
            this.clubDesc.description = updateContent.get(FIELD_STRING).asText();
            Ebean.save(clubDesc);
            return ok("Utvalgsbeskrivelse lagret!");
        } else return badRequest("Ingen lagringstilgang");
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
