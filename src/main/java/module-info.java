module me.wusel.testing {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires javacord;
    requires lavaplayer;

    opens me.wusel.testing to javafx.fxml;
    exports me.wusel.testing;
    exports me.wusel.testing.controllers;
    opens me.wusel.testing.controllers to javafx.fxml;
}