package controllers;

import com.avaje.ebean.annotation.Transactional;
import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.mvc.*;
import play.data.FormFactory;
import javax.inject.Inject;
import java.sql.Date;
import java.text.ParseException;
import java.util.List;
import static play.libs.Json.*;

import play.libs.ws.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class Application extends Controller  {

    @Inject
    FormFactory formFactory;

    public Result index() {
        return ok(views.html.index.render(play.data.Form.form(models.Collection.class),null,null));
    }

    @Transactional(readOnly = true)
    public Result getResultCollections() {
        List<ResultCollection> resultCollections = ResultCollection.find.all();
        return ok(toJson(resultCollections));
    }



    @Transactional(readOnly = true)
    public Result getCollections() {
        List<Collection> collections = Collection.find.all();
        return ok(toJson(collections));
    }


    public String saveResultCollection(JsonNode res,Collection collection)
    {
        String next_url="";
        if(res.get("pagination")!=null)
        {
            if(res.get("pagination").get("next_url")!=null)
            {
                next_url = res.get("pagination").get("next_url").asText();
            }
        }

        JsonNode data =  res.get("data");
        for(JsonNode coll : data) {

            String captionText = "";
            ResultCollection c1 = new ResultCollection();
            c1.setcName(collection.name);
            c1.setURLName(coll.get("link").asText());
            c1.setImg(coll.get("images").get("standard_resolution").get("url").asText());
            c1.setInstagramUsername(coll.get("user").get("username").asText());
            Long tagDate = coll.get("caption").get("created_time").asLong()*1000;
            Long startDate = collection.startDate.getTime();
            if(tagDate>=collection.startDate.getTime() && tagDate<=collection.endDate.getTime()) {
                if (coll.get("caption").get("text") != null) {
                    captionText = coll.get("caption").get("text").asText();
                }
                if (coll.get("caption").get("created_time") != null) {

                    Date dt = new Date(tagDate);


                    if (captionText.contains("#" + collection.hashTag)) {
                        c1.setTagTime(dt);
                    } else {
                        JsonNode comments = coll.get("comments").get("data");
                        for (JsonNode comment : comments) {
                            if ((comment.get("from").get("username").textValue()).equals(coll.get("user").get("username").asText())) {
                                if ((comment.get("text").textValue()).contains("#" + collection.hashTag)) {
                                    Long created = comment.get("created_time").longValue();
                                    dt = new Date(created * 1000);
                                    c1.setTagTime(dt);
                                }
                            }
                        }

                    }
                }
                c1.save();
            }
        }
        return next_url;
    }

    @Inject WSClient ws;
    public void callNextPage(Collection c,String next_url) throws InterruptedException,ExecutionException {
        boolean flag = true;
        while (flag) {
            if (next_url == "") {
                flag = false;
            } else {
                WSRequest request = ws.url(next_url);

                CompletionStage<JsonNode> output = request
                        .setRequestTimeout(-1).get()
                        .thenApply(response -> response.asJson());
                CompletableFuture<JsonNode> res1 = output.toCompletableFuture();
                JsonNode jArray = res1.get();
                next_url = saveResultCollection(jArray,c);
            }

        }
    }




    @Transactional
    public Result addCollection() throws ParseException, ExecutionException, InterruptedException
    {
        String next_url="";
        play.data.Form<models.Collection> form = play.data.Form.form(models.Collection.class).bindFromRequest();
        if (form.hasErrors()) {
            return redirect(routes.Application.index());
        }
        Collection collection = form.get();
        JsonNode res = printCollection(collection);
        next_url = saveResultCollection(res,collection);

        if(next_url!="") {
            callNextPage(collection,next_url);
        }


        collection.save();
        return redirect(routes.Application.index());
    }



    @Transactional
    public Result printCollectionHelper() {
        Collection collection = formFactory.form(Collection.class).bindFromRequest().get();


        return ok(
                views.html.index.render(play.data.Form.form(models.Collection.class),ResultCollection.page(0,collection.name),collection.name));

    }





    public Result list(int page,String filter) {
        return ok(
                views.html.index.render(play.data.Form.form(models.Collection.class),ResultCollection.page(page,filter),filter));

    }




    public CompletionStage<JsonNode> getCollectionHelper(Collection c,String feedUrl) {

        //feedUrl = "https://api.instagram.com/v1/users/55431/media/recent/?max_timestamp=1367432682&min_timestamp=1364840682&access_token=3028350201.1677ed0.ef9e83ac771b423593a82a61ab398e01&count=20";
        WSRequest request = ws.url(feedUrl);

        CompletionStage<JsonNode> output = request
                .setRequestTimeout(-1).get()
                .thenApply(response -> response.asJson());

        return output;

    }



    @Transactional
    public JsonNode printCollection(Collection c) throws InterruptedException,ExecutionException{
        String feedUrl = "https://api.instagram.com/v1/tags/"+c.hashTag+"/media/recent?access_token=3028350201.1677ed0.ef9e83ac771b423593a82a61ab398e01";
        CompletionStage<JsonNode> json = getCollectionHelper(c,feedUrl);
        CompletableFuture<JsonNode> res1 =json.toCompletableFuture();
        JsonNode jArray = res1.get();

    return jArray;
    }

}
