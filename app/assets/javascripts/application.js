$(function() {

    $('.event-click').on('click', function(e) {
        e.preventDefault();

        var eventId = $(this).data('event-id');
        var attend = $(this).data('event-value');
        console.log(eventId);
        console.log(attend);

        var dataObject = {};
        dataObject.event = eventId;
        dataObject.attend = attend;

        var jqhxr = $.ajax({
            url: "/events/attend",
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(dataObject),
            dataType: 'json'
        }).done(function () {
        }).always(function () {
            console.log("Done");
            var target = $('a#event-'+eventId);
            var className = 'alert';
            if(attend) {
                target.removeClass(className);
                target.text("Skal");
            } else if(!attend) {
                target.addClass(className);
                target.text("Skal ikke");
            }
        });
    });
});

function admin() {

    $('.email-check-form').submit(function (e) {
        e.preventDefault();
        var form = $(this);
        var email = form.find($('.user-email')).val() + form.find($('.email-postfix')).val();
        console.log(email);
        var dataObject = {};
        dataObject.email = email;

        $.ajax({
            url: "/users/find",
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(dataObject),
            dataType: 'json',

            statusCode: {
                200: function () {
                    console.log("It is done");
                    form.unbind('submit');
                    form.submit();
                },
                404: function () {
                    alert("Ukjent bruker");
                }
            }
        });
    });

    $(document).on('click', '.delete-init-group', function (e) {
        e.preventDefault();

        var dataObject = {};

        dataObject.location = $(this).data('init-location');
        dataObject.guardian = $(this).data('init-guardian');

        $.ajax({
            url: "/admin/guardian",
            type: 'DELETE',
            contentType: 'application/json',
            data: JSON.stringify(dataObject),
            dataType: 'json',

            statusCode: {
                200: function () {
                    location.reload();
                },
                404: function () {
                    alert("Ukjent faddergruppe");
                }
            }
        });
    });

    $('.updateLocation').submit(function (e) {
        e.preventDefault();
        var jsonObject = {};
        var htmlResponse = $(document).find('.updateLocationMessage');
        htmlResponse.html('');
        var message;

        $(document).find('.updateLocation').each(function () {

            var locationId = $(this).find('.locationId').val();
            var locationName = $(this).find('.locationName').val();

            console.log("locationId: " + locationId);
            console.log("locationname: " + locationName);

            jsonObject[locationId] = locationName;
        });

        var xHttp = $.ajax({
            url: "/admin/site",
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(jsonObject),
            dataType: 'json',
            statusCode: {
                200: function (jqxhr) {
                    message = "<div data-alert class=\"alert-box success text-center radius\">" +
                        jqxhr.responseText + "<a href=\"#\" class=\"close\">&times;</a></div>";
                },
                400: function () {
                    message = "<div data-alert class=\"alert-box alert text-center radius\">" +
                        jqxhr.responseText + "<a href=\"#\" class=\"close\">&times;</a></div>";
                }
            }
        }).always(function () {

            $.get("/admin/site", function () {

                htmlResponse.append(message);
            });
        });

        console.log(xHttp);
    });

    // does not work yet
    $('.submitDeleteGuardian').on('click', function () {

        var htmlElement = $(document).find('.responseShow');
        var guardianHtml = $(document).find('.displayGuardian');
        var buttonHtml = $(this).parent('.small-3.columns');

        var guardianId = $(this).data('guardian-id');
        var guardianGroupLocation = $(this).data('guardian-group');

        var dataObject = {};

        dataObject.guardian = guardianId;
        dataObject.location = guardianGroupLocation;

        console.log(dataObject);
        var message;

        $.ajax({
            url: "/admin/guardian",
            type: 'DELETE',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(dataObject),
            statusCode: {
                200: function (jqxhr) {
                    message = "<div data-alert class=\"alert-box success text-center radius\">" +
                        jqxhr.responseText + "<a href=\"#\" class=\"close\">&times;</a></div>";
                },
                400: function () {
                    message = "<div data-alert class=\"alert-box alert text-center radius\">" +
                        jqxhr.responseText + "<a href=\"#\" class=\"close\">&times;</a></div>";
                }
            }
        }).always(function () {


            $.get("/admin/site", function () {

                htmlElement.append(message);
                guardianHtml.html('');
                buttonHtml.html('');
            });
        });
    });

    $('.submitDeleteClub').on('click', function (e) {

        e.preventDefault();

        var dataObject = {};

        dataObject.confirmDelete = $(document).find('.confirmDelete').val();

        var something = $('.clubDropDownList').find(':selected');
        var clubName = something.data('club-name');
        dataObject[clubName] = something.data('club-id');
        console.log(dataObject);

        var message;
        var htmlContent = $(document).find('.deleteModal');

        $.ajax({
            url: "/admin/site/delete",
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(dataObject),
            dataType: 'json',
            statusCode: {
                200: function (jqxhr) {
                    message = "<div data-alert class=\"alert-box success text-center radius\">" +
                        jqxhr.responseText + "<a href=\"#\" class=\"close\">&times;</a></div>";
                },
                400: function (jqxhr) {
                    message = "<div data-alert class=\"alert-box alert text-center radius\">" +
                        jqxhr.responseText + "<a href=\"#\" class=\"close\">&times;</a></div>";
                }
            }
        }).always(function () {

            $.get("/admin/site", function (data) {

                console.log(message);
                htmlContent.find('.insertCallback').html(message);
            });
        });
    });
}

