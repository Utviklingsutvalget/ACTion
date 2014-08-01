function powerup() {

    $(document).on('click', '.updatepowerup', function (e) {
        e.preventDefault();
        var powerup = buildAndReturnPowerup($(this));
        powerup.update()

    });

    $(document).on('click', '.updatepowerup-type', function(e) {
        e.preventDefault();
        var powerup = buildAndReturnPowerup($(this));
        var updateTypeElement = $(this).closest('.powerup-body');
        if(updateTypeElement.data('powerup-update-type') !== undefined) {
            powerup.updateType = updateTypeElement.data('powerup-update-type');
            console.log(powerup.updateType);
        }
        powerup.update();

    });
}

function buildAndReturnPowerup(fromElement) {
    var container = $(document).find('.powerup-container');
    var clubId = container.data('powerups-for');
    var powerupId = fromElement.closest('.powerup').data('powerup-id');

    var powerup = new PowerupClass();
    powerup.id = powerupId;
    powerup.club = clubId;
    return powerup;
}

function getPowerupHtml(id) {

    var container = $(document).find('.powerup-container');
    var htmlPowerups = container.find('.powerup');
    console.log(id);
    var htmlPowerup = null;
    htmlPowerups.each(function () {
        var tempPowerup = $(this);
        if (tempPowerup.data('powerup-id') === id) {
            htmlPowerup = $(this);
        }
    });
    return htmlPowerup;
}

function createStandardButton(powerupId) {
    var htmlPowerup = getPowerupHtml(powerupId);
    var insertInto = htmlPowerup.find('.powerup-content');
    insertInto.append("<button class=\"updatepowerup button small radius\">Save changes</button>");
}

function createUpdateTypeButton(powerupId, target) {
    var htmlPowerup = getPowerupHtml(powerupId);
    var htmlElement = htmlPowerup.find(target);
    htmlElement.append("<button class=\"updatepowerup-type button small radius\">Save changes</button>");
}

function getId(fromLocation) {
    return fromLocation.closest('.powerup').data("powerup-id");
}

function PowerupClass() {
    this.id = 0;
    this.club = 0;
    this.updateType = undefined;

    this.update = update;
    this.invalidate = invalidate;

    function update() {

        var dataObject = {};
        var newTitleNode = 'newTitleNode';
        var newTitleInput = '#newTitleInput';
        var applicationMessage = "#application_message";
        var appMessageNode = "appMessageNode";

        var powerupHtml = getPowerupHtml((this).id);
        console.log($(powerupHtml).prop('id'));
        var powerupContent = powerupHtml.find('.powerup-content');
        if((this).updateType !== undefined) {
            var updateType = (this).updateType;
            dataObject['update-type'] = updateType;
            powerupContent.find('.powerup-body').each(function() {
                console.log($(this));
               if($(this).data('powerup-update-type') === updateType) {
                   powerupContent = $(this);
                   return false;
               }
            });
        }

        powerupContent.find('.powerup-node').each(function () {

            if ($(this).hasClass('powerup-node')) {
                var field;
                if($(this).prop('id') === appMessageNode){

                    field = ($(this).find(applicationMessage).val());

                }else{

                    field = ($(this)).data('powerup-field');

                }
                var targetSelector;
                var targetObject;
                if ($(this).data('powerup-select') !== undefined) {

                    targetSelector = $(this).data('powerup-select');
                    targetObject = $(this).find(targetSelector);
                    var selectedValue = $('option:selected', targetObject);

                    console.log("targetselector: " + targetSelector);

                    dataObject[field] = selectedValue.val();

                } else if ($(this).data('powerup-value') !== undefined) {

                    dataObject[field] = $(this).data('powerup-value');

                } else if($(this).data('powerup-input') !== undefined) {
                    targetSelector = $(this).data('powerup-input');
                    targetObject = $(this).find(targetSelector);
                    dataObject[field] = targetObject.val();

                } else if($(this).data('powerup-checkbox') !== undefined) {
                    dataObject[field] = $(this).is(':checked');
                } else {
                    dataObject[field] = $(this).html();

                }
            }
        });
        console.log(dataObject);
        var message;
        var id = (this).id;
        var url = "/clubs/" + (this).club + "/" + id;
        powerupContent = getPowerupHtml(id).find('.powerup-content');
        $.ajax({
            url:  url,
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(dataObject),
            dataType: 'json',

            statusCode: {
                200: function(jqxhr) {
                    console.log(jqxhr);
                    message = "<div data-alert class=\"alert-box success text-center radius\">" + jqxhr.responseText + "<a href=\"#\" class=\"close\">&times;</a></div>";
                },
                401: function(jqxhr) {
                    console.log(jqxhr);
                    message = "<div data-alert class=\"alert-box alert text-center radius\">" + jqxhr.responseText + "<a href=\"#\" class=\"close\">&times;</a></div>";
                },
                400: function(jqxhr){
                    console.log(jqxhr);
                    message = "<div data-alert class=\"alert-box alert text-center radius\">" + jqxhr.responseText + "<a href=\"#\" class=\"close\">&times;</a></div>";
                }
            }
        }).always(function() {
            $.get(url, function (data) {
                console.log(powerupContent.html());
                powerupContent.html(message + data);
            });
        });

    }

    function invalidate() {

    }

}
