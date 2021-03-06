package powerups.core.descriptionpowerup;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.Club;
import models.PowerupModel;
import org.jsoup.Jsoup;
import play.mvc.Result;
import play.twirl.api.Html;
import powerups.Powerup;
import powerups.core.descriptionpowerup.html.admin;
import powerups.core.descriptionpowerup.html.listdesc;
import powerups.core.descriptionpowerup.html.powerup;
import powerups.models.ClubDescription;
import utils.MembershipLevel;

import static play.mvc.Results.*;

/**
 * Specific implementation of the {@link powerups.Powerup} class. This powerup is used to show information about clubs
 * in the "about us" sections.
 */
public class DescriptionPowerup extends Powerup {

    public static final String FIELD_STRING = "description";
    public static final String LIST_STRING = "listdesc";
    /**
     * The model used by this powerup to hold information about descriptions.
     *
     * @see powerups.models.ClubDescription
     */
    private final ClubDescription clubDesc;
    private final boolean editable;
    @Inject
    private DescriptionService descriptionService;

    public DescriptionPowerup(Club club, PowerupModel model) {
        super(club, model);
        clubDesc = descriptionService.findByClubId(club.getId());

        editable = this.getContext().getMemberLevel().getLevel() >= MembershipLevel.BOARD.getLevel();
    }

    @Override
    public Html render() {
        return powerup.render(clubDesc.getDescription());
    }

    @Override
    public Html renderAdmin() {
        return admin.render(clubDesc, FIELD_STRING, LIST_STRING);
    }

    @Override
    public void activate() {
        ClubDescription clubDesc = new ClubDescription();
        clubDesc.setClub(this.getClub());
        clubDesc.setDescription("");
        clubDesc.setDescription("");
        Ebean.save(clubDesc);
    }

    @Override
    public void deActivate() {
        Ebean.delete(clubDesc);
    }

    @Override
    public Result update(JsonNode updateContent) {
        if (!updateContent.has(FIELD_STRING)) {
            return internalServerError("En feil har oppstått");
        } else if (updateContent.get(FIELD_STRING).asText().equals(clubDesc.getDescription())
                && updateContent.get(LIST_STRING).asText().equals(clubDesc.getListDescription())) {
            return ok("Ingen endringer å lagre");
        } else if (this.editable) {
            this.clubDesc.setDescription(updateContent.get(FIELD_STRING).asText());
            if (updateContent.has(LIST_STRING)) {
                this.clubDesc.setListDescription(Jsoup.parse(updateContent.get(LIST_STRING).asText()).body().text());
            }
            Ebean.save(clubDesc);
            return ok("Utvalgsbeskrivelse lagret!");
        } else return badRequest("Ingen lagringstilgang");
    }

    /**
     * Renders the shorter description to be used for the club list view. This is different from normal rendering
     * because we don't want the entire description to be shown in the list, nor do we want to cut off the actual
     * description at an arbitrary point.
     *
     * @return The html to be inserted into the club list view.
     * @see views.html.club.index
     * @see views.html.club.list
     */
    public Html renderList() {
        return listdesc.render(clubDesc.getListDescription());
    }
}
