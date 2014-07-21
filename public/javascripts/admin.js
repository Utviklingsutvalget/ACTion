function admin() {

    $(document).on('click', '.newclub', function(e) {
        e.preventDefault();
        var email = $('#leader-email').val();
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
                console.log("EMAIL NOT FOUND");
                e.preventDefault();
            } else {
                console.log("FOUND EMAIL");
            }
        });
    });
}