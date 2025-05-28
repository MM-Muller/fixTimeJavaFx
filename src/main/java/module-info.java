module com.fixtime.fixtimejavafx {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.fixtime.fixtimejavafx.model to javafx.base;
    opens com.fixtime.fixtimejavafx.view to javafx.fxml;
    exports com.fixtime.fixtimejavafx.app;
}
