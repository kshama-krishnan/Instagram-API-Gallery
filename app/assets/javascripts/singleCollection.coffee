$ ->
  $.get "/res", (res) ->
    $.each res, (index, single) ->
      $("#c").append $("<option>").text single.images.low_resolution.url