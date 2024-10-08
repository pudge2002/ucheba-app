package com.example.application.views.account;

import com.example.application.views.main.MainView;
import com.example.application.views.navbars.desktopNav;
import com.example.application.views.navbars.mobileNav;
import com.example.application.views.registration.RegistrationView;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

@PageTitle("Account")
@Menu(icon = "line-awesome/svg/pencil-ruler-solid.svg", order = 5)
@Route(value = "account")
public class AccountView extends Composite<VerticalLayout>implements BeforeEnterObserver {
    H1 h1 = new H1();
    public AccountView() {
        HorizontalLayout layoutRow = new HorizontalLayout();
        VerticalLayout layoutColumn2 = new VerticalLayout();
        Avatar avatar = new Avatar();
        H2 h2 = new H2();
        H6 h6 = new H6();
        Button buttonSecondary = new Button();
        TabSheet tabSheet = new TabSheet();

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.CENTER);
        getContent().setAlignItems(Alignment.CENTER);
        h1.setText("Обложка");
        h1.setWidth("max-content");
        layoutRow.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("min-content");
        layoutRow.setHeight("min-content");
        layoutColumn2.setHeightFull();
        layoutRow.setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        avatar.setName("Menshev");
        avatar.setWidth("150px");
        avatar.setHeight("150px");
        h2.setText("Menshev");
        h2.setWidth("max-content");
        h6.setText("student");
        h6.setWidth("max-content");
        buttonSecondary.setText("Редактировать");
        buttonSecondary.getStyle().set("margin-top","20%");
        buttonSecondary.setWidth("min-content");
        getContent().setAlignSelf(FlexComponent.Alignment.CENTER, tabSheet);
        tabSheet.setWidth("min-content");
        tabSheet.getStyle().set("flex-grow", "1").set("width","100%");
        setTabSheetSampleData(tabSheet);


        getContent().add(layoutRow);
        layoutRow.add(layoutColumn2);
        layoutColumn2.add(avatar);
        layoutColumn2.add(h2);
        layoutColumn2.add(h6);
        layoutRow.getStyle().set("width", "90%");
        layoutRow.add(buttonSecondary);
        getContent().add(tabSheet);

        // Добавляем класс для стилизации
        getContent().addClassName("main-content");

        buttonSecondary.addClickListener(e -> {
            UI.getCurrent().navigate(AccountEditView.class);
        });
    }
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        boolean isMobile = DeviceUtils.isMobileDevice();
        if (isMobile) {
            getContent().add(new mobileNav());
        } else {
            getContent().add(new desktopNav());
        }
    }
    public class DeviceUtils {
        public static boolean isMobileDevice() {
            VaadinSession session = VaadinSession.getCurrent();
            String userAgent = session.getBrowser().getBrowserApplication();
            return userAgent.contains("Mobile") || userAgent.contains("Android") || userAgent.contains("iPhone");
        }
    }
    private void setTabSheetSampleData(TabSheet tabSheet) {
        Div postsDiv = new Div(new Text("Пока пусто"));
        postsDiv.addClassName("custom-div");
        tabSheet.add("Посты", postsDiv);

        Div questionsDiv = new Div(new Text("Пока пусто"));
        questionsDiv.addClassName("custom-div");
        tabSheet.add("Вопросы", questionsDiv);
    }
}
