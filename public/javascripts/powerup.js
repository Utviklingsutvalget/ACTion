function powerup() {

    $(document).on('click', '.updatepowerup', function (e) {
        e.preventDefault();
        var container = $(document).find('.powerup-container');
        var clubId = container.data('powerups-for');
        var powerupId = $(this).closest('.powerup').data('powerup-id');

        console.log($(this).closest('.powerup-node'));

        var powerup = new PowerupClass();

        powerup.id = powerupId;
        powerup.club = clubId;
        console.log(powerup.id);
        console.log(powerup.club);
        powerup.update();
    });
}
function getPowerupHtml(id) {

    var container = $(document).find('.powerup-container');
    var htmlPowerups = container.find('.powerup');
    console.log(id);
    var htmlPowerup = null;
    htmlPowerups.each(function () {
        var tempPowerup = $(this);
        if (tempPowerup.data('powerup-id') === id) {
            console.log("FOUND THE POWER YAY");
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

function createButtonAt(powerupId, target) {
    var htmlPowerup = getPowerupHtml(powerupId);
    console.log(htmlPowerup);
    var htmlElement = htmlPowerup.find(target);
    console.log(htmlElement);
    htmlElement.append("<button class=\"updatepowerup button small radius\">Save changes</button>");
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

        var dataObject = {};
        var newTitleNode = 'newTitleNode';
        var newTitleInput = '#newTitleInput';
        var recruitPowerupNode = "recruitModal";
        var applicationMessage = "#application_message";
        var appMessageNode = "appMessageNode";

        var powerupHtml = getPowerupHtml((this).id);
        console.log(powerupHtml);
        var powerupContent = powerupHtml.find('.powerup-content');



        powerupContent.find('.powerup-node').each(function () {

            if ($(this).hasClass('powerup-node')) {

                if($(this).prop('id') === newTitleNode) {

                    var field = ($(this).find(newTitleInput).val());

                    console.log("success: " + field);

                }else if($(this).prop('id') === appMessageNode){

                    var field = ($(this).find(applicationMessage).val());

                }else{

                    var field = ($(this)).data('powerup-field');

                }

                if ($(this).data('powerup-select') !== undefined) {

                    var targetSelector = $(this).data('powerup-select');
                    var targetObject = $(this).find(targetSelector);
                    var selectedValue = $('option:selected', targetObject);

                    console.log("targetselector: " + targetSelector);

                    dataObject[field] = selectedValue.val();

                } else if ($(this).data('powerup-value') !== undefined) {

                    dataObject[field] = $(this).data('powerup-value');

                } else if($(this).data('powerup-input') !== undefined) {
                    var targetSelector = $(this).data('powerup-input');
                    var targetObject = $(this).find(targetSelector);
                    dataObject[field] = targetObject.val();

                } else {
                    dataObject[field] = $(this).html();

                }
            }
        });
        console.log(dataObject);

        var jqhxr = $.ajax({
            url:  "/clubs/" + (this).club + "/" + (this).id,
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