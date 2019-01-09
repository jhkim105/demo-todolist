var _baseUrl = "tasks";
var _currentTaskId = '';
var _currentPageNumber = 0;

$(function() {
  $('#items_per_page').val($('#pageSize').val());

  $('#pageSize').change(function(){
    var val = $(this).val();
    $('#items_per_page').val(val);
    renderTable(0);
  });

  // close
  $('#table-tasks').on('click', '.btn-close-task', function() {
    var taskId = $(this).attr('taskid');
    console.log(taskId);

    $.ajax({
      type: 'POST',
      url: _baseUrl + '/' + taskId + '/close',
      dataType: 'json'
    }).done(function () {
      alert("SUCCESS");
      renderTable(_currentPageNumber);
    }).fail(function (xhr) {
      console.log(xhr);
      var message = '';
      if (xhr.responseJSON && xhr.responseJSON.message) {
        message = xhr.responseJSON.message;
      }
      alert("ERROR::" + message);
    });
  });

  // reopen
  $('#table-tasks').on('click', '.btn-open-task', function() {
    var taskId = $(this).attr('taskid');
    console.log(taskId);

    $.ajax({
      type: 'POST',
      url: _baseUrl + '/' + taskId + '/open',
      dataType: 'json'
    }).done(function () {
      alert("SUCCESS");
      renderTable(_currentPageNumber);
    }).fail(function (xhr) {
      var message = '';
      if (xhr.responseJSON && xhr.responseJSON.message) {
        message = xhr.responseJSON.message;
      }
      alert("ERROR::" + message);
    });
  });


  // search
  $('#btn-search').click(function() {
    renderTable(0);
  });

  $('#q').keyup(function (e){
    if (e.keyCode == 13)
      renderTable(0);
  });

  // create-form
  $('#btn-create').click(function(){
    _currentTaskId = '';
    _currentPageNumber = 0;
    setTaskForm('', '');
    event.preventDefault();
    $('#popup-task-form').modal('show');
    $('#btn-task-delete').removeClass('visible');
    $('#btn-task-delete').addClass('invisible');
  });

  // update-form
  $('#table-tasks').on('click', 'a.task-desc', function(event) {
    _currentTaskId = $(this).attr('taskid');

    $.get( _baseUrl + '/' + _currentTaskId)
      .done(function(data) {
        setTaskForm(data.description, data.superTaskIdsLabel);
        event.preventDefault();
        $('#popup-task-form').modal('show');
        $('#btn-task-delete').removeClass('invisible');
        $('#btn-task-delete').addClass('visible');
    });

  });


  $('#popup-task-form').on('show.bs.modal', function () {
  });

  $('#popup-task-form').on('hidden.bs.modal', function () {
    // clear form
    _currentTaskId = '';
    setTaskForm('', '');

    renderTable(_currentPageNumber);
  });

  // save task
  $('#btn-task-save').click(function(){
    var description = $('#taskForm #description').val();
    var superTaskIdsLabel = $('#taskForm #superTaskIdsLabel').val();
    console.log(_currentTaskId, description, superTaskIdsLabel);
    var data = {
      'id': _currentTaskId,
      'description': description,
      'superTaskIdsLabel': superTaskIdsLabel
    };

    var httpMethod = _currentTaskId === '' ? 'POST' : 'PUT';
    $.ajax({
      type: httpMethod,
      url: _baseUrl,
      data: JSON.stringify(data),
      contentType: 'application/json; charset=UTF-8',
      dataType: 'json'
    }).done(function (){
      alert("SUCCESS");
    }).fail(function (xhr){
      var message = '';
      if (xhr.responseJSON && xhr.responseJSON.message) {
        message = xhr.responseJSON.message;
      }
      alert("ERROR::" + message);
    });
  });

  // save task
  $('#btn-task-delete').click(function(){

    $.ajax({
      type: 'DELETE',
      url: _baseUrl + "/" + _currentTaskId,
      contentType: 'application/json; charset=UTF-8',
      dataType: 'json'
    }).done(function (){
      alert("SUCCESS");
    }).fail(function (xhr){
      console.log(xhr);
      var message;
      if (xhr.responseJSON && xhr.responseJSON.message) {
        message = xhr.responseJSON.message;
      }
      if (message)
        alert("ERROR::" + message);
    });
  });


  renderTable(0);

});

