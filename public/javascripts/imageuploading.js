var THUMBNAILHEIGHT = 150;
var THUMBNALWIDTH = 150;

/**
 * Possibly change design to use classes outside the modal div itself and incorporate classes.
 * Then all div id identifiers within the modal can remain the same and only event handlers needs redesign
 * to trigger on classes.
 *
 * */

$(document).ready(function(){

    var imagePowerupType = undefined;

    // calls file input with coverbutton
    $(".fileInputButton").on('click', function(){

        var fileInputField = $(this).parent().find('.fileInputField');
        fileInputField.click();
    });

    // called when user has selected file from wizard
    $(".fileInputField").change(function(e){

        var uploadModal = $(this).closest('.uploadModal');

        // fetches data which determines field of choice for link output
        imagePowerupType = uploadModal.data('image-class');
        console.log(imagePowerupType);

        var existingImagesElement = $(this).closest(uploadModal).find('.existingImages');

        // fetches file object
        var fileElement = $(this)[0].files[0];
        var message;
        var FILE_FIELD = "file";

        var formData = new FormData();
        formData.append(FILE_FIELD, fileElement);

        $.ajax({
            url: "/upload",
            type: "POST",
            data: formData,
            mimeType: "multipart/form-data",
            contentType: false,
            processData: false,
            success: function(data){
                message = "<div data-alert class=\"alert-box success text-center radius\">"
                    + data + "<a href=\"#\" class=\"close\">&times;</a></div>";
            }

        }).done(function(){

            appendXHRResponse(message, uploadModal);

            $.get("/upload", {fileName: fileElement.name}).done(function(data){

                existingImagesElement.html('');

                $.each(data, function(key, value){

                    console.log(value.url);
                    appendUrlToField(value.url, imagePowerupType);
                    appendImage(value, existingImagesElement);
                });
            });
        });
    });

    // needs to be formatted accordingly to make sure events fire for dynamically created htmlelements
    // e.g deletebuttons
    $("#uploadModal").on('click', '.deleteExisitingFile', function(){

        var uploadModal = $(this).closest('#uploadModal');
        var intent = $(uploadModal).find('#intent').val();
        var userID = $(uploadModal).find('#userID').val();
        var clubID = $(uploadModal).find('#clubID').val();
        var feedID = $(uploadModal).find('#feedID').val();
        var existingImagesElement = $(uploadModal).find('.existingImages');
        var imageID = $(this).parent().find('img').attr('id');

        $.get("/deleteuploaded", {userID: userID, intent: intent, feedID: feedID, clubID: clubID, imageID: imageID},
            function(data){

            var message = "<div data-alert class=\"alert-box success text-center radius\">"
                    + data + "<a href=\"#\" class=\"close\">&times;</a></div>";
            appendXHRResponse(message);
        }).done(function(){

                // refetch images to display updated dir
                $.get("/upload", {userID: userID, intent: intent, feedID: feedID,
                    clubID: clubID}).done(function(data){

                    existingImagesElement.html('');

                    $.each(data, function(key, value){

                        appendImage(value);
                    });
                });
            });
    });

    // modal is opened
    $(".uploadModal").bind('opened', function(){

        var existingImagesElement = $(this).find('.existingImages');
        existingImagesElement.html('');
        resetXHRR(existingImagesElement.parent());

        console.log("opened!");
    });

    // modal is closed
    $(".uploadModal").bind('closed', function(){

        var exisitingImagesElement = $(this).find('.existingImages');
        exisitingImagesElement.html('');
        resetXHRR();
        console.log("closed!");
    });
});

function appendUrlToField(url, classToAppendTo){

    if(classToAppendTo === undefined){

        console.log("classToAppend is undefined for url: " + url);
        return;
    }

    $('.' + classToAppendTo).val(url);
}

function appendButton(dbId){

    //var button = "<div id=\"value.id\" class=\"button deleteExisitingFile\">Slett</div>";
    var button = document.createElement("div");
    $(button).addClass('button deleteExisitingFile');
    $(button).attr('id', dbId);
    $(button).text('delete');
    return button;
}

function appendImage(value, existingImageElements){

    var imgNode = document.createElement("div");
    $(imgNode).addClass('imgNode');

    $(existingImageElements).append(imgNode);

    var button = appendButton(value.id);

    var image = document.createElement("img");
    image.src = value.url;
    image.width = THUMBNAILHEIGHT;
    image.height = THUMBNALWIDTH;
    imgNode.appendChild(image);
    imgNode.appendChild(button);
}

function resetXHRR(parent){
    var htmlElement = $(parent).find('.xhrResponse');
    htmlElement.html('');
}

function appendXHRResponse(message, parent){
    var htmlElement = $(parent).find('.xhrResponse');

    htmlElement.html('');
    htmlElement.append(message);
}