function powerup() {

    $(document).on('click', '.updatepowerup', function (e) {
        e.preventDefault();
        var powerup = buildAndReturnPowerup($(this));
        powerup.update();

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

function createStandardButton(powerupId, buttonText) {
    if(typeof(buttonText) === undefined || buttonText === null) {
        buttonText = "Save Changes";
    }

    var htmlPowerup = getPowerupHtml(powerupId);
    var insertInto = htmlPowerup.find('.powerup-content');
    insertInto.append("<button class=\"updatepowerup button small radius\">" + buttonText + "</button>");
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
    this.prependMessage = prependMessage;

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
        var club = (this).club;
        var url = "/clubs/" + club + "/" + id;
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
                    invalidate(powerupContent, message, club, id);
                },
                401: function(jqxhr) {
                    console.log(jqxhr);
                    message = "<div data-alert class=\"alert-box alert text-center radius\">" + jqxhr.responseText + "<a href=\"#\" class=\"close\">&times;</a></div>";
                    invalidate(powerupContent, message, club, id);
                },
                400: function(jqxhr){
                    console.log(jqxhr);
                    message = "<div data-alert class=\"alert-box alert text-center radius\">" + jqxhr.responseText + "<a href=\"#\" class=\"close\">&times;</a></div>";
                    invalidate(powerupContent, message, club, id);
                },
                412: function(jqxhr) {
                    message = "<div data-alert class=\"alert-box alert text-center radius\">" + jqxhr.responseText + "<a href=\"#\" class=\"close\">&times;</a></div>";
                    prependMessage(powerupContent, message);
                }
            }
        });
    }

    function invalidate(powerupHtml, message, clubId, powerupId) {
        var url = "/clubs/" + clubId + "/" + powerupId;
        $.get(url, function (data) {
            console.log(powerupHtml.html());
            powerupHtml.html(message + data);
        });
    }

    function prependMessage(powerupHtml, message) {
        powerupHtml.prepend(message);
    }

}

function initiationSearch() {

    $(document).on('click', '.initiation-search', function(e) {
        e.preventDefault();
        var parent = $(this).closest('.location-container');
        var locationId = parent.data('location-id');
        var input = parent.find('.initiation-input').val();
        if(input === "") {
            return false;
        }
        var target = parent.find('.guardian-container');
        var dataObject = {};
        dataObject.location = locationId;
        dataObject.query = input;

        $.ajax({
            url: "/initiation",
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(dataObject),
            dataType: 'json',

            statusCode: {
                200: function (data) {
                    target.replaceWith(data.responseText);
                },
                404: function(data) {
                    target.replaceWith(data.responseText);
                }
            }
        });
    });
}

console.log("Hello!");
