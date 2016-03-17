$ ->
  $.get "/tasks", (tasks) ->
    $.each tasks, (index, task) ->
      $("#tasks").append $("<li>").text task.contents
