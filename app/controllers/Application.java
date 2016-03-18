package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.PagedList;
import com.avaje.ebean.annotation.Transactional;
import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.*;
import play.data.Form;
import play.db.Database;
import play.libs.F;
import play.libs.ws.WS;
import play.mvc.*;
import views.html.*;
import models.Person;
import play.data.FormFactory;
import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import static play.libs.Json.*;
import play.mvc.BodyParser;

import play.mvc.*;
import play.libs.ws.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import javax.inject.Inject;

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

    @Transactional
    public Result addCollection()
    {
        play.data.Form<models.Collection> form = play.data.Form.form(models.Collection.class).bindFromRequest();
        Collection collection = form.get();
        collection.save();
        return redirect(routes.Application.index());
    }


/*

    @Transactional
    public Result addPerson() {
        Person person = formFactory.form(Person.class).bindFromRequest().get();
        JPA.em().persist(person);
        return redirect(routes.Application.index());

    }


    @Transactional
    public Result addTask() {
        Task task = formFactory.form(Task.class).bindFromRequest().get();
        JPA.em().persist(task);
        return redirect(routes.Application.index());
    }

*/


    @Transactional
    public Result printCollectionHelper() throws ExecutionException, InterruptedException {
        Collection collection = formFactory.form(Collection.class).bindFromRequest().get();
        //Collection c = (models.Collection) JPA.em().createQuery("select c from Collection c  where c.name = ?1").setParameter(1,collection.name).getSingleResult();
        Collection cocoTasks = Collection.find.where()
                .ilike("name",collection.name)
                .findUnique();
        //List<Collection> collections = (List<Collection>) JPA.em().createQuery("select c from Collection c where c.name='abc'").getResultList();
        JsonNode res = printCollection(cocoTasks);
        //List<ResultCollection> collList = new ArrayList<ResultCollection>();
        for(JsonNode coll : res) {
            ResultCollection c1 = new ResultCollection();
            c1.setcName(collection.name);
            c1.setURLName(coll.get("link").toString());
            c1.setImg(coll.get("images").get("standard_resolution").get("url").toString());
            c1.setInstagramUsername(coll.get("user").get("username").toString());
            //collList.add(c1);
            c1.save();

        }
        //PagedList<ResultCollection> findPagedList = (PagedList<ResultCollection>) collList;
        //return ok(toJson(res));

        return ok(
                views.html.index.render(play.data.Form.form(models.Collection.class),ResultCollection.page(0,collection.name),collection.name));

    }





    public Result list(int page,String filter) {
        return ok(
                views.html.index.render(play.data.Form.form(models.Collection.class),ResultCollection.page(page,filter),filter));

    }



    @Inject WSClient ws;
    public CompletionStage<JsonNode> getCollectionHelper(Collection c) {


        String feedUrl = "https://api.instagram.com/v1/tags/"+c.hashTag+"/media/recent?access_token=3028350201.1677ed0.ef9e83ac771b423593a82a61ab398e01";
        WSRequest request = ws.url(feedUrl);


        CompletionStage<JsonNode> output = request
                .setRequestTimeout(-1).setQueryParameter("count","20").get()
                .thenApply(response -> response.asJson());

        return output;
        //return redirect(routes.Application.index());
        //return ok(toJson(jsonPromise));
    }



    @Transactional
    public JsonNode printCollection(Collection c) throws InterruptedException,ExecutionException{

        CompletionStage<JsonNode> json = getCollectionHelper(c);
        CompletableFuture<JsonNode> res1 =json.toCompletableFuture();
        JsonNode jArray = res1.get();
        JsonNode res2 = jArray.get("data");



    return res2;
    }

/*


    @Transactional(readOnly = true)
    public Result getTasks() {
        List<Task> tasks = (List<Task>) JPA.em().createQuery("select t from Task t").getResultList();
        return ok(toJson(tasks));
    }
*/


}
