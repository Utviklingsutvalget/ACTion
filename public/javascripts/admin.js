function admin() {

    $(document).on('click', '.newclub', function(e) {
        var email = $('#leader-email').val() + $('#emailpostfix').val();
        console.log(email);

        var dataObject = {};
        dataObject['email'] = email;
        var jqhxr = $.ajax({
            url: "/users/find",
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(dataObject),
            dataType: 'json'
        }).always(function() {
            if(jqhxr.status !== 200) {
                e.preventDefault();
            }
        });
    });

    $('.updateLocation').submit(function(e){
        e.preventDefault();
        var jsonObject = {};

        $(document).find('.updateLocation').each(function(){

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

    $('.deleteClub').submit(function(e){
        e.preventDefault();

        var dataObject = {};

        var confirmDeleteClub = $(document).find('.confirmDelete').val();
        dataObject['confirmDelete'] = confirmDeleteClub;

        var deleteClubElement = $(document).find('.deleteClub');
        var clubId = $(deleteClubElement).find('.clubId').val();
        var clubName = $(deleteClubElement).find('.clubId').data('club-name');

        dataObject[clubName] = clubId;
        console.log(dataObject);

        $.ajax({
            url: "/admin/site/delete",
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(dataObject),
            dataType: 'json',
            statusCode: {
                200: function(){

                }
            }
        });

    });
}