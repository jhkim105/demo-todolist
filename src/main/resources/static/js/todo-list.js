
$(function() {

  // close
  $('#table-tasks').on('click', '.btn-close-task', function() {
    var taskId = $(this).attr('taskid');
    console.log(taskId);

    $.ajax({
      type: 'POST',
      url: 'tasks/' + taskId + '/close',
      dataType: 'json'
    }).done(function (result) {
      alert("SUCCESS");
      renderTable(0);
    }).fail(function (xhr) {
      console.log(xhr);
      if (xhr.responseJSON && xhr.responseJSON.message) {
        alert(xhr.responseJSON.message);
      }
    });
  });


  // search
  $('#btn-search').click(function() {
    renderTable(0);
  });

  // update-form
  $('#table-tasks').on('click', 'a.task-desc', function(event) {
    console.log('update');
    event.preventDefault();
    $('#popup-task-form').modal('show');
  });


  $('#popup-task-form').on('show.bs.modal', function () {
  });

  $('#popup-task-form').on('hidden.bs.modal', function () {
    renderTable(0);
  });

  renderTable(0);

});

function renderTable(pageNumber) {
  var optInit = getOptionsFromForm();
  getTasks(optInit.items_per_page, pageNumber, function(data) {
    drawTable(data);
    $("#Pagination").pagination(data.totalElements, optInit);
  });
}


function addRow(task) {
  var tableBodyContent = $('#tableBody').html();
  tableBodyContent += '<tr class="odd gradeX">';
  tableBodyContent += '<td class="text-center">' + task.idx + '</td>';
  tableBodyContent += '<td class="text-center"><a href="#" class="task-desc" taskid="' + task.id + '">' + task.description + '</a></td>';
  tableBodyContent += '<td class="text-center">' + task.createdAt + '</a></td>';
  tableBodyContent += '<td class="text-center">' + task.updatedAt + '</td>';
  tableBodyContent += '<td class="text-center">' + task.closed + '</td>';
  tableBodyContent += '<td class="text-center"><button type="button" class="btn btn-default btn-sm btn-close-task"  taskid="' + task.id + '">Close</button></td>';
  tableBodyContent += '</tr>';
  console.log(tableBodyContent);
  $('#tableBody').html(tableBodyContent);
}

function getTasks(pageSize, pageNumber, callback) {
  var searchParam = {'page': pageNumber, 'size': pageSize};

  $.ajax({
    type : 'get',
    async : false,
    url : 'tasks',
    data : searchParam,
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
