var imageCounter = 0;
var THUMBNAILHEIGHT = 150;
var THUMBNALWIDTH = 150;

$(document).ready(function(){

    var userID = $("#imageUpload").find("#userID").val();
    var intent = $("#imageUpload").find("#intent").val();
    console.log("userID: " + userID);
    console.log("intent: " + intent);

    var location = window.location.pathname;

    // calls file input with coverbutton
    $("#fileInputButton").on('click', function(){

        $("#fileInputField").click();
    });

    // called when user has selected file from wizard
    $("#fileInputField").change(function(e){

        var form = document.getElementById('imageUpload');
        sendToServer(form);
        resetXHRR();
        /*
        var form = $('#imageUpload');
        var formData = new FormData(form);
        var fileElement = document.getElementById('fileInputField');
        var file = $('#fileInputField');
        formData.append('file', file.files[0]);

        $.ajax({
            url: "/upload",
            type: "GET",
            data: formData,
            mimeType: "multipart/form-data",
            contentType: false,
            processData: false,
            success: function(){
                alert("hei");
            }
        });
        */

        $.get("/upload", {userID: userID, intent: intent}).done(function(data){

            resetExistingImages();

            $.each(data, function(key, value){

                console.log(value.id);
                imageCountIncrease();
                appendImage(value);
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

                    resetExistingImages();

                    $.each(data, function(key, value){

                        imageCountIncrease();
                        appendImage(value);
                    });
                });
            });
    });

    // modal is opened
    $("#uploadModal").bind('opened', function(){

        var intent = $(this).find('#intent').val();
        var userID = $(this).find('#userID').val();
        var clubID = $(this).find('#clubID').val();
        var feedID = $(this).find('#feedID').val();

        $.get("/upload", {url: location, userID: userID, intent: intent, feedID: feedID,
            clubID: clubID}).done(function(data){

            resetExistingImages();

            $.each(data, function(key, value){

                imageCountIncrease();
                appendImage(value);
            });
        });

        console.log("opened!");
    });

    // modal is closed
    $("#uploadModal").bind('closed', function(){

        resetExistingImages();
        resetXHRR();
        console.log("closed!");
    });
});

// unused so far
function test(){
    var uploadModal = $(document).find('#uploadModal');
    var intent = $(uploadModal).find('#intent').val();
    var userID = $(uploadModal).find('#userID').val();
    var clubID = $(uploadModal).find('#clubID').val();
    var feedID = $(uploadModal).find('#feedID').val();

    $.get("/upload", {url: location, userID: userID, intent: intent, feedID: feedID,
        clubID: clubID}).done(function(data){

        $.each(data, function(key, value){

            imageCountIncrease();
            appendImage(value);
        });
    });
}

function appendButton(dbId){

    //var button = "<div id=\"value.id\" class=\"button deleteExisitingFile\">Slett</div>";
    var button = document.createElement("div");
    $(button).addClass('button deleteExisitingFile');
    $(button).attr('id', dbId);
    $(button).text('delete');
    return button;
}

function appendImage(value){

    var imgNode = document.createElement("div");
    $(imgNode).addClass('imgNode');
    document.getElementById("existingImages").appendChild(imgNode);

    /*
    console.log("val.size: " + value.size);
    console.log("val.name: " + value.name);
    console.log("val.url: " + value.url);
    console.log("value.id: " + value.id);
    console.log("value.serverfilepath: " + value.serverfilepath);
    */

    var button = appendButton(value.id);

    var image = document.createElement("img");
    image.src = value.url;
    image.width = THUMBNAILHEIGHT;
    image.height = THUMBNALWIDTH;
    image.id = value.id;
    imgNode.appendChild(image);
    imgNode.appendChild(button);
}

function resetExistingImages(){

    console.log($('#existingImages'));
    $("#existingImages").html('');
}

function imageCountIncrease(){
    imageCounter++;
}

function sendToServer(form){

    var formData = new FormData(form);
    var fileElement = document.getElementById('fileInputField');
    var message;
    var FILE_FIELD = "file";

    formData.append(FILE_FIELD, fileElement.files[0]);

    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/upload', true);
    xhr.send(formData);

    xhr.onreadystatechange = function(){

        if(xhr.status == 200 && xhr.readyState == 4){

            message = "<div data-alert class=\"alert-box success text-center radius\">"
                + xhr.responseText + "<a href=\"#\" class=\"close\">&times;</a></div>";

        }else if(xhr.status == 400 && xhr.readyState == 4){

            message = "<div data-alert class=\"alert-box alert text-center radius\">"
                + xhr.responseText + "<a href=\"#\" class=\"close\">&times;</a></div>";
        }

        console.log(message);
        appendXHRResponse(message);
    };
}

function resetXHRR(){
    var htmlElement = $(document).find('#xhrResponse');
    htmlElement.html('');
}

function appendXHRResponse(message){

    var htmlElement = $(document).find('#xhrResponse');
    htmlElement.html('');
    htmlElement.append(message);
}
