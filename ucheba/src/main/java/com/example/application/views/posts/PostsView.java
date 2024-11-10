package com.example.application.views.posts;

import com.example.application.Model.Controller;
import com.example.application.localdata.Post;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@PageTitle("Posts")
@Menu(icon = "line-awesome/svg/list-solid.svg", order = 4)
@Route(value = "posts")
public class PostsView extends Composite<VerticalLayout> {

    Grid<Post> grid = new Grid<>();
    Controller db = new Controller();

    public PostsView(String navigationPage) throws SQLException {

        afterNavigation();

        addClassName("posts-view");
        getContent().setSizeFull();

        getStyle().set("margin", "0");
        getStyle().set("padding", "0");
        getContent().setMinHeight("100px");

        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(person -> createCard(person));

        grid.getStyle().set("margin", "0");
        grid.getStyle().set("padding", "0");
        grid.getStyle().set("background-color", "#E6E9ED");

        grid.addItemClickListener(event -> {
            Post post = event.getItem();
            VaadinSession.getCurrent().setAttribute("post", post);
            getUI().ifPresent(ui -> ui.navigate(navigationPage));
        });

        getContent().add(grid);
    }

    private HorizontalLayout createCard(Post person) {

        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);

        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");

        H3 name = new H3(person.getHeading());
//        name.addClassName("name");
        LocalDateTime createdAt = person.getCreatedAt();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = createdAt.format(formatter);
        Span date = new Span(formattedDate);
        date.addClassName("date");
        Avatar avatar = new Avatar();
        avatar.setHeight("50px");
        avatar.setWidth("50px");
        avatar.setName(person.getAuthor());
        avatar.getStyle().set("background-color", person.getContent()); //цвет от авы пользователя
        header.add(avatar, name, date);
        header.getStyle().set("display", "flex");
        header.getStyle().set("align-items", "flex-start"); // Центрируем элементы по горизонтали

       header.setWidthFull(); // Устанавливаем ширину контейнера на 100%

        Span post = new Span(person.getContent());
//        post.addClassName("post");/

//        HorizontalLayout actions = new HorizontalLayout();
//        actions.addClassName("actions");
//        actions.setSpacing(false);
//        actions.getThemeList().add("spacing-s");
//
//        Icon likeIcon = VaadinIcon.HEART.create();
//        likeIcon.addClassName("icon");
//        Span likes = new Span(person.getLikes());
//        likes.addClassName("likes");
//        Icon commentIcon = VaadinIcon.COMMENT.create();
//        commentIcon.addClassName("icon");
//        Span comments = new Span(person.getComments());
//        comments.addClassName("comments");
//        Icon shareIcon = VaadinIcon.CONNECT.create();
//        shareIcon.addClassName("icon");
//        Span shares = new Span(person.getShares());
//        shares.addClassName("shares");
//
//        actions.add(likeIcon, likes, commentIcon, comments, shareIcon, shares);

        description.add(header, post);
        card.add(description);
        return card;
    }


    public void afterNavigation() {

        // Set some data when this view is displayed.
        List<Post> persons = db.getAllPosts();

        grid.setItems(persons);
    }
}
