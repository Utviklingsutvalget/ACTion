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
}