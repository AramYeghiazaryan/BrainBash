let array = [];

let $selectDropDown = $('#categories');
$.get('/category', function (categories) {
    loadCategories(categories);
});

function loadCategories(categories) {
    loadSelectOptionsCategories($selectDropDown, categories);
}

function loadSubCategories() {
    $('#subCategoryDiv').remove();
    let categoryId = $selectDropDown.val();
    let $subCategoryDiv = $('<Div/>').attr('id', 'subCategoryDiv').attr('class', 'subCategoryNavigation');
    $('.navigation').append($subCategoryDiv);
    $.get('/subcategory/filter?categoryId=' + categoryId, function (subCategories) {

        $subCategoryDiv.empty();
        subCategories.forEach(function (subCategory) {

            let subCategoryLabel = $('<label/>').html(subCategory.typeName);
            let subCategoryCheckBox = $('<input/>').attr({
                type: 'checkBox'
            }).attr('subCategoryId', subCategory.id)
                .on('click', function () {
                    if ($(this).is(":checked")) {
                        let subCategoryId = subCategoryCheckBox.attr('subCategoryId');
                        let $topicDiv = $('<Div/>').attr('id', 'topicsDiv').attr('class', 'topicNavigation');
                        $.get('/topic?subCategoryId=' + subCategoryId, function (topics) {

                            if (!($.isEmptyObject(topics))) {
                                topics.forEach(function (topic) {
                                    let topicLabel = $('<label/>').html(topic.topicName);
                                    let topicCheckBox = $('<input/>').attr({
                                        type: 'checkbox'
                                    }).attr('topicId', topic.id);
                                    $topicDiv.prepend('<br/>');
                                    $topicDiv.prepend(topicLabel.prepend(topicCheckBox));
                                    topicCheckBox.on('click', function () {
                                        if ($(this).is(':checked')) {
                                            let Id = $(this).attr('topicId');
                                            array.push(parseInt(Id));
                                        }
                                    })
                                });
                            } else {
                                alert('topic is empty');
                                alert(subCategory.typeName + ' is empty');
                                subCategoryCheckBox.prop('checked', false);
                            }

                        });
                        subCategoryLabel.append($topicDiv);
                    } else {
                        let $topicDiv = $('#topicsDiv');
                        $topicDiv.remove();
                    }
                });
            $subCategoryDiv.prepend($('<br/>'));
            $subCategoryDiv.prepend(subCategoryLabel.prepend(subCategoryCheckBox));
        })
    })
}

function loadSelectOptionsCategories($select, categories) {
    categories.forEach(function (category) {
        let $option = $('<option/>', {
            value: category.id,
            text: category.type
        });
        $select.append($option);
    });
    $select.val([]);
}

function createTest() {


    let number = parseInt($('#questionNumber').val());
    let duration = parseInt($('#testDuration').val());
    let level = $('#level').val();
    let description = $("#testDescription").val();
    let name = $("#testName").val();

    let random = JSON.stringify({
        name: name,
        description: description,
        level: level,
        questionNumber: number,
        duration: duration,
        topicId: array
    });
    JSON.stringify(random);
    console.log(random);
    $.ajax({
        type: "POST",
        data: random,
        url: "https://brainbash.herokuapp.com/test/random/generate",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: (function () {
            alert('Question was successfully created');
        })
    })
}