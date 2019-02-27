let answerCount = 0;

function addAnswer() {
    answerCount++;
    let $answerBlock = $('#answerBlock');
    let $answerSet = $('<div/>').attr('class', 'answerClass');
    let answerText = $('<textarea />').attr('row', '5').attr('column', '25').attr('type', 'text').attr('id', 'answerText');
    let answerDescription = $('<textarea />').attr('row', '5').attr('column', '25').attr('id', 'answerDescription');
    let isCorrect = $('<input/>').attr('type', 'checkbox').attr('id', 'isCorrect');
    let label = $('<label/>').html('Answer #' + answerCount + ': ');
    let descriptionLabel = $('<label/>').html(' Description: ');
    let isCorrectLabel = $('<label/>').html(' Is Correct ');
    $answerBlock.append($answerSet);
    $answerSet.append(answerText).append(descriptionLabel).append(answerDescription).append(isCorrect).append(label);
    $answerSet.prepend(label);
    $answerSet.append(isCorrectLabel);
}

function removeAnswer() {
    if (answerCount >= 1) {
        let divToRemove = $('.answerClass').last();
        divToRemove.remove();
        answerCount--;
    }
}

function createQuestion() {
    let topicId = parseInt($('#topics').val());
    let questionText = $('#questionText').val();
    let level = $('#level').val();
    let points = parseInt($('#points').val());
    let answerData = $('#answerBlock').find('.answerClass').map(function () {
        let answerText = $(this).find('#answerText').val();
        let answerDescription = $(this).find('#answerDescription').val();
        let isCorrect = $(this).find('#isCorrect').is(":checked");
        return {
            answer: answerText,
            description: answerDescription,
            correct: isCorrect
        }
    }).toArray();
    console.log(answerData);
    let question = JSON.stringify({
        topicId: topicId,
        question: questionText,
        level: level,
        points: points,
        answerDtoList: answerData
    });
    console.log(question);
    $.ajax({
        type: "POST",
        data: question,
        url: "/question/add",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: (function (result) {
            console.log(result);
            location.href = 'https://brainbash.herokuapp.com/question/add';
        })
    });
}

let $selectDropDown = $('#categories');
$.get('/category', function (categories) {
    loadCategories(categories);
});

function loadCategories(categories) {
    loadSelectOptionsCategories($selectDropDown, categories);
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

function loadSubCategoriesDropDown() {
    $('#subCategoryDiv').empty();
    let categoryId = $('#categories').val();
    $.get('/subcategory/filter?categoryId=' + categoryId, function (subCategories) {
        let $subCategoryDiv = $('<div/>').attr('id', 'subCategoryDiv');
        let $subCategorySelector = $('<select/>').attr('id', 'subCategories');
        subCategories.forEach(function (subCategory) {
            let $option = $('<option/>', {
                value: subCategory.id,
                text: subCategory.typeName
            });
            $subCategorySelector.append($option);
        });
        let subLabel = $('<label/>').html(' SubCategory: ');
        subLabel.append($subCategorySelector);
        $('.navigation').append($subCategoryDiv);
        $('#subCategoryDiv').append($('<br/>')).append(subLabel);
        $subCategorySelector.val([]);
        $subCategorySelector.attr('onchange', 'loadTopicsDropDown()');
    });
}

function loadTopicsDropDown() {
    $('#topicDiv').empty();
    let subCategoryId = $('#subCategories').val();
    $.get('/topic?subCategoryId=' + subCategoryId, function (topics) {
        let $topicDiv = $('<div/>').attr('id', 'topicDiv');
        let $topicSelector = $('<select/>').attr('id', 'topics');
        topics.forEach(function (topic) {
            let $option = $('<option/>', {
                value: topic.id,
                text: topic.topicName
            });
            $topicSelector.append($option);
        });
        let topicLabel = $('<label/>').html(' Topic: ');
        topicLabel.append($topicSelector);
        $('#subCategoryDiv').append($topicDiv);
        $('#topicDiv').append($('<br/>')).append(topicLabel);
        $topicSelector.val([]);
    });
}
