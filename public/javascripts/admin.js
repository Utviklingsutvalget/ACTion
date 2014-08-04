function admin() {

    $('.email-check-form').submit(function (e) {
        e.preventDefault();
        var form = $(this);
        var email = form.find($('.user-email')).val() + form.find($('.email-postfix')).val();
        console.log(email);
        var dataObject = {};
        dataObject['email'] = email;

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

    $(document).on('click', '.delete-init-group', function(e) {
        e.preventDefault();

        var dataObject = {};

        dataObject['location'] = $(this).data('init-location');
        dataObject['guardian'] = $(this).data('init-guardian');

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
            dataType: 'json'
        });

        console.log(xHttp);
    });

    $('.deleteClub').submit(function (e) {
        e.preventDefault();

        var dataObject = {};

        dataObject['confirmDelete'] = $(document).find('.confirmDelete').val();

        var deleteClubElement = $(document).find('.deleteClub');
        var clubId = $(deleteClubElement).find('.clubId').val();
        var clubName = $(deleteClubElement).find('.clubId').data('club-name');

        dataObject[clubName] = clubId;
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
                    message = "<div data-alert class=\"alert-box success text-center radius\">"
                        + jqxhr.responseText + "<a href=\"#\" class=\"close\">&times;</a></div>";
                },
                400: function (jqxhr) {
                    message = "<div data-alert class=\"alert-box alert text-center radius\">"
                        + jqxhr.responseText + "<a href=\"#\" class=\"close\">&times;</a></div>";
                }
            }
        }).always(function () {

            $.get("/admin/site", function (data) {

                console.log(message);
                htmlContent.find('label').html(message);
            });
        });
    });
}