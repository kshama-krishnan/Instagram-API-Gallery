$ ->
  $.get "/collections", (collections) ->
    $.each collections, (index, collection) ->
      $("#collection").append $("<option>").text collection.name

