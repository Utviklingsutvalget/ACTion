function powerup() {

    $(document).on('click', '.updatepowerup', function (e) {
        e.preventDefault();
        var container = $(document).find('.powerup-container');
        var clubId = container.data('powerups-for');
        var powerupId = $(this).closest('.powerup').data('powerup-id');
        var powerup = new PowerupClass();
        powerup.id = powerupId;
        powerup.club = clubId;

        powerup.update();
    });
}
function getPowerupHtml(id) {
    var container = $(document).find('.powerup-container');
    var htmlPowerups = container.find('.powerup');
    var htmlPowerup = null;
    htmlPowerups.each(function () {
        var tempPowerup = $(this);
        if (tempPowerup.data('powerup-id') === id) {
            htmlPowerup = $(this);
        }
    });
    return htmlPowerup;
}

function createButton(powerupId) {
    var htmlPowerup = getPowerupHtml(powerupId);
    var insertInto = htmlPowerup.find('.powerup-content');
    insertInto.append("<button class=\"updatepowerup button small radius\">Save changes</button>");
}

function getId(fromLocation) {
    return fromLocation.closest('.powerup').data("powerup-id");
}

function PowerupClass() {
    this.id = 0;
    this.club = 0;

    this.update = update;
    this.invalidate = invalidate;

    function update() {
        var powerupHtml = getPowerupHtml((this).id);
        var powerupContent = powerupHtml.find('.powerup-content');
        var dataObject = {};
        powerupContent.find('.powerup-node').each(function () {
            if ($(this).hasClass('powerup-node')) {
                var field = ($(this)).data('powerup-field');
                dataObject[field] = $(this).html();
            }
        });
        console.log(dataObject);

        var jqhxr = $.ajax({
            url: window.location.pathname + "/" + (this).id,
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(dataObject),
            dataType: 'json'
        }).done(function () {
        }).always(function () {
        });
        (this).invalidate();
    }

    function invalidate() {

    }

}