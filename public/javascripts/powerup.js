function powerup() {

    $(document).on('click', '.updatepowerup', function(e) {
        e.preventDefault();
        var container = $(document).find('.powerupContainer');
        var clubId = container.data('powerups-for');
        var powerupId = $(this).closest('.powerup').data('powerup-id');
        var powerup = new PowerupClass();
        powerup.id = powerupId;
        powerup.club = clubId;

        var powerupHtml = getPowerupHtml(powerupId);
        console.log(powerupHtml);
        var dataItems = {};
        powerupHtml.children().each(function() {
            console.log($(this));
            var fieldName = $(this).data('powerup-field');
            if(fieldName !== null) {
                console.log($(this).text());
                dataItems[fieldName] = $(this).text();
            }
        });
        console.log(dataItems);

        var jqhxr = $.ajax({
            url: window.location.pathname + "/" + powerupId,
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(dataItems),
            dataType: 'json'
        }).done(function() {
            console.log("Successfully sent AJAX");
        }).always(function() {
            console.log("Sent")
        });
        console.log(jqhxr);


    });
}
function getPowerupHtml(id) {
    var container = $(document).find('.powerupContainer');
    var htmlPowerups = container.find('.powerup');
    var htmlPowerup = null;
    htmlPowerups.each(function() {
        var tempPowerup = $(this);
        if(tempPowerup.data('powerup-id') === id) {
            htmlPowerup = $(this);
            console.log("Setting a powerup");
        }
    });
    return htmlPowerup;
}

function createButton(powerupId) {
    var htmlPowerup = getPowerupHtml(powerupId);
    var insertInto = htmlPowerup.find('.powerupContent');
    insertInto.append("<button class=\"updatepowerup button small radius\">Save changes</button>");
    //var scriptTag = insertInto.find('script');
    //scriptTag.remove();
    //console.log(scriptTag);
}

function getId(fromLocation) {
    return fromLocation.closest('.powerup').data("powerup-id");
}

function PowerupClass() {
    var id = 0;
    var club = 0;

    function update() {

    }

    function invalidate() {

    }

}