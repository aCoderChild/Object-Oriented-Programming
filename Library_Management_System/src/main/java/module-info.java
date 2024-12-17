module com.example.my_group_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.media;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    requires java.sql;
    requires mysql.connector.j;
    requires java.desktop;
    requires jakarta.mail;
    requires java.prefs;

    requires google.api.client;
    requires google.http.client;
    requires google.http.client.jackson2;
    requires google.api.services.books.v1.rev114;
    requires org.apache.commons.text;

    // Export packages to be accessible by FXML
    opens com.example.my_group_project to javafx.fxml;
    opens com.example.my_group_project.Controllers to javafx.fxml;
    opens com.example.my_group_project.Database to javafx.fxml;

    // Export packages to be used by other modules
    exports com.example.my_group_project;
    exports com.example.my_group_project.Controllers;
    exports com.example.my_group_project.Database;
    exports com.example.my_group_project.User;
    opens com.example.my_group_project.User to javafx.fxml;
    exports com.example.my_group_project.Controllers.User;
    opens com.example.my_group_project.Controllers.User to javafx.fxml;
    exports com.example.my_group_project.Controllers.Admin;
    opens com.example.my_group_project.Controllers.Admin to javafx.fxml;
}