function renderTable(pageNumber) {
  $('#tableBody').html('');
  $('#current_page').val(pageNumber);
  _currentPageNumber = pageNumber;
  var options = getOptionsFromForm();
  getTasks(options.items_per_page, pageNumber, function(data) {
    drawTable(data);
    $("#Pagination").pagination(data.totalElements, options);
  });
}

function setTaskForm(description, superTaskIdsLabel) {
  $('#taskForm #description').val(description);
  $('#taskForm #superTaskIdsLabel').val(superTaskIdsLabel);
}


function addRow(task) {
  var tableBodyContent = $('#tableBody').html();
  var closedLabel;
  if (task.closed) {
    closedLabel = "Closed";
  } else {
    closedLabel = "Open";
  }

  console.log(task.superTaskIds);
  var superTaskHtml = '';
  if (task.superTaskIds) {
    task.superTaskIds.forEach(function (superTaskId){
      var taskId = superTaskId.slice(1);
      if (superTaskHtml !== '')
        superTaskHtml += ', ';
      superTaskHtml += '<a href="#" class="task-desc" taskid="' + taskId + '">' + superTaskId + '</a>';
    });
  }


  tableBodyContent += '<tr class="odd gradeX">';
  tableBodyContent += '<td class="text-center">' + task.id + '</td>';
  tableBodyContent += '<td class="text-center"><a href="#" class="task-desc" taskid="' + task.id + '">' + task.description + '</a></td>';
  tableBodyContent += '<td class="text-center">' + superTaskHtml + '</td>';
  tableBodyContent += '<td class="text-center">' + task.createdAt + '</a></td>';
  tableBodyContent += '<td class="text-center">' + task.updatedAt + '</td>';
  tableBodyContent += '<td class="text-center">' + closedLabel + '</td>';
  if (task.closed) {
    tableBodyContent += '<td class="text-center"><button type="button" class="btn btn-default btn-sm btn-open-task"  taskid="' + task.id + '">Reopen</button></td>';
  } else {
    tableBodyContent += '<td class="text-center"><button type="button" class="btn btn-default btn-sm btn-close-task"  taskid="' + task.id + '">Close</button></td>';
  }
  tableBodyContent += '</tr>';
  $('#tableBody').html(tableBodyContent);
}

function getTasks(pageSize, pageNumber, callback) {
  var q = $('#q').val();
  var open = false;
  if ($("#checkbox-open").is(':checked')) {
    open = true;
  }
  var searchParam = {'page': pageNumber, 'size': pageSize, 'order': 'ID', 'direction': 'DESC', 'q': q, 'open': open};

  $.ajax({
    type : 'get',
    async : false,
    url : 'tasks',
    data : searchParam,
    traditional: true,
    dataType : 'json'
  }).done(function(data) {
    console.log(data);
    callback(data);
  });
}

function drawTable(data) {
  console.log(data);
  var page = data.number;
  var pageSize = data.size;
  var list = data.content;
  console.log(list);
  $('#tableBody').html('');
  if (list) {
    for (var i = 0; i < list.length; i++) {
      var task = {
        idx : page * pageSize + i + 1,
        description : list[i].description,
        superTaskIdsLabel : list[i].superTaskIdsLabel,
        superTaskIds : list[i].superTaskIds,
        createdAt : list[i].createdAt,
        updatedAt : list[i].updatedAt,
        id : list[i].id,
        closed : list[i].closed
      };
      console.log(task);
      addRow(task);
    }
  }
}

function getTasksAndWriteTableBody(pageSize, pageNo) {
  getTasks(pageSize, pageNo, function(data) {
    drawTable(data);
  });
}

function pageSelectCallback(page_index, jq) {
  _currentPageNumber = page_index;
  var items_per_page = $('#items_per_page').val();
  console.log('pageSelectCallback::items_per_page:', items_per_page);
  getTasksAndWriteTableBody(items_per_page, page_index);
  return false;
}

function getOptionsFromForm() {
  var opt = {
    callback : pageSelectCallback
  };

  $("input:hidden").each(function() {
    opt[this.name] = this.className.match(/numeric/) ? parseInt(this.value) : this.value;
  });

  return opt;
}
